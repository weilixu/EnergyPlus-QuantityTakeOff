package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import analyzer.model.Model;

public class AnalyzerInterface extends JPanel {
    
    private final String DEFAULT_TITLE = "EnergyPlus Uncertainty Analyzer";

    // All Setting Menu Title
    private final String MENU_TITLE = "Setting";
    private final String MENU_EXIT = "Exit";
    private final String MENU_REFRESH = "Load";

    private final JMenuItem loadMenus;

    private final Model core;

    // initial set-up variables
    private final JFrame frame;
    private final JMenuBar settingMenuBar;

    // The outer panel contains the IDF file name information etc...
    private final JPanel outerPanel;
    private final VariablePanel innerPanel;

    private final File eplusFile;

    public AnalyzerInterface(Model c, JFrame f, File file) {
	// assign the model to the interface
	core = c;
	eplusFile = file;

	// build the frame
	frame = f;
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
	loadMenus = new JMenuItem(MENU_REFRESH);
	loadMenus.setMnemonic(KeyEvent.VK_R);
	loadMenus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		// also here should use the eplusFile variable to load the
		// eplus, the inputs to the inner panel would be the searched
		// values
		// do actions to load/error
		innerPanel.changeVariables(new ArrayList<String>());
		// outerPanel.add(innerPanel, BorderLayout.CENTER);
		outerPanel.revalidate();
		outerPanel.repaint();
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

	// add menubar
	settingMenuBar.add(setting);
	frame.setJMenuBar(settingMenuBar);

	frame.pack();
	frame.setVisible(true);
    }
}