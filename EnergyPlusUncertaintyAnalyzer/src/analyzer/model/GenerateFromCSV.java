package analyzer.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.mathworks.toolbox.javabuilder.*;

import csvfitdist.CsvFitDist;

public class GenerateFromCSV {
	// file directory related data
	private String directory;
	private String filename;
	private String fullFileName;
	// meta-data
	private String[] headerList;
	private ArrayList<String[]> listProcessed;
	private int numVars;
	// data
	private double[][] data;
	private String[] distSummaryArr;

	public GenerateFromCSV() {
		listProcessed = new ArrayList<String[]>();
		numVars = 0;
		// modifyData();
		// data = new double[numVars][numRV];
		// distSummaryArr = new String[numVars];
		// generateRV(sortby, dataType);

	}

	/**
	 * initialize the file directory
	 * 
	 * @param eplusFile
	 */
	public void setFileDirectory(File eplusFile) {
		directory = eplusFile.getParentFile().getAbsolutePath();
		filename = eplusFile.getName();
		fullFileName = eplusFile.getAbsolutePath();
	}

	/**
	 * 
	 * @return 2-d array containing random variables
	 */
	public double[][] getData() {
		return data;
	}

	/**
	 * 
	 * @return 1-d array containing each distribution description
	 */
	public String[] getDistSummary() {
		return distSummaryArr;
	}

	/**
	 * Get the list of header that processed by the multi-variable function
	 * 
	 * @return
	 */
	public String[] getHeaderList() {
		return headerList;
	}

	/**
	 * generate random variables by fitting each column of data to fitdist
	 * and write to csv titled "RV"+filename each column of random variable
	 * in csv file is in same order as original dataset provided
	 * 
	 * @param numRV
	 *                number of random variables to generate
	 * @param sortby
	 *                sorting criteria
	 * @param dataType
	 *                "CONTINUOUS" or "DISCRETE"
	 */
	public void generateRV(int simulationNumber, String sortby,
			String dataType) {
		// pre-process the data
		modifyData();
		CsvFitDist fitCsv = null;
		Object[] inputs = new Object[5];
		inputs[0] = directory;
		inputs[1] = filename;
		inputs[2] = simulationNumber; // number of random variables to
						// generate
		inputs[3] = sortby; // sortby
		inputs[4] = dataType; // dataType

		Object[] fitCSVResult = null;
		
		//initialize data 2-d array & distribution summary array
		data = new double[numVars][simulationNumber];
		distSummaryArr = new String[numVars];
		
		try {
			fitCsv = new CsvFitDist();
			fitCSVResult = fitCsv.csvfitdist(1, inputs);
			// System.out.println(fitCSVResult[0].getClass().getName());
			MWStructArray mwarr = (MWStructArray) fitCSVResult[0];

			for (int i = 0; i < numVars; i++) {
				// MWStructArray.getField is a 1-based offset
				// array
				distSummaryArr[i] = mwarr.getField("summary",
						i + 1).toString();
				MWNumericArray mwnumarr = (MWNumericArray) mwarr
						.getField("data", i + 1);
				data[i] = mwnumarr.getDoubleData();
				// System.out.println(distSummaryArr[i]);
				// System.out.println(Arrays.toString(data[i]));

			}

		} catch (MWException e) {
			e.printStackTrace();
		}
	}

	/**
	 * fills empty cells with NaN and overwrites csv file
	 */
	private void modifyData() {
		try {
			FileReader file = new FileReader(fullFileName);
			BufferedReader br = new BufferedReader(file);
			ArrayList<String[]> list = new ArrayList<String[]>();
			String line = "";
			String splitBy = ",";
			// get header
			headerList = br.readLine().split(splitBy);
			numVars = headerList.length;
			
			while ((line = br.readLine()) != null) {
				String[] tempLine = line.split(splitBy);
				// if (tempLine.length > numVars) {
				// numVars = tempLine.length;
				// }
				list.add(tempLine);
			}

			br.close();
			fillWithNaN(list);
			writeCSV();
			// System.out.println(Arrays.toString(headerList));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * fills empty cells with NaN
	 */
	private void fillWithNaN(ArrayList<String[]> list) {
		for (int i = 0; i < list.size(); i++) {
			String[] temp = list.get(i);
			String[] processed = new String[numVars];
			for (int j = 0; j < numVars; j++) {
				if (j < temp.length && !temp[j].isEmpty()) {
					processed[j] = temp[j];
				} else {
					processed[j] = "NaN";
				}
			}
			listProcessed.add(processed);
		}

	}

	private void writeCSV() {

		try {
			FileWriter w = new FileWriter(directory + filename,
					false);

			for (int i = 0; i < listProcessed.size(); i++) {
				String[] temp = listProcessed.get(i);
				for (int j = 0; j < temp.length; j++) {
					w.append(temp[j]);
					if (j < (temp.length - 1)) {
						w.append(",");
					}
				}
				if (i < (listProcessed.size() - 1)) {
					w.append("\n");
				}
			}
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// example usage
	// public static void main(String[] args) {
	// String myDirectory = "C:\\Users\\Weili\\Desktop\\New folder\\";
	// String myFilename = "testcsvfitdist.csv";
	// GenerateFromCSV p = new GenerateFromCSV(myDirectory,
	// myFilename, 1000, "BIC", "CONTINUOUS");
	// double[][] myData = p.getData();
	// String[] distSummaryList = p.getDistSummary();
	// for (int i=0; i<myData.length; i++) {
	// double[] currData = myData[i];
	// String distSummary = distSummaryList[i];
	// System.out.println(Arrays.toString(currData));
	// System.out.println(distSummary);
	// }
	//
	//
	// }
}
