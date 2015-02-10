package analyzer.distributions;

import org.apache.commons.math3.distribution.LogNormalDistribution;

public class TruncatedLogNormalDistribution extends LogNormalDistribution{
    
    private double lower;
    private double higher;
    
    public TruncatedLogNormalDistribution(double mean, double sigma, double lower, double higher){
	super(mean, sigma);
	this.lower = lower;
	this.higher = higher;
    }
    
    /**
     * sample from log normal distribution if the sample is not within the selected
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
