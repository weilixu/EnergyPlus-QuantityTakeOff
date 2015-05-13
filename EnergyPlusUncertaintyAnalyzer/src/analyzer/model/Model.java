package analyzer.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.pgGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.parallel.IParallelEvaluator;
import jmetal.util.parallel.MultithreadedEvaluator;

import org.jfree.chart.ChartPanel;

import de.erichseifert.gral.ui.InteractivePanel;
import analyzer.distributions.DistributionType;
import analyzer.distributions.MakeDistributionModel;
import analyzer.eplus.EnergyPlusFilesGenerator;
import analyzer.eplus.IdfReader;
import analyzer.eplus.RunEnergyPlus;
import analyzer.eplus.RunEnergyPlusOptimization;
import analyzer.graphs.GraphGenerator;
import analyzer.htmlparser.BuildingAreaParser;
import analyzer.lang.AnalyzerException;
import analyzer.lang.AnalyzerUtils;
import analyzer.lifecyclecost.DataObjects;
import analyzer.lifecyclecost.FieldElement;
import analyzer.lifecyclecost.LifeCycleCostModel;
import analyzer.lifecyclecost.TemplateObject;
import analyzer.lifecyclecost.squaremeterestimation.BuildingType;
import analyzer.lifecyclecost.squaremeterestimation.SquareMeterModel;
import analyzer.listeners.DistGenerationListeners;
import analyzer.listeners.FitDistListeners;
import analyzer.listeners.GraphGenerationListener;
import analyzer.listeners.LoadIdfListeners;
import analyzer.listeners.MakeDistGraphGeneratorListener;
import analyzer.listeners.ModelDataListener;
import analyzer.listeners.SquareMeterCostModelListener;
import analyzer.optimization.singleobjective.EUI;
import analyzer.sensitivity.SensitivityAnalysis;

/**
 * Distribution model
 * 
 * @author Adrian Weili
 *
 */
public class Model {
    // Strings for the file names
    private final String DIST_NAME = "DIST_";
    private final String IMAGE_POST = ".jpg";

    // total cost simulation number
    private static final int totalCostSimulationNumber = 1000;

    private String variableName;

    // record the number of the simulation to determine the size of data
    private int simulationNumber;

    /*
     * Folders
     */
    private File eplusFile;
    private File parentFile;
    private File resultFile;

    /*
     * All about make distribution model
     */
    private final MakeDistributionModel makeDistModel;
    private DistributionType type;

    /**
     * idf reader <link>IdfReader<link>
     */
    private final IdfReader idfData;

    /**
     * graph generator <link>GraphGenerator<link>
     */
    private GraphGenerator graphs;
    /**
     * <link>RunEnergyPlus<link>
     */
    private RunEnergyPlus run;

    /**
     * fit distribution generator
     */
    private RVGenerator generator;

    /**
     * fit multiple variable distribution
     */
    private GenerateFromCSV multiGenerator;

    /**
     * sensitivity analysis module
     */
    private SensitivityAnalysis sensitivity;

    /**
     * Life cycle cost module
     */
    private LifeCycleCostModel lccModel;

    /**
     * Square meter cost estimation model
     */
    private SquareMeterModel squaremeterModel;

    /**
     * Building area html parser
     */
    private BuildingAreaParser buildingArea;

    /*
     * A data structure to save generated random variables from the model. The
     * size of the double[] array is equal to the simulaitonNumber. String
     * stands for the variableName
     */
    private static HashMap<String, double[]> randomVariableList = new HashMap<String, double[]>();
    private HashMap<String, double[]> squareCostDataMap;

    private String distSummary;
    // to record the add-on modules
    private DataObjects currentDataSet;

    /*
     * Add all the listeners from GUI
     */
    // GUI listener for IdfReader
    private List<LoadIdfListeners> loadIDFListeners;
    // add the image panel listener to monitor the image generation
    private List<DistGenerationListeners> distGeneListeners;
    // listen the data from the model
    private List<ModelDataListener> dataListeners;
    // listen the data from the model for fit distribution results
    private List<FitDistListeners> fitDistListeners;
    // listen the make distribution from the model
    private List<MakeDistGraphGeneratorListener> mdGraphs;
    // listen to the graph generator
    private List<GraphGenerationListener> graphListeners;
    // listen to the square meter generator
    private List<SquareMeterCostModelListener> costModelListeners;

