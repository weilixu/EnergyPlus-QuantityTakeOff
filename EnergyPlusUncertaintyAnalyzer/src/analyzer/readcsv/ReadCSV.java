package analyzer.readcsv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCSV {
    private double[] csvData;
    private boolean dataRetrieved;

    public ReadCSV() {
	dataRetrieved = false;
    }

    public void readData(String filename) {
	double[] data = null;
	try {
	    FileReader file = new FileReader(filename);
	    BufferedReader br = new BufferedReader(file);
	    ArrayList<String[]> list = new ArrayList<String[]>();
	    String line = "";
	    String csvSplitBy = ",";
	    int size = 0;
	    while ((line = br.readLine()) != null) {
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
	
	dataRetrieved = true;
	csvData = data;
    }
    
    public double[] getData(){
	if(dataRetrieved){
	    return csvData;
	}
	return null;
    }
   /*
    * get the minimum value from the imported data
    */
    public Double getMinimum(){
	double min = csvData[0];
	for(double d: csvData){
	    if (min>d){
		min = d;
		}
	    }
	return min;
    }
    
    /*
     * get the maximum value from the imported data
     */
    public Double getMaximum(){
	double max = csvData[0];
	for(double d: csvData){
	    if (max<d){
		max = d;
		}
	    }
	return max;
    }
    
    

    private double[] convertListToDouble(ArrayList<String[]> list, int length) {
	double[] output = new double[length];
	int idx = 0;
	for (int i = 0; i < list.size(); i++) {
	    String[] temp = list.get(i);
	    for (int j = 0; j < temp.length; j++) {
		output[idx] = Double.parseDouble(temp[j]);
		idx = idx + 1;
	    }
	}
	return output;
    }

}
