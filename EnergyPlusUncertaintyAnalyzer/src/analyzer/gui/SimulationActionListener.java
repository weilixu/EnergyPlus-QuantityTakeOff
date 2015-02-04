package analyzer.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import analyzer.eplus.RunEnergyPlus;
import analyzer.lang.AnalyzerUtils;


public class SimulationActionListener implements ActionListener{
    private final RunEnergyPlus run;
    private final JFrame parentFrame;
    private final JButton analysisButton;
    
    //entering the option box's 
    private final JTextField eplusDir;
    private final JTextField weatherFile;
    private final JTextField numberProc;
    private final String DIR_NAME = "EnergyPlus directory (*):";
    private final String WEA_NAME = "Weather file name:";
    private final String PROC_NAME = "Number of processor:";

    
    public SimulationActionListener(JFrame frame, RunEnergyPlus r, JButton ab){
	parentFrame = frame;
	analysisButton = ab;
	run = r;
	
	String[] config = AnalyzerUtils.getEplusConfig();
	eplusDir = new JTextField(config[0]);
	eplusDir.setToolTipText("EnergyPlus file directory. e.g.C:\\Users\\EnergyPlusV8-1-0\\ (Required field)");
	weatherFile = new JTextField(config[1]);
	weatherFile.setToolTipText("Weather file directory. e.g. USA_IL_Chicago-OHare.Intl.AP.725300_TMY3 (Option field)");
	numberProc = new JTextField(config[2]);
	numberProc.setToolTipText("Number of Parallel simulations. Recommend 4-8 (Option field)");
	
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	Object[] message = {DIR_NAME,eplusDir,
		WEA_NAME, weatherFile,
		PROC_NAME, numberProc
	};
	
	int option = JOptionPane.showConfirmDialog(parentFrame, message, "Simulation Initializer", JOptionPane.OK_CANCEL_OPTION);
	if(option == JOptionPane.OK_OPTION){
	    AnalyzerUtils.setEplusDirectory(eplusDir.getText());
	    AnalyzerUtils.setEplusWeather(weatherFile.getText());
	    AnalyzerUtils.setEplusProcessor(numberProc.getText());
	    
	    run.setEnergyPlusDirectory(eplusDir.getText());
	    run.setWeatherFile(weatherFile.getText());
	    run.setNumberOfProcessor(numberProc.getText());
	    try {
		run.startSimulation();
		analysisButton.setEnabled(true);
	    } catch (Exception e) {
		showErrorDialog(parentFrame, "Found Error in inputs", "Check your configuration inputs!");
		e.printStackTrace();
	    }
	}else{
	    //cancel, do nothing
	}
	
    }
    // for error info
    private static void showErrorDialog(Component c, String title, String msg) {
	JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
    }

}
