package analyzer.optimization.singleobjective;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import analyzer.eplus.IdfReader;
import analyzer.eplus.RunEnergyPlusOptimization;
import analyzer.htmlparser.EnergyPlusHTMLParser;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class OPT1 extends Problem {
    /*
     * Adaptive regression algorithm parameters
     */
    private int generationNumForSim;
    private int generationNumForCir;
    private int trainNumber;
    private Instances o1TrainSet;
    private Instances o2TrainSet;
    private FastVector fvO1Attributes;
    private FastVector fvO2Attributes;
    private Classifier o1Classifier;
    private Classifier o2Classifier;

    /*
     * Energy Simulation Parameters
     */
    private static final Object lock = new Object();
    private static Integer simulationCount = 0;
    private IdfReader originalData;
    private File analyzeFolder;
    private int population;
    private HashMap<String, double[]> randomVarList;
    private String[] variableList;

    public OPT1(HashMap<String, double[]> rl, IdfReader data, File folder,
	    int n, int Q, int p) {
	randomVarList = rl;
	originalData = data;
	analyzeFolder = folder;

	generationNumForSim = n;
	generationNumForCir = Q;
	population = p;
	fvO1Attributes = new FastVector(randomVarList.size());
	fvO2Attributes = new FastVector(randomVarList.size());

	/*
	 * set-up optimization parameters
	 */
	numberOfVariables_ = randomVarList.size();
	numberOfObjectives_ = 2;
	numberOfConstraints_ = 0;
	problemName_ = "Calibration";

	upperLimit_ = new double[numberOfVariables_];
	lowerLimit_ = new double[numberOfVariables_];

	variableList = new String[randomVarList.size()];
	Iterator<String> variableIterator = randomVarList.keySet().iterator();
	int index = 0;
	while (variableIterator.hasNext()) {
	    String name = variableIterator.next();
	    // take out the special character
	    name = name.substring(name.indexOf("$"));
	    double[] list = randomVarList.get(name);
	    // find the min and max data from the list
	    double min = Double.MAX_VALUE;
	    double max = Double.MIN_VALUE;
	    for (int i = 0; i < list.length; i++) {
		if (list[i] > max) {
		    max = list[i];
		}
		if (list[i] < min) {
		    min = list[i];
		}
	    }
	    // assign min and max to the list
	    lowerLimit_[index] = min;
	    upperLimit_[index] = max;
	    variableList[index] = name;
	    Attribute temp = new Attribute(name);
	    fvO1Attributes.addElement(temp);
	    fvO2Attributes.addElement(temp);

	    index++;
	}
	solutionType_ = new RealSolutionType(this);
	Attribute cvrmse = new Attribute("cvrmse");
	Attribute nmbe = new Attribute("nmbe");
	fvO1Attributes.addElement(cvrmse);
	fvO2Attributes.addElement(nmbe);
	o1TrainSet = new Instances("Rel", fvO1Attributes, 0);
	o2TrainSet = new Instances("Rel", fvO2Attributes, 0);
	o1TrainSet.setClassIndex(o1TrainSet.numAttributes() - 1);
	o2TrainSet.setClassIndex(o2TrainSet.numAttributes() - 1);
    }

    @Override
    public void evaluate(Solution solution) throws JMException {
	Variable[] decisionVariables = solution.getDecisionVariables();
	IdfReader copiedData = originalData.cloneIdf();
	boolean realSimulation = true;
	RunEnergyPlusOptimization optimization = null;

	// add one class index
	Instance o1Ins = new Instance(decisionVariables.length + 1);
	Instance o2Ins = new Instance(decisionVariables.length + 1);
	o1Ins.setDataset(o1TrainSet);
	o2Ins.setDataset(o2TrainSet);

	synchronized (OPT1.lock) {
	    // create flag that determine whether we can run on regression or
	    // real simulation
	    Double generation = Math.floor(simulationCount / population);
	    int newGenCounter = (generation.intValue()) % generationNumForCir;
	    simulationCount++;
	    System.out.println(generation + " " + newGenCounter);
	    /*
	     * 1. Case run real simulation
	     */
	    if (newGenCounter < generationNumForSim) {
		realSimulation = true;
		System.out.println("Real Simulation");
		// 1.1 rebuild training data or continuously build up
		if (o1Classifier != null & o2Classifier != null) {
		    o1Classifier = null;
		    o2Classifier = null;
		    o1TrainSet = new Instances(o1TrainSet, 0);
		    o2TrainSet = new Instances(o2TrainSet, 0);
		}// if

		// 1.2 modify the idf according to generated data
		for (int i = 0; i < decisionVariables.length; i++) {
		    String name = variableList[i];
		    Double value = (Double) decisionVariables[i].getValue();

		    // add value
		    o1Ins.setValue(i, value);
		    o2Ins.setValue(i, value);

		    copiedData.modifySpecialCharactor("$" + name,
			    value.toString());
		}// for
		optimization = new RunEnergyPlusOptimization(copiedData);
		optimization.setFolder(analyzeFolder);
		optimization.setSimulationTime(simulationCount);
	    } else {
		// 2 Regression Case
		realSimulation = false;
		System.out.println("Regression Simulation");

		if (o1Classifier == null && o2Classifier == null) {
		    System.out
			    .println("Regression Simulation, need to train model");

		    // 2.1 build a new classifier if it is the start of
		    // simulation case
		    try {
			o1Classifier = trainModel(o1TrainSet);
			o2Classifier = trainModel(o2TrainSet);
			dataForplot();
		    } catch (Exception e) {
			System.err
				.println("Exception caught when classifying the training data");
			e.printStackTrace();
		    }//try-catch
		}//if
		
		//2.2 give design values to the instance
		for(int i=0; i<decisionVariables.length; i++){
		    Double value = (Double) decisionVariables[i].getValue();
		    // add value
		    o1Ins.setValue(i, value);
		    o2Ins.setValue(i, value);   
		}//for
	    }//else
	}//synchronized
	
	/*
	 * 3. Done with settings, test results
	 */
	if(realSimulation){
	    //3.1 Require real simulation cases
	    EnergyPlusHTMLParser parser = null;
	    try{
		parser = optimization.runSimulation();
		// retrieve the results - fitness function
		
	    }catch (IOException e) {
		    e.printStackTrace();
	    }// try-catch
	}else{
	    //3.2 Regression mode

	}
    }

    private Classifier trainModel(Instances data) throws Exception {
	String[] algorithm = { "SVM", "LR" }; // algorithms
	String bestSelected = null;
	double minRootMeanError = Double.MAX_VALUE;
	for (int i = 0; i < algorithm.length; i++) {
	    String currentAlg = algorithm[i];
	    Classifier temp = WekaUtil.getClassifier(currentAlg);
	    double tempRME = WekaUtil.evaluateCrossValidate(data, temp);
	    if (minRootMeanError >= tempRME) {
		minRootMeanError = tempRME;
		bestSelected = currentAlg;
	    }
	}
	return WekaUtil.buildClassifier(data, bestSelected);
    }

    private void dataForplot() {
	trainNumber++;
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < o1TrainSet.numInstances(); i++) {
	    try {
		Instance ins = o1TrainSet.instance(i);
		Instance insC = o2TrainSet.instance(i);
		double operation = o1Classifier.classifyInstance(ins);
		double capital = o2Classifier.classifyInstance(insC);
		for (int j = 0; j < ins.numAttributes(); j++) {
		    sb.append(ins.value(j));
		    sb.append(",");
		}
		sb.append(operation);
		sb.append(",");
		sb.append(capital);
		sb.append(",");
		sb.append(o1TrainSet.instance(i).classValue());
		sb.append(",");
		sb.append(o2TrainSet.instance(i).classValue());
		sb.append("\n");
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    try {
		File file = new File(
			"E:\\02_Weili\\02_ResearchTopic\\Optimization\\predict"
				+ trainNumber + ".csv");
		// if file doesnt exists, then create it
		if (!file.exists()) {
		    file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(sb.toString());
		bw.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}
