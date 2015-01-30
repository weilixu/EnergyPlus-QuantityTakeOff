package analyzer.listeners;
/**
 * GUI listen to the data updates in the  model
 * @author Weili
 *
 */
public interface ModelDataListener {
    
    /*
     * capture the updates from the model
     */
    public void modelDataUpdate(int size);

}
