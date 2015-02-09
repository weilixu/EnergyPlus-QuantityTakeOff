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
    
    private final String object;
    
    public ObjectProperty(String o){
	object = o;
    }
    
    
    /**
     * getter method to get the information
     * @return
     */
    public String getObject(){
	return object;
    }
    
    /**
     * override method to display the node name in the JTree
     */
    @Override
    public String toString(){
	return object;
    }
}
