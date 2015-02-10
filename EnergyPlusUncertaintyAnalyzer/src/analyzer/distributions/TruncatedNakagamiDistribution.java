package analyzer.distributions;

import org.apache.commons.math3.distribution.NakagamiDistribution;

public class TruncatedNakagamiDistribution extends NakagamiDistribution{
    
    private double lower;
    private double higher;
    
    public TruncatedNakagamiDistribution(double mu, double omega, double lower, double higher) {
	super(mu, omega);
	this.lower = lower;
	this.higher = higher;
    }
    
    /**
     * sample from nakagami distribution if the sample is not within the selected
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
