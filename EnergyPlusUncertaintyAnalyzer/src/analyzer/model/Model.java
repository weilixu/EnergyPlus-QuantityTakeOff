package analyzer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import allfitdist.FitDist;
import allmakedist.MakeDist;
import analyzer.listeners.DistGenerationListeners;
import analyzer.listeners.FitDistListeners;
import analyzer.listeners.ModelDataListener;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

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
    // string for parent name
    private String source;
    // String for the variable name
    private String variableName;
    //record the number of the simulation to determine the size of data
    private int simulationNumber;
    
    
    /*
     * A data structure to save generated random variables from the model.
     * The size of the double[] array is equal to the simulaitonNumber. 
     * String stands for the variableName
     */
    private static HashMap<String, double[]> randomVariableList = new HashMap<String, double[]>();
    
    /*
     * Add all the listeners from GUI
     */
    // add the image panel listener to monitor the image generation
    private List<DistGenerationListeners> distGeneListeners;
    // listen the data from the model
    private List<ModelDataListener> dataListeners;
    // listen the data from the model for fit distribution results
    private List<FitDistListeners> fitDistListeners;

    public Model() {
	distGeneListeners = new ArrayList<DistGenerationListeners>();
	dataListeners = new ArrayList<ModelDataListener>();
<<<<<<< HEAD
	
=======
	fitDistListeners = new ArrayList<FitDistListeners>();
>>>>>>> 09251998cff682476b6c213caea2a6301ccc5616
    }

    /**
     * register the listener for the model. The GUI will communicate with the
     * model through this listener
     * @param d
     */
    public void addDistGeneListeners(DistGenerationListeners d) {
	distGeneListeners.add(d);
    }
    
    public void addModelDataListeners(ModelDataListener m){
	dataListeners.add(m);
    }
    
    public void addFitDistListeners(FitDistListeners f){
	fitDistListeners.add(f);
    }
    
    /**
     * get the size of the data structure.
     * @return
     */
    public int getGeneratedVariableSize(){
	return randomVariableList.size();
    }

    /**
     * set the file directory for the model
     * @param s
     */
    public void setSource(String s) {
	source = s;
    }

    /**
     * set the name of the variable for the model
     * @param v
     */
    public void setVariable(String v) {
	variableName = v;
    }

    /**
     * set the simulation number for the model
     * @param number
     */
    public void setSimulationNumber(int number) {
	simulationNumber = number;
    }
    
    public HashMap<String, double[]> getData(){
	return randomVariableList;
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
    public void fitData(double[] data, String sortby, String dataType,
	    String lower, String upper) {
	FitDist fitDistr = null;
	Object[] fitDistInputs = new Object[8];
	fitDistInputs[0] = source;
	fitDistInputs[1] = DIST_NAME + variableName + IMAGE_POST;
	fitDistInputs[2] = data;
	fitDistInputs[3] = simulationNumber;
	fitDistInputs[4] = sortby;
	fitDistInputs[5] = dataType;
	fitDistInputs[6] = Double.parseDouble(lower);
	fitDistInputs[7] = Double.parseDouble(upper);
	Object[] fitDistResult = null;
	try {
	    fitDistr = new FitDist();
	    fitDistResult = fitDistr.allfitdist(2, fitDistInputs);
	} catch (MWException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	MWNumericArray rndVars = (MWNumericArray) fitDistResult[0];
	Object[] output = new Object[2];
	// .getDoubleData returns double[] containing random variables
	output[0] = rndVars.getDoubleData();
<<<<<<< HEAD
	output[1] = fitDistResult[1].toString(); // convert to String
	return output;
=======
	output[1] = fitDistResult[1];
	
	onDistributionGenerated();
	onFitResultsUpdates();
	randomVariableList.put(variableName, (double[])output[0]);	
	onDataUpdates();
>>>>>>> 09251998cff682476b6c213caea2a6301ccc5616
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
    public void generateRV(String distrName, double[] distrParam,
	    String lower, String upper) {
	MakeDist makeDistr = null;
	Object[] makeDistInputs = new Object[7];
	makeDistInputs[0] = source;
	makeDistInputs[1] = DIST_NAME + variableName + IMAGE_POST;
	makeDistInputs[2] = simulationNumber;
	makeDistInputs[3] = distrName;
	makeDistInputs[4] = distrParam;
	makeDistInputs[5] = Double.parseDouble(lower); // min
	makeDistInputs[6] = Double.parseDouble(upper); // max

	Object[] makeDistResult = null;
	try {
	    makeDistr = new MakeDist();
	    makeDistResult = makeDistr.allmakedist(1, makeDistInputs);
	} catch (MWException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	MWNumericArray rndVars = (MWNumericArray) makeDistResult[0];
	//updates GUIs
	onDistributionGenerated();
	randomVariableList.put(variableName, rndVars.getDoubleData());	
	onDataUpdates();
    }
    
    private void onDistributionGenerated(){
	for(DistGenerationListeners dgl:distGeneListeners){
	    if(dgl.getVariable().equals(variableName)){
		StringBuffer sb = new StringBuffer();
		sb.append(source);
		sb.append("\\");
		sb.append(DIST_NAME);
		sb.append(variableName);
		sb.append(IMAGE_POST);
		dgl.loadDistImage(sb.toString());
	    }
	}
    }
    
    private void onFitResultsUpdates(){
	for(FitDistListeners fdl: fitDistListeners){
	    if(fdl.getVariable().equals(variableName)){
		StringBuffer sb = new StringBuffer();
		sb.append("this is just a sample");
		sb.append("\n");
		sb.append("to demonstrate this could work!");
		fdl.fitDataGenerated(sb);
	    }
	}
    }
    
    private void onDataUpdates(){
	for(ModelDataListener m: dataListeners){
	    m.modelDataUpdate(randomVariableList.size());
	}
    }
}
