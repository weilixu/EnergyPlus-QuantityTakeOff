package analyzer.distributions;

import org.apache.commons.math3.distribution.BinomialDistribution;

/**
 * subclass of binomial distribution. It takes in double values (even for trial
 * numbers) It generates truncated samples
 * 
 * @author Weili
 *
 */
public class TruncatedBinomialDistribution extends BinomialDistribution implements TruncatedDistribution{

    private double lower;
    private double higher;

    public TruncatedBinomialDistribution(double trials, double prob,
	    double lower, double higher) {

	super((int) trials, prob);
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
