package analyzer.distributions;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * This is a subclass of normal distribution class. The class performs a
 * truncated normal distribution based on user inputs
 * 
 * This class only takes mean, standard deviation, lower truncation and higher
 * truncations to initialize
 * 
 * @author weilix
 *
 */
public class TruncatedNormalDistribution extends NormalDistribution implements TruncatedDistribution{

    private double lower;
    private double higher;

    public TruncatedNormalDistribution(double m, double std, double l, double h) {
	super(m, std);
	lower = l;
	higher = h;
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
