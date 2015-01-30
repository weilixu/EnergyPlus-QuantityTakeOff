package analyzer.readcsv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class ReadCSV {
    
    
	public double[] readData(String filename){
		double[] data = null;
		try {
			FileReader file = new FileReader(filename);
			BufferedReader br = new BufferedReader(file);
			ArrayList<String[]> list = new ArrayList<String[]>();
			String line = "";
			String csvSplitBy = ",";
			int size = 0;
			while((line = br.readLine()) != null) {
				String[] tempLine = line.split(csvSplitBy);
				size = size + tempLine.length;
				list.add(tempLine);
			}
			br.close();
			data = convertListToDouble(list, size);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;	
	}
	
	private double[] convertListToDouble(ArrayList<String[]> list, int length){
		double[] output = new double[length];
		int idx = 0;
		for (int i=0; i<list.size(); i++){
			String[] temp = list.get(i);
			for (int j=0; j<temp.length; j++){
				output[idx] = Double.parseDouble(temp[j]);
				idx = idx + 1;
			}
		}
		return output;
	}
	


}
