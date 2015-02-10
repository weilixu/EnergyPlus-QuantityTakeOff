package analyzer.distributions;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

/**
 * This class performs truncated gamma distribution This class takes shape,
 * scale and lower truncation and higher truncation for initialization
 * 
 * @author weilix
 *
 */
public class TruncatedGammaDistribution extends GammaDistribution {
    
    private double lower;
    private double higher;
    
    public TruncatedGammaDistribution(double shape, double scale, double lower, double higher)
	    throws NotStrictlyPositiveException {
	super(shape, scale);
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
    
    /**
     * generate a number of samples
     * @param num
     * @return
     */
    public double[] truncatedSample(int num){
	double[] samples = new double[num];
	for(int i=0; i<num; i++){
	    samples[i]=truncatedSample();
	}
	return samples;
    }

}
