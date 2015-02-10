package analyzer.distributions;

import java.util.ArrayList;
import java.util.List;

import analyzer.listeners.MakeDistGraphGeneratorListener;

public class MakeDistributionModel {
    
    private int numSimulation;
    private String variable;
    
    
    private final DistributionFactory factory;
    private DistributionType type;
    
    private List<MakeDistGraphGeneratorListener> mdGraphs;
    
    public MakeDistributionModel(){
	factory = new DistributionFactory();
	mdGraphs = new ArrayList<MakeDistGraphGeneratorListener>();
    }
    
    public void addMakeDistGraphGeneratorListener(MakeDistGraphGeneratorListener mdL){
	mdGraphs.add(mdL);
    }
    
    public void setSimulationNumber(int num){
	numSimulation = num;
    }
    
    public void setVariable(String v){
	variable = v;
    }
    
    public void setDistributionType(DistributionType type){
	this.type = type;
	factory.setDistributionType(this.type);
    }
    
    public void generateRnd(double[] parameters){
	TruncatedDistribution dist = factory.getDistribution(parameters);
	onSamplesUpdated(dist.truncatedSample(numSimulation));
    }
    
    private void onSamplesUpdated(double[] samples){
	for(MakeDistGraphGeneratorListener mdl: mdGraphs){
	    mdl.onUpdatedDistGenerated(samples);
	}
    }
}
