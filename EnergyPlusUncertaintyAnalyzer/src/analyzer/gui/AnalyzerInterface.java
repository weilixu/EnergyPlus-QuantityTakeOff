package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import analyzer.eplus.IdfReader;
import analyzer.listeners.loadIDFListener;
import analyzer.model.Model;

public class AnalyzerInterface extends JPanel implements loadIDFListener {

    private final String DEFAULT_TITLE = "EnergyPlus Uncertainty Analyzer";

    // All text of menu bar
    private final String MENU_TITLE = "Setting";
    private final String MENU_EXIT = "Exit";
    private final String MENU_LOAD = "Load IDF";
    private final String MENU_SWITCH = "Data Analysis";

    // All Menu Items
    private final JMenuItem loadMenus;
    private final JMenuItem dataAnalysisSwitcherMenus;

    private final Model core;

    // initial set-up variables
    private final JFrame frame;
    private final JMenuBar settingMenuBar;

    // The outer panel contains the IDF file name information etc...
    private final JPanel outerPanel;
    private final VariablePanel innerPanel;
    
    //record eplus file
    private final File eplusFile;
    
    private final IdfReader idfReader;
    
    public AnalyzerInterface(Model c, JFrame f, File file) {
	// assign the model to the interface
	core = c;
	eplusFile = file;
	
	//add the reader to the interface and load the listener
	idfReader = new IdfReader(file.getAbsolutePath());
	idfReader.addLoadIDFListeners(this);

	// build the frame
	frame = f;
	f.setTitle(DEFAULT_TITLE);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setPreferredSize(new Dimension(700, 600));

	// Add the frame's panels to the view.
	outerPanel = new JPanel(new BorderLayout());

	// fileLabel = new JLabel(DEFAULT_FILE_NAME);
	// fileLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	// outerPanel.add(fileLabel, BorderLayout.NORTH);

	innerPanel = new VariablePanel(core);
	outerPanel.add(innerPanel, BorderLayout.CENTER);

	frame.add(outerPanel);

	// set up an menu
	settingMenuBar = new JMenuBar();
	JMenu setting = new JMenu(MENU_TITLE);
	setting.setMnemonic(KeyEvent.VK_S);

	// add refresh button. reset all the settings
	loadMenus = new JMenuItem(MENU_LOAD);
	loadMenus.setMnemonic(KeyEvent.VK_L);
	loadMenus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		try{
		    //reads the file
		    idfReader.readEplusFile();
		}catch(IOException e){
		    showErrorDialog(frame,"Cannot Load Idf File","Please check your directory!");
		}
	    }
	});
	setting.add(loadMenus);

	dataAnalysisSwitcherMenus = new JMenuItem(MENU_SWITCH);
	dataAnalysisSwitcherMenus.setMnemonic(KeyEvent.VK_D);
	dataAnalysisSwitcherMenus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		// switch to data analysis interface;
	    }
	});
	setting.add(dataAnalysisSwitcherMenus);

	// add the separator to divide the data inputs and frame function
	setting.addSeparator();

	// exit function
	JMenuItem exitMenuItem = new JMenuItem(MENU_EXIT);
	exitMenuItem.setMnemonic(KeyEvent.VK_X);
	exitMenuItem.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		System.exit(0);
	    }
	});
	setting.add(exitMenuItem);

	// add menubar
	settingMenuBar.add(setting);
	frame.setJMenuBar(settingMenuBar);

	frame.pack();
	frame.setVisible(true);
    }

    // after load the energyplus file, the IdfReader will collect the variables
    // found in each node and send it back to gui
    @Override
    public void loadedEnergyPlusFile(ArrayList<String> variableList, ArrayList<String> variableInfo) {
	innerPanel.changeVariables(variableList, variableInfo);
	// outerPanel.add(innerPanel, BorderLayout.CENTER);
	outerPanel.revalidate();
	outerPanel.repaint();
    }
    
    //for info showing
    private static void showInfoDialog(Component c, String title, String msg) {
        JOptionPane.showMessageDialog(c, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }
    //for error info
    private static void showErrorDialog(Component c, String title, String msg) {
        JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}