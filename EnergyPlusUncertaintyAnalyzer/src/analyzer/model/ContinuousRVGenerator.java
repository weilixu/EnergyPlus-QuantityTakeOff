package analyzer.model;

import java.util.Arrays;

import org.apache.commons.math3.distribution.*;

public class ContinuousRVGenerator {
	final private int numRV; // number of random variables to generate

	/**
	 * constructor to set numRV
	 * 
	 * @param n
	 *                number of random variables to genrate
	 */
	public ContinuousRVGenerator(int n) {
		numRV = n;
	}

	/**
	 * generate random variables based on name of distribution. Does not
	 * cover exponential distribution. To generate from exponential use
	 * expRnd below
	 * 
	 * @param distName
	 *                Name of distribution
	 * @param param1
	 *                first parameter of distribution
	 * @param param2
	 *                second parameter of distribution
	 * @return array of random variables generated from distName
	 */
	public double[] getSamples(String distName, double param1, double param2) {
		double[] rv = null;
		switch (distName.toUpperCase()) {
		case "BETA":
			rv = betaRnd(param1, param2);
			break;
		case "GAMMA":
			rv = gammaRnd(param1, param2);
			break;
		case "LOGISTIC":
			rv = logisticRnd(param1, param2);
			break;
		case "LOGNORMAL":
			rv = lognormRnd(param1, param2);
			break;
		case "NAKAGAMI":
			rv = nakagamiRnd(param1, param2);
			break;
		case "NORMAL":
			rv = normRnd(param1, param2);
			break;
		case "UNIFORM":
			rv = unifContinuousRnd(param1, param2);
			break;
		case "WEIBULL":
			rv = weibullRnd(param1, param2);
			break;
		default:
			System.out.println("Distribution not found!");
		}
		return rv;
	}

	/**
	 * generate random variables from beta distribution
	 * 
	 * @param alpha
	 *                First shape parameter
	 * @param beta
	 *                second shape parameter
	 * @return array of random variables of size numRV from beta
	 *         distribution
	 */
	private double[] betaRnd(double alpha, double beta) {
		BetaDistribution bd = new BetaDistribution(alpha, beta);
		double[] rv = bd.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from exponential distribution
	 * 
	 * @param mu
	 *                mean
	 * 
	 * @return array of random variables of size numRV from exponential
	 *         distribution
	 */
	public double[] expRnd(double mu) {
		ExponentialDistribution ed = new ExponentialDistribution(mu);
		double[] rv = ed.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from gamma distribution
	 * 
	 * @param alpha
	 *                shape parameter
	 * @param beta
	 *                scale parameter
	 * @return array of random variables of size numRV from gamma
	 *         distribution
	 */
	private double[] gammaRnd(double alpha, double beta) {
		GammaDistribution gd = new GammaDistribution(alpha, beta);
		double[] rv = gd.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from logistic distribution
	 * 
	 * @param mu
	 *                mean
	 * @param sigma
	 *                scale parameter
	 * @return array of random variables of size numRV from logistic
	 *         distribution
	 */
	private double[] logisticRnd(double mu, double sigma) {
		LogisticDistribution ld = new LogisticDistribution(mu, sigma);
		double[] rv = ld.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from lognormal distribution
	 * 
	 * @param mu
	 *                log mean
	 * @param sigma
	 *                log standard deviation
	 * @return array of random variables of size numRV from lognormal
	 *         distribution
	 */
	private double[] lognormRnd(double mu, double sigma) {
		LogNormalDistribution lnd = new LogNormalDistribution(mu, sigma);
		double[] rv = lnd.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from nakagami distribution
	 * 
	 * @param mu
	 *                shape parameter
	 * @param omega
	 *                scale parameter
	 * @return array of random variables of size numRV from nakagami
	 *         distribution
	 */
	private double[] nakagamiRnd(double mu, double omega) {
		NakagamiDistribution nkd = new NakagamiDistribution(mu, omega);
		double[] rv = nkd.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from normal distribution
	 * 
	 * @param mu
	 *                mean
	 * @param sd
	 *                standard deviation
	 * @return array of random variables of size numRV from normal
	 *         distribution
	 */
	private double[] normRnd(double mu, double sd) {
		NormalDistribution nd = new NormalDistribution(mu, sd);
		double[] rv = nd.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from uniform distribution
	 * 
	 * @param lower
	 *                lower bound
	 * @param upper
	 *                upper bound
	 * @return array of random variables of size numRV from pareto
	 *         distribution
	 */
	private double[] unifContinuousRnd(double lower, double upper) {
		UniformRealDistribution ud = new UniformRealDistribution(lower,
				upper);
		double[] rv = ud.sample(numRV);
		return rv;
	}

	/**
	 * generate random variables from weibull distribution
	 * 
	 * @param lower
	 *                lower bound
	 * @param upper
	 *                upper bound
	 * @return array of random variables of size numRV from weibull
	 *         distribution
	 */
	private double[] weibullRnd(double alpha, double beta) {
		WeibullDistribution wd = new WeibullDistribution(alpha, beta);
		double[] rv = wd.sample(numRV);
		return rv;
	}
	
	// example usage
	public static void main(String[] args) {
		ContinuousRVGenerator r = new ContinuousRVGenerator(1000);
		double[] output = r.getSamples("NaKaGami", 1, 1);
		System.out.println(Arrays.toString(output));
	}

}
