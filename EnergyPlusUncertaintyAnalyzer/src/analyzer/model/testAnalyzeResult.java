package analyzer.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class testAnalyzeResult {

	public static void main(String[] args) {
		AnalyzeResult analyzeResult = new AnalyzeResult();
		String source = "/Users/Adrian/Dropbox/testIDFJfreeChart/";
		String filename = "One_Montgomery_Plaza1Meter.csv";
		String[] header = analyzeResult.getHeader(source, filename);
		System.out.println(Arrays.toString(header));
		HashMap<String, ArrayList<Double[]>> data = null;
		String idfName = "One_Montgomery_Plaza";
		data = analyzeResult.getData(source, idfName, 4);
		ArrayList<Double[]> list = data.get("DECEMBER");
		System.out.println(Arrays.toString(list.get(0)));
		System.out.println(Arrays.toString(list.get(1)));
		System.out.println(Arrays.toString(list.get(2)));
		System.out.println(Arrays.toString(list.get(3)));

		
	}
	


}
