package analyzer.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class AnalyzeResult {

	public String[] getHeader(String source, String filename) {
		FileReader file;
		String[] header = null;
		try {
			file = new FileReader(source + filename);
			BufferedReader br = new BufferedReader(file);
			header = br.readLine().split(",");
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return header;
	}

	public HashMap<String, ArrayList<Double[]>> getData(String source, String idfName, int numSim) {
		HashMap<String, ArrayList<Double[]>> data = new HashMap<String, ArrayList<Double[]>>();
		for (int i = 1; i < numSim + 1; i++) {
			try {
				FileReader file = new FileReader(source
						+ idfName + i + "Meter.csv");
				BufferedReader br = new BufferedReader(file);
				String line = "";
				String csvSplitBy = ",";
				br.readLine();br.readLine(); br.readLine(); //skip first 3 lines (header)
				while ((line = br.readLine()) != null) {
					String[] tempLine = line.split(csvSplitBy);
					Double[] tempData = convertStringToDouble(tempLine);
					String month = tempLine[0].trim().toUpperCase();
					ArrayList<Double[]> list = data.get(month);
					if (list == null){
						ArrayList<Double[]> newList = new ArrayList<Double[]>();
						newList.add(tempData);
						data.put(month, newList);
					}else{
						list.add(tempData);
					}
					
				}
				br.close();					
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}

	private Double[] convertStringToDouble(String[] args){
		int len = args.length;
		Double[] output = new Double[len-1];
		for (int i=0; i<len-1; i++){
			// ignores 1st value of args which is the month string
			// output should be all numeric data
			output[i] = Double.parseDouble(args[i+1]);
		}
		return output;
	}
	


}
