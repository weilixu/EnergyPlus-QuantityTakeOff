package analyzer.model;

import java.util.Arrays;

import org.apache.commons.math3.distribution.*;

public class DiscreteRVGenerator {
	final private int numRV; // number of random variables to generate

	/**
	 * constructor to set numRV
	 * 
	 * @param n
	 *                number of random variables to genrate
	 */
	public DiscreteRVGenerator(int n) {
		// TODO Auto-generated constructor stub
		numRV = n;
	}
	
	/**
	 * generate random variables based on name of distribution. Does not
	 * cover exponential distribution. To generate from exponential use
	 * expRnd below
	 * 
	 * @param distName
	 *                Name of distribution
	 * @param distParam
	 *                array containing parameters to distName
	 * @return array of random variables generated from distName
	 */
	public int[] getSamples(String distName, double[] distParam) {
		int[] rv = null;
		switch (distName.toUpperCase()) {
		case "BINOMIAL":
			rv = binomialRnd((int) distParam[0], distParam[1]);
			break;
		case "POISSON":
			rv = poissonRnd(distParam[0]);
			break;
		case "UNIFORM":
			rv = unifDiscreteRnd((int) distParam[0], (int) distParam[1]);
			break;
		default:
			System.out.println("Distribution not found!");
		}
		return rv;
	}

	/**
	 * generate random variables from binomial distribution
	 * 
	 * @param n
	 *                Number of trials
	 * @param p
	 *                probability of success
	 * @return array of random variables of size numRV from binomial
	 *         distribution
	 */
	private int[] binomialRnd(int n, double p) {
		BinomialDistribution bd = new BinomialDistribution(n, p);
		int[] rv = bd.sample(numRV);
		return rv;
	}
	
	/**
	 * generate random variables from poisson distribution
	 * 
	 * @param lambda
	 *                mean
	 * @return array of random variables of size numRV from poisson
	 *         distribution
	 */
	private int[] poissonRnd(double lambda) {
		PoissonDistribution pd = new PoissonDistribution(lambda);
		int[] rv = pd.sample(numRV);
		return rv;
	}	

	
	/**
	 * generate random variables from binomial distribution
	 * 
	 * @param n
	 *                Number of trials
	 * @param p
	 *                probability of success
	 * @return array of random variables of size numRV from binomial
	 *         distribution
	 */
	private int[] unifDiscreteRnd(int n, double p) {
		BinomialDistribution bd = new BinomialDistribution(n, p);
		int[] rv = bd.sample(numRV);
		return rv;
	}
	
	//example usage	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DiscreteRVGenerator r = new DiscreteRVGenerator(1000);
		double[] param = {1};
		int[] output = r.getSamples("POisson", param);
		System.out.println(Arrays.toString(output));

	}

}
