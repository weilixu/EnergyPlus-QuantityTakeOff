package analyzer.distributions;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

/**
 * Truncated poisson distribution
 * 
 * @author Weili
 *
 */
public class TruncatedPoissonDistribution extends PoissonDistribution implements TruncatedDistribution{
    
    private double lower;
    private double higher;

    public TruncatedPoissonDistribution(double p, double lower, double higher)
	    throws NotStrictlyPositiveException {
	super(p);
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
    public double[] truncatedSample(int num) {
	double[] samples = new double[num];
	for (int i = 0; i < num; i++) {
	    samples[i] = truncatedSample();
	}
	return samples;
    }

}