    public Model() {
	idfData = new IdfReader();
	run = new RunEnergyPlus();
	generator = new RVGenerator();
	multiGenerator = new GenerateFromCSV();
	lccModel = new LifeCycleCostModel();
	squaremeterModel = new SquareMeterModel();

	makeDistModel = new MakeDistributionModel();
	loadIDFListeners = new ArrayList<LoadIdfListeners>();
	distGeneListeners = new ArrayList<DistGenerationListeners>();
	dataListeners = new ArrayList<ModelDataListener>();
	fitDistListeners = new ArrayList<FitDistListeners>();
	mdGraphs = new ArrayList<MakeDistGraphGeneratorListener>();
	graphListeners = new ArrayList<GraphGenerationListener>();
	costModelListeners = new ArrayList<SquareMeterCostModelListener>();

	distSummary = "";
    }

    /**
     * register the listener for the model. The GUI will communicate with the
     * model through this listener
     * 
     * @param d
     */
    public void addDistGeneListeners(DistGenerationListeners d) {
	distGeneListeners.add(d);
    }

    public void addModelDataListeners(ModelDataListener m) {
	dataListeners.add(m);
    }

    public void addFitDistListeners(FitDistListeners f) {
	fitDistListeners.add(f);
    }

    /**
     * add the listener <link>MakeDistDisplayPanel<link>
     * 
     * @param mdL
     */
    public void addMakeDistGraphGeneratorListener(
	    MakeDistGraphGeneratorListener mdL) {
	mdGraphs.add(mdL);
    }

    public void addLoadIDFListeners(LoadIdfListeners l) {
	loadIDFListeners.add(l);
    }

    /**
     * add GUI listener to listen this class
     * 
     * @param g
     */
    public void addGraphGenerationListener(GraphGenerationListener g) {
	graphListeners.add(g);
    }

    public void addCostModelListener(SquareMeterCostModelListener ml) {
	costModelListeners.add(ml);
    }

    /**
     * add the current data set into Idf
     */
    public void addCurrentDataSetToIdf() {
	ArrayList<TemplateObject> objects = currentDataSet.getObjects();
	for (TemplateObject object : objects) {
	    ArrayList<FieldElement> fields = object.getFieldList();
	    String[] objectValues = new String[fields.size()];
	    String[] objectDes = new String[fields.size()];
	    for (int i = 0; i < fields.size(); i++) {
		objectValues[i] = fields.get(i).getValue();
		objectDes[i] = fields.get(i).getDescription();
	    }
	    idfData.addNewEnergyPlusObject(object.getObject(), objectValues,
		    objectDes);
	    // notify any possible changes in the eplus database
	    onReadEplusFile();
	}
    }

    /**
     * get the size of the data structure.
     * 
     * @return
     */
    public int getGeneratedVariableSize() {
	return randomVariableList.size();
    }

    public File getResultFolder() {
	return resultFile;
    }

    public HashMap<String, double[]> getData() {
	return randomVariableList;
    }

    public DefaultMutableTreeNode getLCCCompleteTree() {
	return lccModel.getCompleteTreeNode();
    }

    public DataObjects getCopiedDataObject(DataObjects dataset) {
	return lccModel.makeCopyOfDataSet(dataset);
    }

    /**
     * set the simulation number for the model
     * 
     * @param number
     */
    public void setSimulationNumber(int number) {
	simulationNumber = number;
	makeDistModel.setSimulationNumber(simulationNumber);
    }

    public void setDistributionType(DistributionType type) {
	makeDistModel.setDistributionType(type);
    }

    public void setVariable(String variable) {
	variableName = variable;
	generator.setVariableName(variableName);
    }

    public void setCurrentDataset(DataObjects dataSet) {
	currentDataSet = dataSet;
    }

    public void initializeModel(File file) throws IOException {
	eplusFile = file;
	parentFile = eplusFile.getParentFile();
	resultFile = createResultsFolder();
	run.setFolder(resultFile);
	initializedIdfData();
	initializedGraphGenerator();
    }

