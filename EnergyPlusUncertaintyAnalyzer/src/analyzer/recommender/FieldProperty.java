package analyzer.recommender;

public class FieldProperty {
    
    private final String field;
    private final String unit;
    private final String notes;
    
    /**
     * Field properties
     * @param f
     * @param u
     * @param n
     */
    public FieldProperty(String f, String u, String n){
	field = f;
	unit = u;
	notes = n;
    }
    
    /**
     * get the field
     * @return
     */
    public String getField(){
	return field;
    }
    
    /**
     * get the unit
     * @return
     */
    public String getUnit(){
	return unit;
    }
    
    /**
     * get the notes of this object
     * @return
     */
    public String getNotes(){
	return notes;
    }
    
    @Override
    public String toString(){
	return field;
    }
    
    public String getFullDescription(){
	StringBuffer sb = new StringBuffer();
	sb.append("Field: "+field);
	sb.append("\n");
	sb.append("Unit: "+unit);
//	sb.append("\n");
//	sb.append("Notes: "+notes);
	return sb.toString();
    }

}
