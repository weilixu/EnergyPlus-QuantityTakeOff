package analyzer.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.mathworks.toolbox.javabuilder.MWException;

import csvfitdist.CsvFitDist;

public class GenerateFromCSV {
	private final String directory;
	private final String filename;
	private final String fullFileName;
	private ArrayList<String[]> listProcessed;
	private int numVars;

	public GenerateFromCSV(String d, String f) {
		// TODO Auto-generated constructor stub
		directory = d;
		filename = f;
		fullFileName = directory + filename;
		listProcessed = new ArrayList<String[]>();
		numVars = 0;

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
	public void generateRV(int numRV, String sortby, String dataType) {
		CsvFitDist fitCsv = null;
		Object[] inputs = new Object[5];
		inputs[0] = directory;
		inputs[1] = filename;
		inputs[2] = numRV; // number of random variables to generate
		inputs[3] = sortby; // sortby
		inputs[4] = dataType; // dataType

		try {
			fitCsv = new CsvFitDist();
			fitCsv.csvfitdist(inputs);

		} catch (MWException e) {
			e.printStackTrace();
		}
	}

	/**
	 * fills empty cells with NaN and overwrites csv file
	 */
	public void modifyData() {
		try {
			FileReader file = new FileReader(fullFileName);
			BufferedReader br = new BufferedReader(file);
			ArrayList<String[]> list = new ArrayList<String[]>();
			String line = "";
			String splitBy = ",";
			while ((line = br.readLine()) != null) {
				String[] tempLine = line.split(splitBy);
				if (tempLine.length > numVars) {
					numVars = tempLine.length;
				}
				list.add(tempLine);
			}
			br.close();
			fillWithNaN(list);
			writeCSV();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// example usage
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String myDirectory = "/Users/Adrian/Dropbox/IBPSA 2015/Practical approach uncertainty analysis/matlab_code/editedForJava/";
		String myFilename = "testcsvfitdist.csv";
		GenerateFromCSV p = new GenerateFromCSV(myDirectory, myFilename);
		p.modifyData();
		p.generateRV(1000, "BIC", "CONTINUOUS");

	}
}
