package analyzer.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class AnalyzeResult {
	private final ArrayList<String> keys;
	private final HashMap<String, ArrayList<Double[]>> data;
	private String[] header;
	private final String source;
	private final String idfName;
	private int startYear;

	public AnalyzeResult(String source, String idfName) {
		this.keys = new ArrayList<String>();
		this.data = new HashMap<String, ArrayList<Double[]>>();
		this.header = null;
		this.source = source;
		this.idfName = idfName;
		this.startYear = Calendar.getInstance().get(Calendar.YEAR);


	}
	
	public void setStartYear(int idfYear){
		startYear = idfYear;
	}

	public String getKey(int index) {
		return keys.get(index);
	}
	
	public int getKeysLength() {
		return keys.size();
	}
	
	public int getStartYear() {
		return startYear;
	}

	public double[] getData(String key, int colNumber) {
		ArrayList<Double[]> list = this.data.get(key);
		int length = list.size();
		double[] output = new double[length];
		for (int i = 0; i < length; i++) {
			Double[] temp = list.get(i);
			output[i] = temp[colNumber];
		}
		return output;
	}
	
	public String getVariable(int index){
		return header[index];
	}
	
	public int getVariableLength(){
		return header.length;
	}

	public void setHeader() {
		FileReader file;
		String filename = this.source + this.idfName + "1Meter.csv";
		try {
			file = new FileReader(filename);
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
		header = Arrays.copyOfRange(header, 1, header.length);

		for (int i = 0; i < header.length; i++) {
			int spaceIdx = header[i].indexOf(" ");
			header[i] = header[i].substring(0, spaceIdx);
		}

	}



	public void setData(int numSim) {
		// HashMap<String, ArrayList<Double[]>> data = new
		// HashMap<String, ArrayList<Double[]>>();

		for (int i = 1; i < numSim + 1; i++) {
			try {
				FileReader file = new FileReader(this.source
						+ this.idfName + i
						+ "Meter.csv");
				BufferedReader br = new BufferedReader(file);
				String line = "";
				String csvSplitBy = ",";
				br.readLine();
				br.readLine();
				br.readLine(); // skip first 3 lines (header)
				while ((line = br.readLine()) != null) {
					String[] tempLine = line
							.split(csvSplitBy);
					Double[] tempData = convertStringToDouble(tempLine);
					String month = tempLine[0].trim()
							.toUpperCase();
					ArrayList<Double[]> list = data
							.get(month);
					if (list == null) {
						ArrayList<Double[]> newList = new ArrayList<Double[]>();
						newList.add(tempData);
						this.data.put(month, newList);

						this.keys.add(month);
					} else {
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

		// return data;
	}

	private Double[] convertStringToDouble(String[] args) {
		int len = args.length;
		Double[] output = new Double[len - 1];
		for (int i = 0; i < len - 1; i++) {
			// ignores 1st value of args which is the month string
			// output should be all numeric data
			output[i] = Double.parseDouble(args[i + 1]);
		}
		return output;
	}

}
