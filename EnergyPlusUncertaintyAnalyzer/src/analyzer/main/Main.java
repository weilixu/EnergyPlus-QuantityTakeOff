package analyzer.main;

import javax.swing.SwingUtilities;

import analyzer.gui.AnalyzerInterface;
import analyzer.model.Model;

public class Main {
    
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndStartFramework();
			}
		});
	}
	
	private static void createAndStartFramework(){
	    //change when the model is complete
	    Model model = new Model();
	    AnalyzerInterface frame = new AnalyzerInterface(model);
	}


}
