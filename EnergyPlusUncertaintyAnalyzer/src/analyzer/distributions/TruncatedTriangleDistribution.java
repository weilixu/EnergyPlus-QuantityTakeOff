package analyzer.distributions;

import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class TruncatedTriangleDistribution extends TriangularDistribution implements TruncatedDistribution{
    
    private double lower;
    private double upper;
    private double mode;

    public TruncatedTriangleDistribution(double a, double c, double b)
	    throws NumberIsTooLargeException, NumberIsTooSmallException {
	super(a, c, b);
	lower = a;
	upper = c;
	mode = b;
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
