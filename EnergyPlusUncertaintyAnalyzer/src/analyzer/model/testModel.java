package analyzer.model;

import java.util.Arrays;
import java.util.Random;

public class testModel {

	public static void main(String[] args) {

		Model model = new Model();

		// example usage of fitData which calls fitDist
		Random randomNormal = new Random();
		int n = 1000;
		double[] data = new double[n];
		for (int i = 0; i < n; i++) {
			data[i] = randomNormal.nextGaussian();
		}
		String source = "/Users/Adrian/Dropbox";
		String imgName = "fitDistTestImg.jpg";
		Arrays.sort(data);
		double min = data[0];
		double max = data[n - 1];
		Object[] fitDistOutput = model.fitData(source, imgName, data,
				1000, "BIC", "CONTINUOUS", min, max);
		double[] rndVars = (double[]) fitDistOutput[0];
		System.out.println(Arrays.toString(rndVars));
		System.out.println(fitDistOutput[1]);

		// example usage of generateRV which calls makeDist
		double[] distrParam = new double[1];
		distrParam[0] = 1; // mean
		// distrParam[1] = 1; //standard dev
		double[] makeDistOutput = model.generateRV(source,
				"makeDistTestImg.jpg", 1000, "exponeNtial",
				distrParam, 0, 100);
		System.out.println(Arrays.toString(makeDistOutput));

	}
}
