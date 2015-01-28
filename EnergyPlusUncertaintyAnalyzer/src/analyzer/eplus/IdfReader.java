package analyzer.eplus;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import analyzer.listeners.loadIDFListener;

/**
 * Reads IDF file and store it in a map TreeMap<String, ArrayList<ValueNode>>
 * 
 * @author Weili
 *
 */
public class IdfReader {

    private static final String endToken = ";";
    private static final String descriptionToken = "!";

    private HashMap<String, ArrayList<ValueNode>> eplusMap;
    private ArrayList<String> variableList;
    private String path;

    private boolean output = false;

    private List<loadIDFListener> loadIDFListeners;

    public IdfReader(String filePath) {
	this.path = filePath;
	eplusMap = new HashMap<String, ArrayList<ValueNode>>();
	variableList = new ArrayList<String>();
	loadIDFListeners = new ArrayList<loadIDFListener>();
    }
    
    public void addLoadIDFListeners(loadIDFListener l){
	loadIDFListeners.add(l);
    }

    // the file must be stored by overwrite the !-Option(2nd line in the idf
    // file) to sort the order

    public void readEplusFile() throws IOException {
	FileInputStream inputStream = null;
	Scanner sc = null;
	String startToken = null;

	try {
	    inputStream = new FileInputStream(path);
	    sc = new Scanner(inputStream, "UTF-8");
	    while (sc.hasNextLine()) {
		String line = sc.nextLine();
		if (line.length() <= 2 || line.startsWith("!")) {
		    continue;
		}
		String firstChar = line.substring(0, 1);

		// look for the start token first when the flag is false
		if (!output && !firstChar.equals(" ")
			&& !firstChar.equals(descriptionToken)) {
		    // find the start token
		    output = true;
		    startToken = line.trim().replace(",", "");
		    // if the start token is not in the hash map, put it in
		    if (!eplusMap.containsKey(startToken)) {
			eplusMap.put(startToken, new ArrayList<ValueNode>());
		    }
		} else if (output) {
		    // if output is true, it means the searching is still valid.
		    // for the normal cases. put the string in the last nested
		    // map
		    String[] temp = line.split(descriptionToken);

		    // this temp has only one/two elements (for sure)
		    String description = "";
		    String element = "";
		    if (temp.length > 1) {
			element = temp[0].trim();
			description = temp[1].trim();
		    }
		    // add the special character
		    if (element.indexOf("$") > -1) {
			variableList.add(element);
		    }
		    // put element into the map
		    eplusMap.get(startToken).add(
			    new ValueNode(element, description));

		    if (line.indexOf(endToken) > -1) {
			// find the end lineof the statement, swithc the flag!
			// switch flag
			output = false;
		    }
		}
		if (sc.ioException() != null) {
		    throw sc.ioException();
		}
	    }
	} finally {
	    if (inputStream != null) {
		inputStream.close();
	    }
	    if (sc != null) {
		sc.close();
	    }
	}
	onReadEplusFile();
    }

    /*
     * 
     */
    public HashMap<String, ArrayList<ValueNode>> getEplusMap() {
	if (eplusMap != null) {
	    return eplusMap;
	}
	return null;
    }

    private void onReadEplusFile() {
	for (loadIDFListener l : loadIDFListeners) {
	    l.loadedEnergyPlusFile(variableList);
	}
    }
}
