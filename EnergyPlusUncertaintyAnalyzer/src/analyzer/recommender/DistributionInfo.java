package analyzer.recommender;

/**
 * This shows the leaf node information. The leaf node feed information to this object
 * @author Weili
 *
 */
public class DistributionInfo {
    
    private StringBuffer info;
    private boolean isFilled;
    
    public DistributionInfo(){
	info = new StringBuffer();
	isFilled = false;
    }
    
    /**
     * information feeding method
     * @param s
     */
    public void addString(String s){
	info.append("\n");
	info.append(s);
	isFilled = true;
    }
    
    /**
     * check whether this object is used and activated
     * @return
     */
    public boolean isFilled(){
	return isFilled;
    }
    
    public String getFullDescription(){
	return info.toString();
    }
    
    @Override
    public String toString(){
	return "data";
    }
    

}
