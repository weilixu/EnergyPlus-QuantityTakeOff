package analyzer.main;

import javax.swing.SwingUtilities;

import analyzer.gui.AnalyzerInterface;
import analyzer.model.Model;

public class Analyzer{

    // Messages
  
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		Model m = new Model();
		try {
		    AnalyzerInterface gui = new AnalyzerInterface(m);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	});
    }
}
