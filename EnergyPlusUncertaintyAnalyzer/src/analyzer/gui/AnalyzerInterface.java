package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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

import analyzer.lang.AnalyzerUtils;
import analyzer.listeners.LoadIdfListeners;
import analyzer.listeners.ModelDataListener;
import analyzer.model.Model;

public class AnalyzerInterface extends JPanel implements LoadIdfListeners,
	ModelDataListener {

    private final String DEFAULT_TITLE = "EnergyPlus Uncertainty Analyzer Beta Version";

    /*
     * All the models
     */
    private final Model core;

    /*
     * All text of menu bar
     */
    private final String MENU_TITLE = "Setting";
    private final String MENU_EXIT = "Exit";
    private final String MENU_LOAD = "Load IDF";
    private final String MENU_CONFIG = "Configuration";
    // private final String MENU_SWITCH = "Data Analysis";

    /*
     * All Menu Items
     */
    private final JMenuItem loadMenus;
    private final JMenuItem eplusConfig;

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
    private final JPanel innerPanel;
    private final VariablePanel variablePane;
    // contains the text field specify the number of simulations
    private final JPanel inputPanel;
    private final JPanel simulationPanel;

    // analysis panel set-up
    private final AnalysisPanel analysisPanel;
    private final JPanel analysisBottomPanel;

    private final LifeCycleCostPanel lccPanel;
    private final JPanel lccControlPanel;
    private JButton addButton;
    private final String ADD_TEXT = "Insert Data";

    /*
     * Eplus File
     */
    private File resultFolder;

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
    private JButton variableToAnalysisButton;
    private JButton analysisToVariableButton;
    private JButton variableToLccButton;
    private JButton lccToAnalysisButton;
    private JButton analysisToLccButton;
    private JButton lccToVariableButton;
    private int number_Variable;

    /*
     * Flags to indicates operations
     */
    private boolean analysisFlag = false; // indicates whether an analysis is
					  // performed or not

    /*
     * String[] stores the energyplus configuration
     */
    private final String DIR_NAME = "EnergyPlus directory (*):";
    private final String WEA_NAME = "Weather file name:";
    private final String PROC_NAME = "Number of processor:";
    private final JTextField eplusDir;
    private final JTextField weatherFile;
    private final JTextField numberProc;
    private final String CONFIG_TIP = "Simulation Configuration. Click Okay to customize your settings";

    public AnalyzerInterface(Model c) {
	// assign the model to the interface
	core = c;
	core.addModelDataListeners(this);
	// add the reader to the interface and load the listener
	core.addLoadIDFListeners(this);

	// build the frame
	frame = new JFrame(DEFAULT_TITLE);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setPreferredSize(new Dimension(650, 500));
	frame.setResizable(true);

	// Add the frame's panels to the view.
	outerPanel = new JPanel(new BorderLayout());

	// Add the input panel to the outer panel
	inputPanel = initInputPanel();
	outerPanel.add(inputPanel, BorderLayout.PAGE_START);

	// fileLabel = new JLabel(DEFAULT_FILE_NAME);
	// fileLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	// outerPanel.add(fileLabel, BorderLayout.NORTH);

	variablePane = new VariablePanel(core);
	innerPanel = new JPanel(new BorderLayout());
	innerPanel.add(variablePane, BorderLayout.CENTER);

	// Add the simulation Panel to the outer panel
	simulationPanel = initSimulationPanel();
	simulationPanel.setBackground(Color.WHITE);
	innerPanel.add(simulationPanel, BorderLayout.PAGE_END);

	outerPanel.add(innerPanel, BorderLayout.CENTER);
	frame.add(outerPanel);

	// set up an menu
	settingMenuBar = new JMenuBar();
	JMenu setting = new JMenu(MENU_TITLE);
	setting.setMnemonic(KeyEvent.VK_S);

	// set the configuration button
	eplusConfig = new JMenuItem(MENU_CONFIG);
	eplusConfig.setMnemonic(KeyEvent.VK_C);
	eplusConfig.setToolTipText(CONFIG_TIP);
	// retrieve the property file
	String[] config = AnalyzerUtils.getEplusConfig();
	eplusDir = new JTextField(config[0]);
	weatherFile = new JTextField(config[1]);
	numberProc = new JTextField(config[2]);
	eplusConfig.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		Object[] message = { DIR_NAME, eplusDir, WEA_NAME, weatherFile,
			PROC_NAME, numberProc };
		int option = JOptionPane.showConfirmDialog(frame, message,
			"Simulation Initializer", JOptionPane.OK_CANCEL_OPTION);
		if (option == JOptionPane.OK_OPTION) {
		    AnalyzerUtils.setEplusDirectory(eplusDir.getText());
		    AnalyzerUtils.setEplusWeather(weatherFile.getText());
		    AnalyzerUtils.setEplusProcessor(numberProc.getText());
		    AnalyzerUtils.writeProperties();
		}
	    }
	});
	setting.add(eplusConfig);

	// add load button. load the energyplus model
	loadMenus = new JMenuItem(MENU_LOAD);
	loadMenus.setMnemonic(KeyEvent.VK_L);
	loadMenus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		Integer simulationNumber = null;
		try {
		    //get the simulation number
		    String text = simulationText.getText();
		    simulationNumber = Integer.parseInt(text);
		    //sets the property file
		    AnalyzerUtils.setEplusSimulation(text);
		    
		    core.setSimulationNumber(simulationNumber);

		    try {
			// initialize the file directories
			String filePath = idfDirText.getText();
			File eplusFile = new File(filePath);
			core.initializeModel(eplusFile);
			//write it back to property file
			AnalyzerUtils.setEplusFileDir(filePath);

			// after read set the simulaiton number of the model
			// disable the JTextfield for simulation directory and
			// simulation number
			// also add the actionlistener to the create IDFButton

			createIDFButton
				.addActionListener(new CreateActionListener(
					core, createIDFButton));
			simulationText.setEnabled(false);
			idfDirText.setEnabled(false);
			//write back to property file
			AnalyzerUtils.writeProperties();
		    } catch (IOException e) {
			showErrorDialog(frame, "Cannot Load Idf File",
				"Please check your directory!");
		    }
		} catch (NumberFormatException ne) {
		    showErrorDialog(frame,
			    "Error found in Number of Simulations",
			    "The number of simulaitons has to be integers (e.g. 100)");
		} catch (NullPointerException np) {
		    showErrorDialog(frame,
			    "Encounter an error while processing file!",
			    "The File Cannot be processed, Please check your both inputs!");
		    np.printStackTrace();
		}

	    }
	});
	setting.add(loadMenus);

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

	// Initialize the analysis panel
	analysisPanel = new AnalysisPanel(core);
	analysisPanel.setBackground(Color.WHITE);
	analysisBottomPanel = initializeAnalysisBottomPanel();

	// Initialize the LCC panel
	lccPanel = new LifeCycleCostPanel(core);
	lccPanel.setBackground(Color.WHITE);
	lccControlPanel = initialzeLccControlPanel();

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
	    ArrayList<String[]> variableKeySets) {
	variablePane.changeVariables(variableList, variableKeySets);
	number_Variable = variableList.size();
	// outerPanel.add(innerPanel, BorderLayout.CENTER);
	outerPanel.revalidate();
	outerPanel.repaint();
    }

    @Override
    public void modelDataUpdate(int size) {
	if (size == variablePane.getVariableListSize()) {
	    createIDFButton.setEnabled(true);
	    simulationButton.setEnabled(true);
	}
    }

    @Override
    public void variableEnabled(String variable) {
	variablePane.changeFlagState(variable);
    }

    /**
     * creates the simulation information panel
     * 
     * @return
     */
    private JPanel initInputPanel() {
	JPanel tempPanel = new JPanel();
	String simulation = AnalyzerUtils.getEplusSimulation();
	simulationText = new JTextField(simulation);
	
	simulationText.setPreferredSize(new Dimension(250, 25));
	simulationText.setBorder(BorderFactory.createLoweredBevelBorder());
	// center the text
	simulationText.setHorizontalAlignment(JTextField.CENTER);
	simulationText.setToolTipText(SIM_TIP);

	idfDirLabel = new JLabel("File: ");
	String dir = AnalyzerUtils.getEplusFileDir();
	idfDirText = new JTextField(dir);
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

	variableToAnalysisButton = new JButton("Analyze Results");
	// analysisButton.setEnabled(false);
	variableToAnalysisButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		innerPanel.removeAll();
		if (!analysisFlag) {
		    analysisPanel.generateGraph();
		    analysisFlag = true;
		}
		innerPanel.add(analysisPanel, BorderLayout.CENTER);
		innerPanel.add(analysisBottomPanel, BorderLayout.PAGE_END);
		innerPanel.revalidate();
		innerPanel.repaint();
	    }
	});

	simulationButton = new JButton("Simulate...");
	simulationButton.setEnabled(false);
	simulationButton.addActionListener(new SimulationActionListener(frame,
		core, variableToAnalysisButton));

	variableToLccButton = new JButton("Cost Analysis");
	variableToLccButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		innerPanel.removeAll();
		innerPanel.add(lccPanel, BorderLayout.CENTER);
		innerPanel.add(lccControlPanel, BorderLayout.PAGE_END);
		innerPanel.revalidate();
		innerPanel.repaint();
	    }
	});

	tempPanel.add(createIDFButton);
	tempPanel.add(simulationButton);
	tempPanel.add(variableToLccButton);
	tempPanel.add(variableToAnalysisButton);
	return tempPanel;
    }

    private JPanel initializeAnalysisBottomPanel() {
	JPanel temp = new JPanel();
	analysisToVariableButton = new JButton("Variable Setting");
	analysisToVariableButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		innerPanel.removeAll();
		innerPanel.add(variablePane, BorderLayout.CENTER);
		innerPanel.add(simulationPanel, BorderLayout.PAGE_END);
		innerPanel.revalidate();
		innerPanel.repaint();
	    }
	});

	analysisToLccButton = new JButton("Cost Analysis");
	analysisToLccButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		innerPanel.removeAll();
		innerPanel.add(lccPanel, BorderLayout.CENTER);
		innerPanel.add(lccControlPanel, BorderLayout.PAGE_END);
		innerPanel.revalidate();
		innerPanel.repaint();
	    }
	});
	temp.add(analysisToVariableButton);
	temp.add(analysisToLccButton);
	return temp;
    }

    private JPanel initialzeLccControlPanel() {
	JPanel temp = new JPanel();
	addButton = new JButton(ADD_TEXT);
	addButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		core.addCurrentDataSetToIdf();
	    }
	});
	
	lccToAnalysisButton = new JButton("Analyze Results");
	lccToAnalysisButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		innerPanel.removeAll();
		if (!analysisFlag) {
		    analysisPanel.generateGraph();
		    analysisFlag = true;
		}
		innerPanel.add(analysisPanel, BorderLayout.CENTER);
		innerPanel.add(analysisBottomPanel, BorderLayout.PAGE_END);
		innerPanel.revalidate();
		innerPanel.repaint();
	    }
	});
	
	lccToVariableButton = new JButton("Variable Setting");
	lccToVariableButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		innerPanel.removeAll();
		innerPanel.add(variablePane, BorderLayout.CENTER);
		innerPanel.add(simulationPanel, BorderLayout.PAGE_END);
		innerPanel.revalidate();
		innerPanel.repaint();
	    }
	});
	
	temp.add(addButton);
	temp.add(lccToVariableButton);
	temp.add(lccToAnalysisButton);
	return temp;
    }

    // for error info
    private static void showErrorDialog(Component c, String title, String msg) {
	JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
    }

}