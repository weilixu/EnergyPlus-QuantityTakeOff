package analyzer.recommender;

/**
 * This class represents one property of a particular object
 * The property includes:
 * <value>object<value> the object name in the EnergyPlus
 * <value>information<value> the input name of the property in EnergyPlus
 * <value> unit <value> the default unit in EnergyPlus
 * <value> reference <value> the reference of recommendations
 * @author Weili
 *
 */
public class ObjectProperty {
    
    private final String field;
    private final String unit;
    private final String reference;
    
    public ObjectProperty(String f, String u, String r){
	field = f;
	unit = u;
	reference = r;
    }
    
    
    /**
     * getter method to get the information
     * @return
     */
    public String getField(){
	return field;
    }
    
    /**
     * getter method to get the unit
     * @return
     */
    public String getUnit(){
	return unit;
    }
    
    /**
     * getter method to get the reference
     * @return
     */
    public String getReference(){
	return reference;
    }
    
    /**
     * getter method to get the full description of this object
     * @return
     */
    public String getFullDescription(){
	StringBuffer sb = new StringBuffer();
	sb.append(field);
	sb.append(" {");
	sb.append(unit);
	sb.append("}");
	sb.append("\n");
	sb.append("Reference: ");
	sb.append(reference);
	return sb.toString();
    }
    
    /**
     * override method to display the node name in the JTree
     */
    @Override
    public String toString(){
	return field;
    }
}
