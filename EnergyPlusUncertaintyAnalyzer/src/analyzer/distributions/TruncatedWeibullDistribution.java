package analyzer.distributions;

import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

/**
 * This class performs truncated weibull distribution this class only takes in
 * alpha, beta and lower truncation, higher truncation for initialization
 * 
 * @author weilix
 *
 */
public class TruncatedWeibullDistribution extends WeibullDistribution implements TruncatedDistribution{

    private double lower;
    private double higher;

    public TruncatedWeibullDistribution(double alpha, double beta,
	    double lower, double higher) throws NotStrictlyPositiveException {
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
