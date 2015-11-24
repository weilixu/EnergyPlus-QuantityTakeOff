package analyzer.optimization.singleobjective;

import java.io.File;
import java.util.HashMap;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;
import eplus.IdfReader;
import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instances;

public class OPT1 extends Problem{
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
    
    public OPT1(HashMap<String, double[]> rl, IdfReader data, File folder
	    , int n, int Q, int p){
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
	
    }

    @Override
    public void evaluate(Solution arg0) throws JMException {
	// TODO Auto-generated method stub
	
    }
}
