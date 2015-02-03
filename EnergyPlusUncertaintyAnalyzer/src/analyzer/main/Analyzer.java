package analyzer.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
		AnalyzerInterface gui = new AnalyzerInterface(m);
	    }
	});
    }
}
