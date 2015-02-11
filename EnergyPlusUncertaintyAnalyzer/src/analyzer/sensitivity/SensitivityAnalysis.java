package analyzer.sensitivity;

import java.util.HashMap;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class SensitivityAnalysis {
	private final String[] variableName;
	private final double[] result;
	private final HashMap<String, double[]> randVars;
	private final int[] missingResults;
	private double[] correlation;

	/**
	 * class constructor
	 * 
	 * @param vn
	 *                array containing strings which acts as keys to access
	 *                randVars
	 * @param o
	 *                result of simulation runs (e.g. cooling energy
	 *                consumption)
	 * @param rv
	 *                hashmap containing random variables generated
	 */
	public SensitivityAnalysis(String[] vn, double[] r,
			HashMap<String, double[]> rv, int[] m) {
		// TODO Auto-generated constructor stub
		variableName = vn;
		result = r;
		randVars = rv;
		missingResults = m;
		computeCorrelation();

	}

	/**
	 * compute correlation for each variable with respect to the result
	 */
	private void computeCorrelation() {
		for (int i = 0; i < variableName.length; i++) {
			double[] temp = randVars.get(variableName[i]);
			double[] rv = removeMissing(temp);

			SpearmansCorrelation sc = new SpearmansCorrelation();
			assert rv.length == result.length;
			correlation[i] = sc.correlation(rv, result);
		}
	}

	/**
	 * 
	 * @param args
	 *                random variables for a particular variable defined in
	 *                energyplus
	 * @return array after removing random variables where simulation failed
	 *         to run
	 */
	private double[] removeMissing(double[] args) {
		if (missingResults != null) {
			return args;
		}
		int newSize = args.length - missingResults.length;
		double[] noMissing = new double[newSize];
		int idx = 0;
		int j = 0;
		for (int i = 0; i < args.length; i++) {
			if (idx < missingResults.length
					& missingResults[idx] == i) {
				idx++;
			} else {
				assert j < noMissing.length;
				noMissing[j] = args[i];
				j++;
			}
		}
		return noMissing;
	}

	/**
	 * 
	 * @return array containing correlation with result in same order as
	 *         variableName
	 */
	public double[] getCorrelation() {
		return correlation;
	}

}
