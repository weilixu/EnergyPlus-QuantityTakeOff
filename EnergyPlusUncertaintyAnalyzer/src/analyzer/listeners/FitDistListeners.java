package analyzer.listeners;
/**
 * Fit Distribution listeners listens the fit model updates.
 * It extracts the properties of the fitted model
 * @author Weili
 *
 */
public interface FitDistListeners {
    
    /**
     * update when the data is fitted
     * @param sb
     */
    public void fitDataGenerated(StringBuffer sb);
    
    /**
     * get the variable name from the model
     * @return
     */
    public String getVariable();

}
