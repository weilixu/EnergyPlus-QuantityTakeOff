package analyzer.distributions;

import org.apache.commons.math3.distribution.BinomialDistribution;

/**
 * subclass of binomial distribution. It takes in double values (even for trial
 * numbers) It generates truncated samples
 * 
 * @author Weili
 *
 */
public class TruncatedBinomialDistribution extends BinomialDistribution {

    private double lower;
    private double higher;

    public TruncatedBinomialDistribution(double trials, double prob,
	    double lower, double higher) {

	super((int) trials, prob);
	this.lower = lower;
	this.higher = higher;
    }

    /**
     * sample from binomial distribution if the sample is not within the
     * selected range, this method will re-pick the sample
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
     * 
     * @param num
     * @return
     */
    public double[] truncatedSample(int num) {
	double[] samples = new double[num];
	for (int i = 0; i < num; i++) {
	    samples[i] = truncatedSample();
	}
	return samples;
    }
}
