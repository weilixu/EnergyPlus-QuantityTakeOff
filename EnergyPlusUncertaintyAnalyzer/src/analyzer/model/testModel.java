package analyzer.model;

import java.util.Arrays;
import java.util.Random;

import allfitdist.FitDist;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class testModel {

	public static void main(String[] args) {

		Model model = new Model();
		model.setSource("/Users/Adrian/Dropbox/testIDFJfreeChart");
		model.setVariable("$dsfdsf");
		model.setSimulationNumber(100);
		// example usage of fitData which calls fitDist
//		Random randomNormal = new Random();
//		int n = 1000;
//		double[] data = new double[n];
//		for (int i = 0; i < n; i++) {
//			data[i] = randomNormal.nextGaussian();
//		}
//
//		Arrays.sort(data);
//
//		Object[] fitDistOutput = model.fitData(data,
//				"BIC", "CONTINUOUS", "-3", "3");
//		double[] rndVars = (double[]) fitDistOutput[0];
//		System.out.println(Arrays.toString(rndVars));
//		System.out.println(fitDistOutput[1]);

		// example usage of generateRV to generate 1 parameter Distribution
		double[] distrParam = new double[2];
		distrParam[0] = 0; // mean
		distrParam[1] = 1;

		//double[] makeDistOutput = model.generateRV("NORMAL",
		//		distrParam, "-3", "3");
		//System.out.println("RV for 1 parameter dist:");
		//System.out.println(Arrays.toString(makeDistOutput));
		
//		// example usage of generateRV to generate 2 parameter Distribution
//		Model model2 = new Model();
//		double[] distrParam2 = new double[2]; // set array size to 2
//		distrParam2[0] = 0; // 'x'
//		distrParam2[1] = 100; // cdf values
//		double[] makeDistOutput2 = model.generateRV(source,
//				"makeDistTestImg2Param.jpg", 1000, "Uniform",
//				distrParam2, 0, 90);
//		System.out.println("RV for 2 parameter dist:");
//		System.out.println(Arrays.toString(makeDistOutput2));
		
//		// example usage of generateRV to generate 3 parameter Distribution
//		Model model3 = new Model();
//		double[] distrParam3 = new double[3]; // set array size to 3
//		distrParam3[0] = 1; //lower limit
//		distrParam3[1] = 7; // peak
//		distrParam3[2] = 10; // upper limit
//		double[] makeDistOutput3 = model3.generateRV(source,
//				"makeDistTestImg3Param.jpg", 1000, "Triangular",
//				distrParam3, 0, 100);
//		System.out.println("RV for 3 parameter dist:");
//		System.out.println(Arrays.toString(makeDistOutput3));


	}
}
