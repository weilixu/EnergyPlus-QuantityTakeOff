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
	 * generate random variables from binomial distribution
	 * 
	 * @param n
	 *                Number of trials
	 * @param p
	 *                probability of success
	 * @return array of random variables of size numRV from binomial
	 *         distribution
	 */
	public int[] binomialRnd(int n, double p) {
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
	public int[] poissonRnd(double lambda) {
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
	public int[] unifDiscreteRnd(int n, double p) {
		BinomialDistribution bd = new BinomialDistribution(n, p);
		int[] rv = bd.sample(numRV);
		return rv;
	}
	
	//example usage	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DiscreteRVGenerator r = new DiscreteRVGenerator(1000);
		int[] output = r.poissonRnd(1);
		System.out.println(Arrays.toString(output));

	}

}
