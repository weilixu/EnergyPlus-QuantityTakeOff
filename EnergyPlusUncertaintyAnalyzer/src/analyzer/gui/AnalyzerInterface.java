package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import analyzer.model.Model;

public class AnalyzerInterface extends JPanel {
    private final String DEFAULT_TITLE = "EnergyPlus Uncertainty Analyzer";
    private final String DEFAULT_FILE_NAME = "EnergyPlus file name";

    // All Setting Menu Title
    private final String MENU_TITLE = "Setting";
    private final String EPLUS_TITLE = "Load IDF file";
    private final String MENU_EXIT = "Exit";
    private final String MENU_REFRESH = "refresh";

    // All Menu-related inputs.
    private final JMenuItem loadIDF;
    private final JMenuItem refreshMenus;

    // EnergyPlus Dialog Title and Message
    private final String LOAD_TITLE = "Load your EnergyPlus file";
    private final String EPLUS_FILE_DIR = "Enter the file directory";

    private final Model core;

    // initial set-up variables
    private final JFrame frame;
    private final JMenuBar settingMenuBar;

    // The outer panel contains the IDF file name information etc...
    private final JPanel outerPanel;
    private final VariablePanel innerPanel;

    //private final JLabel fileLabel;

    public AnalyzerInterface(Model c) {
	// assign the model to the interface
	core = c;

	// build the frame
	frame = new JFrame(DEFAULT_TITLE);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setPreferredSize(new Dimension(700, 600));

	// Add the frame's panels to the view.
	outerPanel = new JPanel(new BorderLayout());

	//fileLabel = new JLabel(DEFAULT_FILE_NAME);
	//fileLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	//outerPanel.add(fileLabel, BorderLayout.NORTH);

	innerPanel = new VariablePanel(core);
	outerPanel.add(innerPanel, BorderLayout.CENTER);

	frame.add(outerPanel);

	// set up an menu
	settingMenuBar = new JMenuBar();
	JMenu setting = new JMenu(MENU_TITLE);
	setting.setMnemonic(KeyEvent.VK_S);

	// add load IDF option in the menu
	loadIDF = new JMenuItem(EPLUS_TITLE);
	loadIDF.setMnemonic(KeyEvent.VK_N);
	loadIDF.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		String directory = (String) JOptionPane.showInputDialog(frame,
			LOAD_TITLE, EPLUS_FILE_DIR, JOptionPane.PLAIN_MESSAGE,
			null, null, "");
		if (directory != null) {
		    // do actions to load/error
		    innerPanel.changeVariables(new ArrayList<String>());
		    //outerPanel.add(innerPanel, BorderLayout.CENTER);
		    outerPanel.revalidate();
		    outerPanel.repaint();
		}
	    }
	});
	setting.add(loadIDF);

	// add refresh button. reset all the settings
	refreshMenus = new JMenuItem(MENU_REFRESH);
	refreshMenus.setMnemonic(KeyEvent.VK_R);
	refreshMenus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		// do something
	    }
	});
	setting.add(refreshMenus);

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
}
