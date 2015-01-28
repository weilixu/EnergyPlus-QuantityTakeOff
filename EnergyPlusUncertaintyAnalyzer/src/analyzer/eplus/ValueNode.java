package analyzer.eplus;

/**
 * This class stores every line in IDF file as a node. Each node contains two
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
public class ValueNode {
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
    public String getAttribute() {
	return attribute;
    }

    // check whether this line is the end statment
    public boolean isEndStatement() {
	return isEnd;
    }

    // get the description of this line
    public String getDescription() {
	return description;
    }

    // check whether there is a parametric value defined in this line
    public boolean isCritical() {
	return isCriticalLine;
    }
    
    //get the parametric value input index
    public int getIndex() {
	return index;
    }
    
    //replace the parametric value to the input
    public void replaceCharactor(String input) {
	String temp = attribute.substring(0, index) + input
		+ attribute.substring(index + 1);
	attribute = temp;
    }
    
    //clone a new value node
    public ValueNode clone() {
	return new ValueNode(originalAttribute, description);
    }
}
