package analyzer.optimization.singleobjective;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import analyzer.eplus.IdfReader;
import analyzer.eplus.RunEnergyPlusOptimization;
import analyzer.model.Model;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

/**
 * A single objective function to evaluate the Energy Usage Intensity of design options
 * Fitness function depends on EUI, the lower EUI a design option could get, the better chance it
 * suvives.
 * 
 * This class utilize genetic algorithm to perform optimization. A multithread method is implemented.
 * Population number should be identical to the number of thread. The maximum multithread is set to 4 cores
 * for the current version
 * @author Weili
 *
 */
public class EUI extends Problem{
    private String[] VariableName;
    private static Integer simulationCount = 0;
    private final IdfReader data;
    private File analyzeFolder;
    
    public EUI(HashMap<String, double[]> randomVariableList, IdfReader data, File folder){
	numberOfVariables_=randomVariableList.size();
	numberOfObjectives_=1;//this is a single objective optimization
	numberOfConstraints_=0;
	problemName_ = "Building EUI Optimization";
	this.data = data;
	analyzeFolder = folder;
	
	VariableName = new String[numberOfVariables_];
	upperLimit_ = new double[numberOfVariables_];
	lowerLimit_ = new double[numberOfVariables_];
	
	Set<String> variableList = randomVariableList.keySet();
	Iterator<String> variableIterator = variableList.iterator();
	int index = 0;
	while(variableIterator.hasNext()){
	    String variable = variableIterator.next();
	    VariableName[index] = variable;
	    upperLimit_[index] = getUpperLimit(randomVariableList.get(variable));
	    lowerLimit_[index] = getLowerLimit(randomVariableList.get(variable));
	    index++;
	}
	solutionType_ = new RealSolutionType(this);
    }
    
    
    @Override
    public void evaluate(Solution solution) throws JMException {
	Variable[] decisionVariables = solution.getDecisionVariables();
	RunEnergyPlusOptimization optimization = new RunEnergyPlusOptimization(data,decisionVariables,VariableName);
	optimization.setFolder(analyzeFolder);
	
	synchronized(this){
	    simulationCount++;
	    optimization.setSimulationTime(simulationCount);
	}
	
	try {
	    solution.setObjective(0, optimization.runSimulation());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    private double getUpperLimit(double[] data){
	double biggest = Double.MIN_VALUE;
	for(int i=0; i<data.length; i++){
	    if(data[i]>biggest){
		biggest = data[i];
	    }
	}
	return biggest;
    }
    
    private double getLowerLimit(double[] data){
	double smallest = Double.MAX_VALUE;
	for(int i=0; i<data.length; i++){
	    if(data[i]<smallest){
		smallest = data[i];
	    }
	}
	return smallest;
    }
}
