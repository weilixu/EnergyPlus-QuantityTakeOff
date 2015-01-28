package analyzer.model;

import allfitdist.FitDist;
import allmakedist.MakeDist;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class Model {

	/**
	 * 
	 * @param source
	 *                Directory to save image to
	 * @param imgName
	 *                Name of file to save image to (e.g. "testImage.jpg")
	 * @param data
	 *                data to fit distribution to
	 * @param numRV
	 *                number of random variables to generate
	 * @param sortby
	 *                sortby is the likelihood function used to sort the
	 *                fitted probability distribtuions. Valid inputs are:
	 *                NLogL - Negative Log Likelihood BIC - Bayesian
	 *                Information criterion AIC - Akaike information
	 *                criterion AICc
	 * @param dataType
	 *                dataType specifies whether to fit a 'DISCRETE' or
	 *                'CONTINUOUS' distribution
	 * @param lower
	 *                lower bound where the generated random variables will
	 *                be truncated tor
	 * @param upper
	 *                upper bound where the generated random variables will
	 *                be truncated tor
	 * @return
	 */
	public Object[] fitData(String source, String imgName, double[] data,
			int numRV, String sortby, String dataType,
			double lower, double upper) {
		FitDist fitDistr = null;
		Object[] fitDistInputs = new Object[8];
		fitDistInputs[0] = source;
		fitDistInputs[1] = imgName;
		fitDistInputs[2] = data;
		fitDistInputs[3] = numRV;
		fitDistInputs[4] = sortby;
		fitDistInputs[5] = dataType;
		fitDistInputs[6] = lower;
		fitDistInputs[7] = upper;
		Object[] fitDistResult = null;
		try {
			fitDistr = new FitDist();
			fitDistResult = fitDistr.allfitdist(2, fitDistInputs);
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MWNumericArray rndVars = (MWNumericArray) fitDistResult[0];
		Object[] output = new Object[2];
		// .getDoubleData returns double[] containing random variables
		output[0] = rndVars.getDoubleData();
		output[1] = fitDistResult[1];
		return output;
	}

	/**
	 * 
	 * @param source
	 *                Directory to save image to
	 * @param imgName
	 *                Name of file to save image to (e.g. "testImage.jpg")
	 * @param numRV
	 *                number of random variables to generate
	 * @param distrName
	 *                name of distribution from which random variables would
	 *                be generated from
	 * @param distrParam
	 *                1 X k vector of parameters for the distribution
	 *                specified where k is the number of parameters
	 * @param lower
	 *                lower bound where the generated random variables will
	 *                be truncated tor
	 * @param upper
	 *                upper bound where the generated random variables will
	 *                be truncated tor
	 * **list of valid distribution and their parameters
	 * Distributions with 1 Parameter
	 *  "Exponential": 'mu' - Mean parameter 
	 *  "Poisson": 'lambda' - Mean
	 *  "Reyleigh": 'b' - Defining parameter
	 * Distributions with 2 Parameters
	 *  "Beta": 'a' - First shape parameter; 'b' - Second shape parameter
	 *  "Binomial": 'N' - Number of trials; 'p' - Probability of success
	 *  "BirnbaumSaunders": 'beta' - Scale parameter; 'gamma' - Shape parameter
	 *  "ExtremeValue": 'mu' - Location parameter; 'sigma' - Scale parameter
	 *  "Gamma": 'a' - Shape parameter; 'b' - Scale parameter
	 *  "InverseGaussian": 'mu' - Scale parameter; 'lambda' - Shape parameter
	 *  "Logistic": 'mu' - Mean; 'lambda' - Shape parameter
	 *  "Loglogistic": 'mu' - Mean; 'sigma' - Scale parameter
	 *  "Lognormal": 'mu' - Log mean; 'sigma' - Log standard deviation
	 *  "Nakagami": 'mu' - Shape parameter 'omega' - Scale parameter
	 *  "NegativeBinomial": 'R' - Number of successes; 'p' - probability of success
	 *  "Normal": 'mu' - Mean; 'sigma' - Standard deviation
	 *  "Rician": 's' - Noncentrality parameter; 'sigma' - Scale parameter
	 *  "Uniform": 'lower' - Lower parameter; 'upper' - Upper parameter
	 *  "Weibull": 'a' - Scale parameter; 'b' - Shape parameter
	 *Distributions with 3 Parameters   
	 *  "Burr": 'alpha' - Scale parameter; 'c' - First shape parameter; 'k' - Second shape parameter
	 *  "GeneralizedExtremeValue": 'k' - Shape parameter; 'sigma' - Scale parameter; 'mu' - Location parameter
	 *  "GeneralizedPareto": 'k' - Shape parameter; 'sigma' - Scale parameter; 'theta' - Location parameter
	 *  "tLocationScale": 'mu' - Location parameter; 'sigma' - Scale parameter; 'nu' - Degrees of freedom
	 *  "Triangular": 'a' - Lower limit; 'b' - Peak location; 'c' - Upper limit
	 *  
	 */
	public double[] generateRV(String source, String imgName, int numRV,
			String distrName, double[] distrParam, double lower,
			double upper) {
		MakeDist makeDistr = null;
		Object[] makeDistInputs = new Object[7];
		makeDistInputs[0] = source;
		makeDistInputs[1] = imgName;
		makeDistInputs[2] = numRV;
		makeDistInputs[3] = distrName;
		makeDistInputs[4] = distrParam;
		makeDistInputs[5] = lower; // min
		makeDistInputs[6] = upper; // max
		Object[] makeDistResult = null;
		try {
			makeDistr = new MakeDist();
			makeDistResult = makeDistr.allmakedist(1,
					makeDistInputs);
		} catch (MWException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MWNumericArray rndVars = (MWNumericArray) makeDistResult[0];
		return rndVars.getDoubleData();

	}

}
