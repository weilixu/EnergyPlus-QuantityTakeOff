package analyzer.readcsv;

import java.util.ArrayList;
import java.util.Arrays;

public class testReadCSV {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadCSV readCSV = new ReadCSV();

		String filename = "/Users/Adrian/Dropbox/testIDFJfreeChart/testCSV.csv";
		ArrayList<String> data = readCSV.readData(filename);
//		System.out.println(Arrays.toString(myData));
		int len = data.size();
		String[] output = new String[len];
		for (int i=0; i<len; i++){
			
			output[i] = data.get(i);
//			System.out.println(output[i]);
//			System.out.println(Double.parseDouble(data.get(i)));
		}
		System.out.print(output[5]);
//		for (int j=0; j<output.length; j++){
//			System.out.println(Double.parseDouble(output[j]));
//		}
		

	}

}
