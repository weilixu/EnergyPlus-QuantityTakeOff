package analyzer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;

import analyzer.eplus.EnergyPlusFilesGenerator;
import analyzer.model.Model;

public class CreateActionListener implements ActionListener{
    private final Model model;
    private EnergyPlusFilesGenerator generator;
    private HashMap<String, double[]> randomVariableList;
    private File folder;
    private final JButton parentButton;
    
    //shows the index of the data group
    private final int DATA_SIZE;
    
    public CreateActionListener(Model m, EnergyPlusFilesGenerator epg, File f, int number, JButton cb){
	model = m;
	generator = epg;
	randomVariableList = model.getData();
	DATA_SIZE = number;
	folder = f;
	parentButton = cb;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	parentButton.setEnabled(false);
	for(int i=0; i<DATA_SIZE; i++){
	    writingFile(generator.cloneIdfData(),i);
	    //move to the next index for the next round of simulation
	}
	parentButton.setEnabled(true);
	
    }
    
    private void writingFile(EnergyPlusFilesGenerator epg, Integer index){
	Set<String> variable = randomVariableList.keySet();
	Iterator<String> iterator = variable.iterator();
	while(iterator.hasNext()){
	    String charactor = iterator.next();
	    Double value = randomVariableList.get(charactor)[index];
	    epg.modifySpecialCharactor(charactor, value.toString());
	}
	//write the file
	epg.WriteIdf(folder.getAbsolutePath(),index.toString());
    }
}
