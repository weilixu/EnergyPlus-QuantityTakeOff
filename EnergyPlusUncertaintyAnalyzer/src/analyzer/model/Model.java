package analyzer.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jfree.chart.ChartPanel;

import analyzer.distributions.DistributionType;
import analyzer.distributions.MakeDistributionModel;
import analyzer.eplus.EnergyPlusFilesGenerator;
import analyzer.eplus.IdfReader;
import analyzer.eplus.RunEnergyPlus;
import analyzer.graphs.GraphGenerator;
import analyzer.listeners.DistGenerationListeners;
import analyzer.listeners.FitDistListeners;
import analyzer.listeners.GraphGenerationListener;
import analyzer.listeners.LoadIdfListeners;
import analyzer.listeners.MakeDistGraphGeneratorListener;
import analyzer.listeners.ModelDataListener;

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
    
    private RVGenerator generator;
    /*
     * A data structure to save generated random variables from the model. The
     * size of the double[] array is equal to the simulaitonNumber. String
     * stands for the variableName
     */
    private static HashMap<String, double[]> randomVariableList = new HashMap<String, double[]>();
    private String distSummary;

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

    public Model() {
	idfData = new IdfReader();
	run = new RunEnergyPlus();
	generator = new RVGenerator();
	
	makeDistModel = new MakeDistributionModel();
	loadIDFListeners = new ArrayList<LoadIdfListeners>();
	distGeneListeners = new ArrayList<DistGenerationListeners>();
	dataListeners = new ArrayList<ModelDataListener>();
	fitDistListeners = new ArrayList<FitDistListeners>();
	mdGraphs = new ArrayList<MakeDistGraphGeneratorListener>();
	graphListeners = new ArrayList<GraphGenerationListener>();

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
     * @param g
     */
    public void addGraphGenerationListener(GraphGenerationListener g) {
	graphListeners.add(g);
    }
    
    

    /**
     * get the size of the data structure.
     * 
     * @return
     */
    public int getGeneratedVariableSize() {
	return randomVariableList.size();
    }
    
    public File getResultFolder(){
	return resultFile;
    }
    
    public void initializeModel(File file) throws IOException{
	eplusFile = file;
	parentFile = eplusFile.getParentFile();
	resultFile = createResultsFolder();
	run.setFolder(resultFile);
	
	initializedIdfData();
	
	initializedGraphGenerator();
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
    
    public void setDistributionType(DistributionType type){
	makeDistModel.setDistributionType(type);
    }
    
    public void setVariable(String variable){
	variableName = variable;
	generator.setVariableName(variableName);
    }
    
    /**
     * start running EnergyPlus simulation
     * @param eplus
     * @param weather
     * @param numberProc
     * @throws Exception
     */
    public void startSimulation(String eplus, String weather, String numberProc) throws Exception{
	run.setEnergyPlusDirectory(eplus);
	run.setWeatherFile(weather);
	run.setNumberOfProcessor(numberProc);
	
	try{
	    run.startSimulation();
	}catch(Exception e){
	    throw e;
	}
    }
    
    public void writeIdfFile(File targetFolder){
	for(int i = 0; i<simulationNumber; i++){
	    writingFile(idfData.cloneIdfData(),i, targetFolder);
	}
    }

    public HashMap<String, double[]> getData() {
	return randomVariableList;
    }

    /**
     * currently only work for re-do option when user enter re-do, model tells
     * GUI that the option should reopen
     */
    public void refreshGeneration() {
	onVariableEnabled();
    }

    /**
     * 
     * @param source
     *            Directory to save image to
     * @param imgName
     *            Name of file to save image to (e.g. "testImage.jpg")
     * @param data
     *            data to fit distribution to
     * @param numRV
     *            number of random variables to generate
     * @param sortby
     *            sortby is the likelihood function used to sort the fitted
     *            probability distribtuions. Valid inputs are: NLogL - Negative
     *            Log Likelihood BIC - Bayesian Information criterion AIC -
     *            Akaike information criterion AICc
     * @param dataType
     *            dataType specifies whether to fit a 'DISCRETE' or 'CONTINUOUS'
     *            distribution
     * @param lower
     *            lower bound where the generated random variables will be
     *            truncated tor
     * @param upper
     *            upper bound where the generated random variables will be
     *            truncated tor
     * @return
     */
    public void fitData(double[] data, String sortby, String dataType, String lower, String upper) {
	generator.setVariableName(variableName);
	double[] samples = generator.fitData(parentFile.getAbsolutePath(), data, simulationNumber, sortby, dataType, lower, upper);
	distSummary = generator.getDistSummary(); // convert to String

	onDistributionGenerated();
	onFitResultsUpdates();
	onVariableEnabled();
	randomVariableList.put(variableName, samples);
	onDataUpdates();
    }

    /**
     * 
     * @param source
     *            Directory to save image to
     * @param imgName
     *            Name of file to save image to (e.g. "testImage.jpg")
     * @param numRV
     *            number of random variables to generate
     * @param distrName
     *            name of distribution from which random variables would be
     *            generated from
     * @param distrParam
     *            1 X k vector of parameters for the distribution specified
     *            where k is the number of parameters
     * @param lower
     *            lower bound where the generated random variables will be
     *            truncated tor
     * @param upper
     *            upper bound where the generated random variables will be
     *            truncated tor **list of valid distribution and their
     *            parameters Distributions with 1 Parameter "Exponential": 'mu'
     *            - Mean parameter "Poisson": 'lambda' - Mean "Reyleigh": 'b' -
     *            Defining parameter Distributions with 2 Parameters "Beta": 'a'
     *            - First shape parameter; 'b' - Second shape parameter
     *            "Binomial": 'N' - Number of trials; 'p' - Probability of
     *            success "BirnbaumSaunders": 'beta' - Scale parameter; 'gamma'
     *            - Shape parameter "ExtremeValue": 'mu' - Location parameter;
     *            'sigma' - Scale parameter "Gamma": 'a' - Shape parameter; 'b'
     *            - Scale parameter "InverseGaussian": 'mu' - Scale parameter;
     *            'lambda' - Shape parameter "Logistic": 'mu' - Mean; 'lambda' -
     *            Shape parameter "Loglogistic": 'mu' - Mean; 'sigma' - Scale
     *            parameter "Lognormal": 'mu' - Log mean; 'sigma' - Log standard
     *            deviation "Nakagami": 'mu' - Shape parameter 'omega' - Scale
     *            parameter "NegativeBinomial": 'R' - Number of successes; 'p' -
     *            probability of success "Normal": 'mu' - Mean; 'sigma' -
     *            Standard deviation "Rician": 's' - Noncentrality parameter;
     *            'sigma' - Scale parameter "Uniform": 'lower' - Lower
     *            parameter; 'upper' - Upper parameter "Weibull": 'a' - Scale
     *            parameter; 'b' - Shape parameter Distributions with 3
     *            Parameters "Burr": 'alpha' - Scale parameter; 'c' - First
     *            shape parameter; 'k' - Second shape parameter
     *            "GeneralizedExtremeValue": 'k' - Shape parameter; 'sigma' -
     *            Scale parameter; 'mu' - Location parameter
     *            "GeneralizedPareto": 'k' - Shape parameter; 'sigma' - Scale
     *            parameter; 'theta' - Location parameter "tLocationScale": 'mu'
     *            - Location parameter; 'sigma' - Scale parameter; 'nu' -
     *            Degrees of freedom "Triangular": 'a' - Lower limit; 'b' - Peak
     *            location; 'c' - Upper limit
     * 
     */
    public void generateRV(double[] parameters) {
	double[] samples = makeDistModel.generateRnd(parameters);
	// updates GUIs
	randomVariableList.put(variableName, samples);
	onVariableEnabled();
	onSamplesUpdated(samples);
    }
    
    public void generateGraphs(){
	graphs.setResults();
	onUpdatedHistoGraphGenerated(graphs.getHistogramCharts());
	onUpdatedTimeSeriesGraphGenerated(graphs.getTimeSeriesCharts());
    }
    
    private void initializedGraphGenerator(){
	graphs = new GraphGenerator(resultFile, simulationNumber, idfData.getValue("RunPeriod", "Start Year"));
	graphs.setSized(idfData.getValue("SimulationControl", "Run Simulation for Sizing Periods"));
    }
    
    private void initializedIdfData() throws IOException{
	idfData.setFilePath(eplusFile.getAbsolutePath());
	idfData.readEplusFile();
	onReadEplusFile();
    }
    
    private void writingFile(EnergyPlusFilesGenerator reader, Integer index, File folder){
	Set<String> variable = randomVariableList.keySet();
	Iterator<String> iterator = variable.iterator();
	while(iterator.hasNext()){
	    String charactor = iterator.next();
	    Double value = randomVariableList.get(charactor)[index];

	    reader.modifySpecialCharactor(charactor, value.toString());
	}
	//write the file
	reader.WriteIdf(folder.getAbsolutePath(),index.toString());
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

    private void onFitResultsUpdates() {
	for (FitDistListeners fdl : fitDistListeners) {
	    if (fdl.getVariable().equals(variableName)) {
		fdl.fitDataGenerated(distSummary);
	    }
	}
    }

    private void onDataUpdates() {
	for (ModelDataListener m : dataListeners) {
	    m.modelDataUpdate(randomVariableList.size());
	}
    }

    private void onVariableEnabled() {
	for (ModelDataListener m : dataListeners) {
	    m.variableEnabled(variableName);
	}
    }
    
    /**
     * update the model
     * @param samples
     */
    private void onSamplesUpdated(double[] samples) {
	for (MakeDistGraphGeneratorListener mdl : mdGraphs) {
	    if(mdl.getVariable().equals(variableName)){
		    mdl.onUpdatedDistGenerated(samples);
	    }
	}
    }

    /*
     * notify GUI for the updates of variableList and the variableInfo
     */
    private void onReadEplusFile() {
	for (LoadIdfListeners l : loadIDFListeners) {
	    l.loadedEnergyPlusFile(idfData.getVariableList(), idfData.getVaraibleKeySets());
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

}
