package analyzer.gui;

import java.io.File;

import javax.swing.JPanel;

import analyzer.listeners.GraphGenerationListener;

public class AnalysisPanel extends JPanel implements GraphGenerationListener{
    
    public final File resultFolder;
    
    public AnalysisPanel(File r){
	resultFolder = r;
    }
    
    
    
    

}
