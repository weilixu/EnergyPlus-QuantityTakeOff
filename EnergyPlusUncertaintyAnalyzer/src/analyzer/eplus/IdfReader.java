package analyzer.eplus;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import analyzer.listeners.loadIDFListener;

/**
 * This object represents an data structure of a typical sorted idf file. The
 * file has to read a valid and sorted idf file first. And it can remove an
 * object, add an object, and edit an object (editing should be specified by
 * client)
 * 
 * 
 * Reads IDF file and store it in a map HashMap<String,
 * HashMap<String,ArrayList<ValueNode>>> The data structure is specifically
 * designed for EnergyPlus key-value pair. In the first HashMap, the String
 * should represents the object name in the EnergyPlus idf file. e.g. Material
 * or BuildingSurface:Detailed etc.
 * 
 * The key maps to another map, this map's key is an element count. This is the
 * number of the same object. e.g. There will be more than 1
 * BuildingSurface:Detailed objects in one single idf file, each time, when the
 * searching algorithm finds the same object, it will increase the count and put
 * it as the second map's key
 * 
 * ValueNode recorde the key-value pair in the idf file. Each line of idf file
 * is one key-value pair. The key in a line should be the text after ! symbol,
 * which represents the information of the data. Value should be the data, which
 * shows at the begining of each line. e.g. 5.456, !- Thermal Resistance
 * {m2-K/W} "5.456" is the value and "Thermal Resistance {m2-K/W}" is the key
 * 
 * The data structure allows the delete an object, or duplicate an object or
 * edit an object.
 * 
 * @author Weili
 *
 */
public class IdfReader {

    // reading specification
    private static final String endToken = ";";
    private static final String descriptionToken = "!";

    // data structures
    private HashMap<String, HashMap<String, ArrayList<ValueNode>>> eplusMap;
    private ArrayList<String> variableList;
    // this Arraylist records the key pairs to access each variable. Trade
    // memory for speed
    private ArrayList<String[]> variableKeySets;
    private String path;

    // boolean indicates the flag
    private boolean output = false;
    private boolean dataFilled = false;

    // GUI listener for this module
    private List<loadIDFListener> loadIDFListeners;

    public IdfReader(String filePath) {
	this.path = filePath;
	eplusMap = new HashMap<String, HashMap<String, ArrayList<ValueNode>>>();
	variableList = new ArrayList<String>();
	variableKeySets = new ArrayList<String[]>();
	loadIDFListeners = new ArrayList<loadIDFListener>();
    }

    public void addLoadIDFListeners(loadIDFListener l) {
	loadIDFListeners.add(l);
    }

    /**
     * read EplusFile and create the database. read the data from idf file and
     * fill the HashMap. This method will switch the flag <dataFilled> to true
     * so that it can offer remove, add or edit functions to client
     * 
     * This method will also notify GUI for the possible uncertain variables
     * found in the idf file
     * 
     * @throws IOException
     */
    // the file must be stored by overwrite the !-Option(2nd line in the idf
    // file) to sort the order

    public void readEplusFile() throws IOException {
	// reset the data structures
	eplusMap = new HashMap<String, HashMap<String, ArrayList<ValueNode>>>();
	variableList = new ArrayList<String>();
	variableKeySets = new ArrayList<String[]>();
	// set the necessary elements
	FileInputStream inputStream = null;
	Scanner sc = null;
	String startToken = null;
	String elementCount = null;

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
			eplusMap.put(startToken,
				new HashMap<String, ArrayList<ValueNode>>());
		    }

