package analyzer.distributions;

import org.apache.commons.math3.distribution.UniformRealDistribution;

public class TruncatedUniformRealDistribution extends UniformRealDistribution implements TruncatedDistribution{

    public TruncatedUniformRealDistribution(double lower,
	    double higher) {
	super(lower, higher);
    }

    @Override
    public double truncatedSample() {
	return sample();
    }

    @Override
    public double[] truncatedSample(int num) {
	return sample(num);
    }
}