    /**
     * start running EnergyPlus simulation
     * 
     * @param eplus
     * @param weather
     * @param numberProc
     * @throws Exception
     */
    public void startSimulation(String eplus, String weather, String numberProc)
	    throws Exception {
	run.setEnergyPlusDirectory(eplus);
	run.setWeatherFile(weather);
	run.setNumberOfProcessor(numberProc);

	try {
	    run.startSimulation();
	} catch (Exception e) {
	    throw e;
	}
    }

    public void initializeSenstivityAnalysis() {
	ArrayList<String> variableList = idfData.getVariableList();
	String[] vn = new String[variableList.size()];
	for (int i = 0; i < variableList.size(); i++) {
	    vn[i] = variableList.get(i);
	}
	sensitivity = new SensitivityAnalysis(vn, graphs.getResults(),
		randomVariableList, graphs.getMissingList());
	double[] results = sensitivity.getCorrelation();
	System.out.println(Arrays.toString(results));
    }

    /**
     * Write the idf file to the result folder
     * 
     * @param targetFolder
     */
    public void writeIdfFile(File targetFolder) {
	for (int i = 0; i < simulationNumber; i++) {
	    writingFile(idfData.cloneIdfData(), i, targetFolder);
	}
    }

    /**
     * currently only work for re-do option when user enter re-do, model tells
     * GUI that the option should reopen
     */
    public void refreshGeneration() {
	onVariableEnabled();
    }

    /**
     * Fit the CSV data into a distribution
     * 
     * @param data
     *            : data extracted from a CSV file
     * @param sortby
     *            : sorting criteria
     * @param dataType
     *            : continuous or discrete
     * @param lower
     *            : truncation lower bound
     * @param upper
     *            : truncation upper bound
     */
    public void fitData(double[] data, String sortby, String dataType,
	    String lower, String upper) {
	generator.setVariableName(variableName);
	Double simNumber = simulationNumber / 1.0;
	double[] samples = generator.fitData(parentFile.getAbsolutePath(),
		data, simNumber, sortby, dataType, lower, upper);
	distSummary = generator.getDistSummary(); // convert to String
	// updates gui
	onDistributionGenerated();
	onFitResultsUpdates();
	onVariableEnabled();
	randomVariableList.put(variableName, samples);
	onDataUpdates();
    }

    /**
     * This method is used for mutli-RV generation
     */
    public void fitData(File file, String sortby, String dataType)
	    throws AnalyzerException {
	// set the file
	multiGenerator.setFileDirectory(file);
	// generate random variable
	multiGenerator.generateRV(simulationNumber, sortby, dataType);

	double[][] data = multiGenerator.getData();
	String[] headerList = multiGenerator.getHeaderList();
	String[] distSummary = multiGenerator.getDistSummary();

	if (headerList.length != distSummary.length
		&& headerList.length != data.length) {
	    throw new AnalyzerException(
		    "CSV Data Format Error: The number of variables does not match the number of data columns");
	}

	// updates gui
	onDistributionGenerated(headerList);
	onFitResultsUpdates(headerList, distSummary);
	onVariableEnabled(headerList);

	for (int i = 0; i < headerList.length; i++) {
	    randomVariableList.put(headerList[i], data[i]);
	}

	onDataUpdates();
    }

    /**
     * generates distribution based on user input
     * 
     * @param parameters
     */
    public void generateRV(double[] parameters) {
	double[] samples = makeDistModel.generateRnd(parameters);
	// updates GUIs
	randomVariableList.put(variableName, samples);
	onVariableEnabled();
	onSamplesUpdated(samples);
	onDataUpdates();
    }

    /**
     * generates graphs
     */
    public void generateGraphs() {
	graphs.setResults();
	onUpdatedHistoGraphGenerated(graphs.getHistogramCharts());
	// onUpdatedTimeSeriesGraphGenerated(graphs.getTimeSeriesCharts());
	// initializeSenstivityAnalysis();
	onUpdateBoxPlotGenerated(graphs.getBoxandWhiskerCharts());
    }

    public void generateTotalCost(BuildingType t) {
	// initialize the html file
	String resultPath = resultFile.getAbsoluteFile() + "//0Table.html";
	File html = new File(resultPath);
	buildingArea = new BuildingAreaParser(html);

	squaremeterModel.setBuildingSize(buildingArea.getBuildingArea());
	squaremeterModel.setSimulationNumber(totalCostSimulationNumber);
	squaremeterModel.setBuildingType(t);
	squareCostDataMap = squaremeterModel.generateSamples();
	onUpdatedCostDataMap();
    }

