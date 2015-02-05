package analyzer.listeners;

import java.util.ArrayList;
/**
 * GUI listeners that interacts with IdfReader for reading and creating IDF data structure
 * 
 * @author Weili
 *
 */
public interface LoadIdfListeners {
    
    public void loadedEnergyPlusFile(ArrayList<String> variableList, ArrayList<String[]> variableKeySet);

}
