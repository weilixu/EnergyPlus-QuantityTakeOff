package analyzer.distributions;

import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * This is class performs exponential distribution This class only takes mean,
 * lower truncation and higher truncation for initialization
 * 
 * @author weilix
 *
 */
public class TruncatedExponentialDistribution extends ExponentialDistribution implements TruncatedDistribution{
    
    private double lower;
    private double higher;
    
    
    public TruncatedExponentialDistribution(double mean, double lower, double higher) {
	super(mean);
	this.lower = lower;
	this.higher = higher;
    }
    
    @Override
    public double truncatedSample() {
	double rnd = sample();
	while (rnd < lower || rnd > higher) {
	    rnd = sample();
	}
	return rnd;
    }
    
    @Override
    public double[] truncatedSample(int num){
	double[] samples = new double[num];
	for(int i=0; i<num; i++){
	    samples[i]=truncatedSample();
	}
	return samples;
    }

}
