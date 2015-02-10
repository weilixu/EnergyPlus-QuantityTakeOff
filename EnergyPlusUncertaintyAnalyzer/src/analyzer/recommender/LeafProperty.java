package analyzer.recommender;

public class LeafProperty {
    
    private final String description;
    private final String reference;
    private final String notes;
    
    private final String distName;
    private final String distParam;
    private final String minimum;
    private final String maximum;
    
    public LeafProperty(String d, String r, String n, String dist, String param, String min, String max){
	description = d;
	reference = r;
	notes = n;
	
	distName = dist;
	distParam = param;
	minimum = min;
	maximum = max;
    }
    
    public String getDescription(){
	return description;
    }
    
    public String getReference(){
	return reference;
    }
    
    public String getNotes(){
	return notes;
    }
    
    public String getDistName(){
	return distName;
    }
    
    public String getDistParameter(){
	return distParam;
    }
    
    public String getMinimum(){
	return minimum;
    }
    
    public String getMaximum(){
	return maximum;
    }
    
    @Override
    public String toString(){
	return description;
    }
    
    public String getFullDescription(){
	StringBuilder sb = new StringBuilder();
	sb.append(description);
	sb.append("\n\n");
	sb.append("Distribution Name: "+distName);
	sb.append("\n\n");
	
	String[] distArray = distParam.split(";");
	sb.append("Distribution Parameters: ");
	for(String s: distArray){
	    sb.append("\n");
	    sb.append(s);
	}
	sb.append("\n\n");
	sb.append("Minimum: "+minimum);
	sb.append("\n\n");
	sb.append("Maximum: "+maximum);
	sb.append("\n\n");
	sb.append("Reference: "+reference);
	sb.append("\n\n");
	sb.append("Notes: "+notes);
	return sb.toString();
    }

}