		    // set the second map's key
		    Integer mapSize = eplusMap.get(startToken).size();
		    elementCount = mapSize.toString();
		    eplusMap.get(startToken).put(elementCount,
			    new ArrayList<ValueNode>());

		} else if (output) {
		    // if output is true, it means the searching is still valid.
		    // for the normal cases. put the string in the last nested
		    // map
		    String[] temp = line.split(descriptionToken);

		    // this temp has only one/two elements (for sure)
		    // description stands for the input information
		    // element stands for the data
		    String description = "";
		    String element = "";
		    if (temp.length > 1) {
			element = temp[0].trim();
			description = temp[1].trim();
		    }

		    // add the special character to the variableList
		    if (element.indexOf("$") > -1) {
			variableList.add(element.substring(0,
				element.length() - 1));
			String[] keyPair = { startToken, elementCount };
			variableKeySets.add(keyPair);
		    }

		    // put element into the map
		    eplusMap.get(startToken).get(elementCount)
			    .add(new ValueNode(element, description));

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
	// indicates the data structure is created and filled with data
	dataFilled = true;
	onReadEplusFile();
    }

    /**
     * @require objectName is exist in the data base remove the whole objects in
     *          the energyPlus. Careful on using this method, it might creates
     *          severe errors in the simulation
     * 
     * @param objectName
     */
    public void removeEnergyPlusObject(String objectName) {
	eplusMap.remove(objectName);
    }

    /**
     * @require objectName and elementCount are exist in the data base Remove
     *          one object in the data structure. The objectName shows the name
     *          of the object, element count shows the number of the object in
     *          the database
     * 
     * @param objectName
     * @param elementCount
     */
    public void removeEnergyPlusObject(String objectName, String elementCount) {
	eplusMap.get(objectName).remove(elementCount);
    }

    /**
     * @require specialCharactor is exist in the data structure, value is valid
     *          for the simulation
     * @ensure replace the special character with the value
     * 
     * @param specialCharactor
     * @param value
     */
    public void modifySpecialCharactor(String specialCharactor, String value) {
	int index = variableList.indexOf(specialCharactor);
	ArrayList<ValueNode> temp = eplusMap.get(variableKeySets.get(index)[0])
		.get(variableKeySets.get(index)[1]);
	for (ValueNode v : temp) {
	    if (v.isCritical() && v.getAttribute().equals(specialCharactor)) {
		v.setAttribute(value);
	    }
	}
    }
    
    /**
     * insert an new object without specify the values and descriptions. This algorithm will
     * copy the existing same object data and create the new element.
     * @param objectName
     */
    public void addNewEnergyPlusObject(String objectName) {
	if (eplusMap.containsKey(objectName)) {
	    // copy the first element
	    String elementCount = "0";
	    ArrayList<ValueNode> temp = eplusMap.get(objectName).get(
		    elementCount);

	    String[] objectValues = new String[temp.size()];
	    String[] objectDes = new String[temp.size()];
	    for (int i = 0; i < temp.size(); i++) {
		objectValues[i] = temp.get(i).getAttribute();
		objectDes[i] = temp.get(i).getDescription();
	    }
	    addNewEnergyPlusObject(objectName, objectValues, objectDes);
	}else{
	    //throw exception or warnings something, determine later
	}
    }

    /**
     * insert an new object into the database with known object name, object
     * values and object descriptions
     * 
     * @param objectName the name of the object
     * @param objectValues the object values
     * @param objectDes the object description (also means the key to the value node)
     */
    public void addNewEnergyPlusObject(String objectName,
	    String[] objectValues, String[] objectDes) {
	// create value node array
	ArrayList<ValueNode> newObject = new ArrayList<ValueNode>();
	for (int i = 0; i < objectValues.length; i++) {
	    newObject.add(new ValueNode(objectValues[i], objectDes[i]));
	}
	//create a new map
	HashMap<String, ArrayList<ValueNode>> temp = new HashMap<String, ArrayList<ValueNode>>();
	String elementCount = null;
	if (eplusMap.containsKey(objectName)) {
	    Integer count = eplusMap.get(objectName).size();
	    elementCount = count.toString();
	    temp.put(elementCount, newObject);
	    eplusMap.put(objectName, temp);
	} else {
	    elementCount = "0";
	    temp.put(elementCount, newObject);
	    eplusMap.put(objectName, temp);
	}
    }
    
    /**
     * Replace one specific inputs through all the same object
     * @param objectName the object
     * @param onKey the input description
     * @param toValue the value
     */
    public void editExistObjectAtSameTime(String objectName, String onKey, String toValue){
	//retrieve the elements from the map and convert it to set
	Set<String> subSet = eplusMap.get(objectName).keySet();
	Iterator iterator = subSet.iterator();
	//go through all the element in the set
	while(iterator.hasNext()){
	    ArrayList<ValueNode> vnList = eplusMap.get(objectName).get(iterator.next());
	    //go through all the ValueNode, find the key and change the value
	    for(ValueNode vn:vnList){
		if(onKey.equals(vn.getDescription())){
		    vn.setAttribute(toValue);
		}
	    }
	}
    }

    /*
     * notify GUI for the updates of variableList
     */
    private void onReadEplusFile() {
	for (loadIDFListener l : loadIDFListeners) {
	    l.loadedEnergyPlusFile(variableList);
	}
    }
    
    /**
     * This private class stores every line in IDF file as a node. Each node contains two
     * information, two indicators and one parametric signal.
     * 
     * Information: {@value attribute} shows the input for this node {@value
     * description} shows the explanation for the correspondent input
     * 
     * Indicators: {@value isEnd} indicates whether this line is the end of the
     * object inputs or not. {@value isCriticalLine} indicates whether this line
     * contains parametric value, which will later be replaced by parametric studies
     * 
     * Parametric Signal: the {@value index} indicates the index of the parametric
     * value, -1 shows there is no parametric value in this line, if the value is
     * greater than 1, that means this node contains parametric input.
     * 
     * @author Weili
     *
     */
    private class ValueNode {
        /**
         * Invariance: Each node should contains information includes an original
         * attribute and the input description
         */

        // used for copy itself
        private String originalAttribute;
        // real value which will then pass to the energyplus engine
        private String attribute;
        // comments after the (!)
        private String description;

        private boolean isEnd = false;
        private boolean isCriticalLine = false;

        private int index = -1;

        
        public ValueNode(String att, String des) {
    	description = des;
    	originalAttribute = att;
    	
    	//test whether this line contains parametric value
    	if (originalAttribute.indexOf("$") > -1) {
    	    isCriticalLine = true;
    	    index = originalAttribute.indexOf("$");
    	}
    	
    	//test whether this line is the end input
    	if (originalAttribute.indexOf(";") > -1) {
    	    attribute = originalAttribute.substring(0,
    		    originalAttribute.indexOf(";"));
    	    isEnd = true;
    	} else if (originalAttribute.indexOf(",") > -1) {
    	    attribute = originalAttribute.substring(0,
    		    originalAttribute.indexOf(","));
    	} else {
    	    // hopefully we will never get to this point
    	    attribute = att;
    	}
        }

        // gets the attribute of the this line
        private String getAttribute() {
    	return attribute;
        }
        
        // reset the attribute for this object
        // can be used to replace the parametric values in the EnergyPlus
        private void setAttribute(String attr){
    	attribute = attr;
        }

        // check whether this line is the end statment
        private boolean isEndStatement() {
    	return isEnd;
        }

        // get the description of this line
        private String getDescription() {
    	return description;
        }

        // check whether there is a parametric value defined in this line
        private boolean isCritical() {
    	return isCriticalLine;
        }
        
        //get the parametric value input index
        private int getIndex() {
    	return index;
        }
        
        //clone a new value node
        public ValueNode clone() {
    	return new ValueNode(originalAttribute, description);
        }
    }
}
