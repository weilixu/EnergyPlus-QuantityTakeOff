package analyzer.distributions;

import org.apache.commons.math3.distribution.BetaDistribution;

/**
 * This subclass performs a truncated beta distribution This class only takes
 * alpha, beta and lower truncation higher truncation to initialize
 * 
 * @author weilix
 *
 */
public class TruncatedBetaDistribution extends BetaDistribution {

    private double lower;
    private double higher;

    public TruncatedBetaDistribution(double alpha, double beta, double lower,
	    double higher) {
	super(alpha, beta);
	this.lower = lower;
	this.higher = higher;
    }

    /**
     * sample from normal distribution if the sample is not within the selected
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
