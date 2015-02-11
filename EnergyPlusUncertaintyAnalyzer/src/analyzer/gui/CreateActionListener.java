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
    private final JButton parentButton;
    
    
    public CreateActionListener(Model m,JButton cb){
	model = m;
	parentButton = cb;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	parentButton.setEnabled(false);
	model.writeIdfFile(model.getResultFolder());
	parentButton.setEnabled(true);
    }
}
