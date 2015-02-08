package analyzer.distributions;

import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * This is class performs exponential distribution This class only takes mean,
 * lower truncation and higher truncation for initialization
 * 
 * @author weilix
 *
 */
public class TruncatedExponentialDistribution extends ExponentialDistribution {
    
    private double lower;
    private double higher;
    
    
    public TruncatedExponentialDistribution(double mean, double lower, double higher) {
	super(mean);
	this.lower = lower;
	this.higher = higher;
    }
    
    /**
     * sample from exponential distribution if the sample is not within the selected
     * range, this method will re-pick the sample
     * 
     * @return
     */
    public double truncatedSample() {
	double rnd = sample();
	while (rnd < lower || rnd > higher) {
	    rnd = sample();
	}
	return rnd;
    }

}
