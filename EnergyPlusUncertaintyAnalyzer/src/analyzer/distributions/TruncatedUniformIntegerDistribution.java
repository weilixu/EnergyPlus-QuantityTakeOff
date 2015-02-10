package analyzer.distributions;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

public class TruncatedUniformIntegerDistribution extends UniformIntegerDistribution implements TruncatedDistribution{

    public TruncatedUniformIntegerDistribution(double lower, double upper)
	    throws NumberIsTooLargeException {
	super((int)lower, (int)upper);
    }

    @Override
    public double truncatedSample() {
	return sample();
    }

    @Override
    public double[] truncatedSample(int num) {
	int[] samples = sample(num);
	double[] results = new double[num];
	for(int i=0; i<num; i++){
	    results[i]=(double)samples[i];
	}
	return results;
    }
}
