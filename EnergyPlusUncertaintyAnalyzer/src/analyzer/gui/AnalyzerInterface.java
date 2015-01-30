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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import analyzer.eplus.IdfReader;
import analyzer.listeners.LoadIdfListeners;
import analyzer.listeners.ModelDataListener;
import analyzer.model.Model;

public class AnalyzerInterface extends JPanel implements LoadIdfListeners,
	ModelDataListener {

    private final String DEFAULT_TITLE = "EnergyPlus Uncertainty Analyzer";

    /*
     * All the models
     */
    private final Model core;
    private final IdfReader idfReader;

    /*
     * All text of menu bar
     */
    private final String MENU_TITLE = "Setting";
    private final String MENU_EXIT = "Exit";
    private final String MENU_LOAD = "Load IDF";
    private final String MENU_SWITCH = "Data Analysis";

    /*
     * All Menu Items
     */
    private final JMenuItem loadMenus;
    private final JMenuItem dataAnalysisSwitcherMenus;

    /*
     * initial set-up variables
     */
    private final JFrame frame;
    private final JMenuBar settingMenuBar;

    /*
     * All the panels
     */
    // The outer panel contains the IDF file name information etc...
    private final JPanel outerPanel;
    private final VariablePanel innerPanel;
    // contains the text field specify the number of simulations
    private final JPanel inputPanel;
    private final JPanel simulationPanel;

    /*
     * Eplus File
     */
    private final File eplusFile;
    private final File parentFile;
    private final String RESULT = "Results";
    private final File resultFolder;

    /*
     * for global variables in inputPanel
     */
    private final String DIR_TIP = ".idf file directory. e.g. C:\\User\\Desktop\\New folder\\test.idf";
    private final String SIM_TIP = "Enter simulaiton number. (>=1) e.g 1000";
    private JTextField simulationText;
    private JLabel idfDirLabel;
    private JTextField idfDirText;

    /*
     * for buttons in simulationPanel
     */
    private JButton simulationButton;
    private JButton createIDFButton;
    private int number_Variable;

    public AnalyzerInterface(Model c, JFrame f, File file) {
	// assign the model to the interface
	core = c;
	core.addModelDataListeners(this);
	eplusFile = file;
	parentFile = eplusFile.getParentFile();
	resultFolder = createResultsFolder();

	// add the reader to the interface and load the listener
	idfReader = new IdfReader();
	idfReader.addLoadIDFListeners(this);

	// build the frame
	frame = f;
	f.setTitle(DEFAULT_TITLE);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setPreferredSize(new Dimension(700, 600));

	// Add the frame's panels to the view.
	outerPanel = new JPanel(new BorderLayout());

	// Add the input panel to the outer panel
	inputPanel = initInputPanel();
	outerPanel.add(inputPanel, BorderLayout.PAGE_START);

	// fileLabel = new JLabel(DEFAULT_FILE_NAME);
	// fileLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	// outerPanel.add(fileLabel, BorderLayout.NORTH);

	innerPanel = new VariablePanel(core, eplusFile);
	outerPanel.add(innerPanel, BorderLayout.CENTER);

	// Add the simulation Panel to the outer panel
	simulationPanel = initSimulationPanel();
	outerPanel.add(simulationPanel, BorderLayout.PAGE_END);

	frame.add(outerPanel);

	// set up an menu
	settingMenuBar = new JMenuBar();
	JMenu setting = new JMenu(MENU_TITLE);
	setting.setMnemonic(KeyEvent.VK_S);

	// add load button. load the energyplus model
	loadMenus = new JMenuItem(MENU_LOAD);
	loadMenus.setMnemonic(KeyEvent.VK_L);
	loadMenus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		try {
		    core.setSimulationNumber(Integer.parseInt(simulationText
			    .getText()));
		    try {
			// reads the file
			idfReader.setFilePath(idfDirText.getText());
			idfReader.readEplusFile();
			// after read set the simulaiton number of the model

			// disable the JTextfield for simulation directory and
			// simulation number
			// also add the actionlistener to the create IDFButton
			createIDFButton.addActionListener(new CreateActionListener(core,
				idfReader, resultFolder, Integer.parseInt(simulationText.getText())));
			simulationText.setEnabled(false);
			idfDirText.setEnabled(false);
		    } catch (IOException e) {
			showErrorDialog(frame, "Cannot Load Idf File",
				"Please check your directory!");
		    }
		} catch (NumberFormatException ne) {
		    showErrorDialog(frame,
			    "Error found in Number of Simulations",
			    "The number of simulaitons has to be integers (e.g. 100)");
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
    public void loadedEnergyPlusFile(ArrayList<String> variableList,
	    ArrayList<String> variableInfo) {
	innerPanel.changeVariables(variableList, variableInfo);
	number_Variable = variableList.size();
	// outerPanel.add(innerPanel, BorderLayout.CENTER);
	outerPanel.revalidate();
	outerPanel.repaint();
    }

    @Override
    public void modelDataUpdate(int size) {
	if (size == number_Variable) {
	    createIDFButton.setEnabled(true);
	    simulationButton.setEnabled(true);
	}
    }

    /**
     * creates the simulation information panel
     * 
     * @return
     */
    private JPanel initInputPanel() {
	JPanel tempPanel = new JPanel();
	idfDirLabel = new JLabel("File: ");
	simulationText = new JTextField("Number of Simulation (>=1)");
	simulationText.setPreferredSize(new Dimension(250, 25));
	simulationText.setBorder(BorderFactory.createLoweredBevelBorder());
	// center the text
	simulationText.setHorizontalAlignment(JTextField.CENTER);
	simulationText.setToolTipText(SIM_TIP);

	idfDirText = new JTextField(eplusFile.getAbsolutePath());
	idfDirText.setPreferredSize(new Dimension(250, 25));
	idfDirText.setBorder(BorderFactory.createLoweredBevelBorder());
	idfDirText.setToolTipText(DIR_TIP);
	tempPanel.add(idfDirLabel);
	tempPanel.add(idfDirText);
	tempPanel.add(simulationText);
	tempPanel.setBorder(BorderFactory
		.createEtchedBorder(EtchedBorder.RAISED));
	return tempPanel;
    }

    private JPanel initSimulationPanel() {
	JPanel tempPanel = new JPanel();
	tempPanel.setBorder(BorderFactory
		.createEtchedBorder(EtchedBorder.LOWERED));

	createIDFButton = new JButton("Create...");
	createIDFButton.setEnabled(false);

	simulationButton = new JButton("Simulate...");
	simulationButton.setEnabled(false);
	simulationButton.addActionListener(new SimulationActionListener(frame,
		resultFolder));
	tempPanel.add(createIDFButton);
	tempPanel.add(simulationButton);
	return tempPanel;
    }
    
    private File createResultsFolder(){
	
	File dir = new File(parentFile.getAbsoluteFile()+"\\"+RESULT);
	if(dir.exists()){
	    return dir;
	}else{
	    dir.mkdir();
	    return dir;
	}
    }

    // for info showing
    private static void showInfoDialog(Component c, String title, String msg) {
	JOptionPane.showMessageDialog(c, msg, title,
		JOptionPane.INFORMATION_MESSAGE);
    }

    // for error info
    private static void showErrorDialog(Component c, String title, String msg) {
	JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
    }

}