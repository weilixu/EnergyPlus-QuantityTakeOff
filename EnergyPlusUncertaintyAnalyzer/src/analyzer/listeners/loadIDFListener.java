package analyzer.listeners;

import java.util.ArrayList;
/**
 * listeners
 * 
 * @author Weili
 *
 */
public interface loadIDFListener {
    
    public void loadedEnergyPlusFile(ArrayList<String> variableList, ArrayList<String> variableInfo);

}
