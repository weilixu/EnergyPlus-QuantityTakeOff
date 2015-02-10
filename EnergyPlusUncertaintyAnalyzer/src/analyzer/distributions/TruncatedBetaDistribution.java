package analyzer.distributions;

import org.apache.commons.math3.distribution.BetaDistribution;

/**
 * This subclass performs a truncated beta distribution This class only takes
 * alpha, beta and lower truncation higher truncation to initialize
 * 
 * @author weilix
 *
 */
public class TruncatedBetaDistribution extends BetaDistribution implements TruncatedDistribution{

    private double lower;
    private double higher;

    public TruncatedBetaDistribution(double alpha, double beta, double lower,
	    double higher) {
	super(alpha, beta);
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
