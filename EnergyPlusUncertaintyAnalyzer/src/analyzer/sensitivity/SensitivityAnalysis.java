package analyzer.sensitivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class SensitivityAnalysis {
	// list of names that were used to define random variables
	private final String[] variableName;
	// result from energyplus meter.csv files. Each double[i] refers to
	// consumption for simulation i
	private final double[] result;
	// random variables generated
	private final HashMap<String, double[]> randVars;
	// array containing indexes where no meter.csv files were found
	private final ArrayList<Integer> missingResults;
	// array containing all correlation
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
			HashMap<String, double[]> rv, ArrayList<Integer> m) {
		// TODO Auto-generated constructor stub
		variableName = vn;
		correlation = new double[variableName.length];
		result = r;
		randVars = rv;
		missingResults = m;
		computeCorrelation();
		sortCorrelation();
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
			
			for(int j=0; j<result.length; j++){
			    System.out.println(result[j]+" "+rv[j]);
			}
			
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
		int newSize = args.length - missingResults.size();
		double[] noMissing = new double[newSize];
		int idx = 0;
		int j = 0;
		for (int i = 0; i < args.length; i++) {
			if (idx < missingResults.size()
					& missingResults.get(idx) == i) {
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
	 * sort correlation using insertion sort so that variables with highest
	 * correlation is at front of array
	 */
	private void sortCorrelation() {
		assert correlation.length == variableName.length;
		for (int i = 0; i < correlation.length; i++) {
			double tempCorrelation = correlation[i];
			String tempVariable = variableName[i];
			int j = i;
			while (j > 0 && correlation[j - 1] < tempCorrelation) {
				correlation[j] = correlation[j - 1];
				variableName[j] = variableName[j - 1];
				j = j - 1;
			}
			correlation[j] = tempCorrelation;
			variableName[j] = tempVariable;
		}
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
