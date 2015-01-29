package analyzer.readcsv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class ReadCSV {
	public ArrayList<String> readData(String filename){
		File file = new File(filename);
		ArrayList<String> dataString = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(file);			
			scanner.useDelimiter(",|\n");

			while (scanner.hasNext()){
//				scanner.useDelimiter(",|\n");
				String tempData = scanner.next().trim();
				boolean flag = (tempData.isEmpty() | tempData.equalsIgnoreCase("NaN")); 
				if (!flag){
					dataString.add(tempData);
				}	
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		double[] data = convertListToDouble(dataString);
		return dataString;
		
	}
	
	private double[] convertListToDouble(ArrayList<String> data){
		int len = data.size();
		double[] output = new double[len];
		for (int i=0; i<len; i++){
			output[i] = Double.parseDouble(data.get(i));
		}
		return null;
	}

}
