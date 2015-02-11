package analyzer.distributions;

import java.util.ArrayList;
import java.util.List;

import analyzer.listeners.MakeDistGraphGeneratorListener;

/**
 * This is the model for generating make distributions tab The model takes in
 * the number of simulations and generates an array of samples Also, the model
 * notifies the make distribution graph generator to updates the graph on the
 * panel
 * 
 * @author Weili
 *
 */
public class MakeDistributionModel {

    private int numSimulation;

    private final DistributionFactory factory;
    private DistributionType type;    
    
    public MakeDistributionModel() {
	factory = new DistributionFactory();
    }
    

    
    /**
     * set the simulation number
     * @param num
     */
    public void setSimulationNumber(int num) {
	numSimulation = num;
    }

    /**
     * set the distribution type of this model
     * @param type
     */
    public void setDistributionType(DistributionType type) {
	this.type = type;
	factory.setDistributionType(this.type);
    }
    
    /**
     * generates random variables
     * @param parameters
     */
    public double[] generateRnd(double[] parameters) {
	TruncatedDistribution dist = factory.getDistribution(parameters);
	double[] samples = dist.truncatedSample(numSimulation);
	return samples;
    }

}