    /**
     * export the inputs to a csv file. The exported file will be saved under
     * parent folder This method should be called after the random variable map
     * filled with data.
     */
    public void exportInputs() {
	try {
	    FileWriter writer = new FileWriter(parentFile.getAbsoluteFile()
		    + "\\inputs.csv");

	    Set<String> variableList = randomVariableList.keySet();
	    Iterator<String> variableIterator = variableList.iterator();
	    while (variableIterator.hasNext()) {
		String variable = variableIterator.next();
		writer.append(variable);
		double[] inputList = randomVariableList.get(variable);
		for (double d : inputList) {
		    writer.append(",");
		    writer.append("" + d);
		}
		writer.append("\n");
	    }
	    writer.flush();
	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /*
     * OPTIMIZATION RELATED FUNCTIONS
     */
    public void singleObjectiveOptimization() throws JMException,
	    ClassNotFoundException {
	Problem problem = new EUI(randomVariableList, idfData,resultFile);
	int threads = Integer.parseInt(AnalyzerUtils.getEplusConfig()[2]);
	IParallelEvaluator parallelEvaluator = new MultithreadedEvaluator(
		threads);

	Algorithm algorithm = new pgGA(problem, parallelEvaluator); // Generational
								    // GA
	/* Algorithm parameters */
	algorithm.setInputParameter("populationSize", threads);
	algorithm.setInputParameter("maxEvaluations", 100);

	int bits = 512;

	HashMap<String, Double> parameters; // Operator parameter
	// Mutation and Crossover for Binary codification
	parameters = new HashMap<String, Double>();
	parameters.put("probability", 0.9);
	Operator crossover = CrossoverFactory.getCrossoverOperator(
		"SBXCrossover", parameters);

	parameters = new HashMap<String, Double>();
	parameters.put("probability", 1.0 / bits);
	Operator mutation = MutationFactory.getMutationOperator(
		"PolynomialMutation", parameters);

	/* Selection Operator */
	parameters = null;
	Operator selection = SelectionFactory.getSelectionOperator(
		"BinaryTournament", parameters);

	/* Add the operators to the algorithm */
	algorithm.addOperator("crossover", crossover);
	algorithm.addOperator("mutation", mutation);
	algorithm.addOperator("selection", selection);

	/* Execute the Algorithm */
	long initTime = System.currentTimeMillis();
	SolutionSet population = algorithm.execute();
	long estimatedTime = System.currentTimeMillis() - initTime;
	System.out.println("Total execution time: " + estimatedTime);

	/* Log messages */
	System.out.println("Objectives values have been writen to file FUN");
	population.printObjectivesToFile("FUN");
	System.out.println("Variables values have been writen to file VAR");
	population.printVariablesToFile("VAR");
    }

    /**
     * Below are all utility method that be called within this class for
     * initialization or acting as helpers or notifying listeners
     */
    private void initializedGraphGenerator() {
	graphs = new GraphGenerator(resultFile, simulationNumber,
		idfData.getValue("RunPeriod", "Start Year"));
	graphs.setSized(idfData.getValue("SimulationControl",
		"Run Simulation for Sizing Periods"));
    }

    private void initializedIdfData() throws IOException {
	idfData.setFilePath(eplusFile.getAbsolutePath());
	idfData.readEplusFile();
	onReadEplusFile();
    }

    private void writingFile(EnergyPlusFilesGenerator reader, Integer index,
	    File folder) {
	Set<String> variable = randomVariableList.keySet();
	Iterator<String> iterator = variable.iterator();
	while (iterator.hasNext()) {
	    String charactor = iterator.next();
	    Double value = randomVariableList.get(charactor)[index];

	    reader.modifySpecialCharactor(charactor, value.toString());
	}
	// write the file
	reader.WriteIdf(folder.getAbsolutePath(), index.toString());
    }

    /**
     * Create a file under parent folder that contains all the simulation
     * results
     * 
     * @return folder
     */
    private File createResultsFolder() {
	File dir = new File(parentFile.getAbsoluteFile() + "\\" + "Results");
	if (dir.exists()) {
	    return dir;
	} else {
	    dir.mkdir();
	    return dir;
	}
    }

    /**
     * Listeners
     */
    /*
     * Listener updates the distribution graph
     */
    private void onDistributionGenerated() {
	for (DistGenerationListeners dgl : distGeneListeners) {
	    if (dgl.getVariable().equals(variableName)) {
		StringBuffer sb = new StringBuffer();
		sb.append(parentFile.getAbsolutePath());
		sb.append("\\");
		sb.append(DIST_NAME);
		sb.append(variableName);
		sb.append(IMAGE_POST);
		dgl.loadDistImage(sb.toString());
	    }
	}
    }

    private void onDistributionGenerated(String[] variables) {
	// does the matlab generates images under same directory?
	// this method assumes all the generated graphs is under the same folder
	// as idf file
	for (DistGenerationListeners dgl : distGeneListeners) {
	    for (String v : variables) {
		if (dgl.getVariable().equals(v)) {
		    StringBuffer sb = new StringBuffer();
		    sb.append(parentFile.getAbsolutePath());
		    sb.append("\\");
		    sb.append(v);
		    sb.append(IMAGE_POST);
		    dgl.loadDistImage(sb.toString());
		}
	    }
	}
    }

    /*
     * Listener updates the fit distribution summary
     */
    private void onFitResultsUpdates() {
	for (FitDistListeners fdl : fitDistListeners) {
	    if (fdl.getVariable().equals(variableName)) {
		fdl.fitDataGenerated(distSummary);
	    }
	}
    }

    private void onFitResultsUpdates(String[] variables, String[] distSummaries) {
	for (FitDistListeners fdl : fitDistListeners) {
	    for (int i = 0; i < variables.length; i++) {
		if (fdl.getVariable().equals(variables[i])) {
		    fdl.fitDataGenerated(distSummaries[i]);
		}
	    }
	}
    }

    /*
     * Listener udpates the create/simulate button status
     */
    private void onDataUpdates() {
	for (ModelDataListener m : dataListeners) {
	    m.modelDataUpdate(randomVariableList.size());
	}
    }

    /*
     * Listener udpates the variable list
     */
    private void onVariableEnabled() {
	for (ModelDataListener m : dataListeners) {
	    m.variableEnabled(variableName);
	}
    }

    private void onVariableEnabled(String[] variables) {
	for (ModelDataListener m : dataListeners) {
	    for (String v : variables) {
		m.variableEnabled(v);
	    }
	}
    }

    /**
     * update the model
     * 
     * @param samples
     */
    private void onSamplesUpdated(double[] samples) {
	for (MakeDistGraphGeneratorListener mdl : mdGraphs) {
	    if (mdl.getVariable().equals(variableName)) {
		mdl.onUpdatedDistGenerated(samples);
	    }
	}
    }

    /*
     * notify GUI for the updates of variableList and the variableInfo
     */
    private void onReadEplusFile() {
	for (LoadIdfListeners l : loadIDFListeners) {
	    l.loadedEnergyPlusFile(idfData.getVariableList(),
		    idfData.getVaraibleKeySets());
	}
    }

    private void onUpdateBoxPlotGenerated(List<InteractivePanel> boxCharts) {
	for (GraphGenerationListener g : graphListeners) {
	    g.boxPlotGraphGenerated(boxCharts);
	}
    }

    /**
     * notify GUI the changes in the time series charts
     */
    private void onUpdatedTimeSeriesGraphGenerated(List<ChartPanel> timeCharts) {
	for (GraphGenerationListener g : graphListeners) {
	    g.timeSeriesGraphGenerated(timeCharts);
	}
    }

    /**
     * notify GUI the changes in the histogram charts
     */
    private void onUpdatedHistoGraphGenerated(List<ChartPanel> histoCharts) {
	for (GraphGenerationListener g : graphListeners) {
	    g.histogramGraphGenerated(histoCharts);
	}
    }

    private void onUpdatedCostDataMap() {
	for (SquareMeterCostModelListener ml : costModelListeners) {
	    ml.costInfoUpdated(squareCostDataMap);
	}
    }

}
