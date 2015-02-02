package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import analyzer.listeners.FitDistListeners;
import analyzer.model.Model;
import analyzer.readcsv.ReadCSV;

public class FitDistPanel extends JPanel implements FitDistListeners{

    private final Model model;
    private final ReadCSV reader;
    private final String variable;

    /*
     * Setting all the panels
     */
    private final JTabbedPane parentPanel;
    // to set up the fit distribution settings
    private final JPanel settingPanel;
    // to capture the distribution display once the set-up is complete
    private final JPanel displayPanel;
    private final JPanel loadPanel;
    private final JPanel fitPanel;

    /*
     * All the texts relates to the setting components
     */
    private final String SORT_CRITERIA_TEXT = "Sorting Criteria";
    private final String SORT_TIP = "is the likelihood function used to sort the fitted probability distribtuions."
	    + "Valid inputs are: NLogL - Negative Log Likelihood BIC - Bayesian Information criterion"
	    + "AIC - Akaike information criterion AICc";
    private final String DIST_TYPE_TEXT = "Distribution Type";
    private final String DIST_TIP = "dataType specifies whether to fit a 'DISCRETE' or 'CONTINUOUS' distribution";
    private final String LOWER_TEXT = "0";
    private final String LOWER_TIP = "lower bound where the generated random variables will be truncated to";
    private final String UPPER_TEXT = "1";
    private final String UPPER_TIP = "upper bound where the generated random variables will be truncated to";
    private final String LOWER_LABEL_TEXT = "Lower";
    private final String UPPER_LABEL_TEXT = "Upper";
    private final String VARIABLE_SET;

    /*
     * Setting the comboboxes/text fields for distribution generation
     * specification
     */
    private final JLabel sortCriteriaLabel;
    private final JComboBox sortCriteriaCombo;
    private final JLabel distTypeLabel;
    private final JComboBox distTypeCombo;
    private final JLabel lowerLabel;
    private final JTextField lowerText;
    private final JLabel upperLabel;
    private final JTextField upperText;
    private final JTextField csvText;
    private final JButton loadButton;
    
    /*
     * Setting the TextArea for display the fitted results
     */
    private final JTextArea fittedResults;
    private final String TEXT_TITLE = "Dist Result";

    /*
     * Setting the finish button
     */
    private final JButton doneButton;
    private final JButton refreshButton;
    private final String DONE_TEXT = "Generate RV";
    private final String REFRESH_TEXT = "Re-do";

    public FitDistPanel(JTabbedPane tp, Model m, String v, String s) {
	model = m;
	model.addFitDistListeners(this);
	reader = new ReadCSV();
	parentPanel = tp;
	variable = v;
	VARIABLE_SET = s;
	this.setLayout(new BorderLayout());
	/*
	 * Setting panel set-up
	 */
	settingPanel = new JPanel();

	// settingPanel.setLayout(new
	// BoxLayout(settingPanel,BoxLayout.PAGE_AXIS));

	/*
	 * initialize all the distribution inputs for the setting panel
	 */
	// label to indicate the sorting criteria combo box
	sortCriteriaLabel = new JLabel(SORT_CRITERIA_TEXT);
	sortCriteriaLabel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
	settingPanel.add(sortCriteriaLabel);

	// set sorting criteria combo box
	String[] sortingList = { "NLogL","BIC", "AIC","AICc"};
	// set the default sorting criteria to BIC
	sortCriteriaCombo = new JComboBox(sortingList);
	sortCriteriaCombo.setSelectedIndex(0);
	sortCriteriaCombo.setToolTipText(SORT_TIP);
	sortCriteriaCombo.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		// do nothing first
	    }
	});
	sortCriteriaCombo.setAlignmentX(Component.BOTTOM_ALIGNMENT);
	sortCriteriaCombo.setPreferredSize(new Dimension(100, 25));
	settingPanel.add(sortCriteriaCombo);

	// label to indicate the distribution type combo box
	distTypeLabel = new JLabel(DIST_TYPE_TEXT);
	distTypeLabel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
	settingPanel.add(distTypeLabel);

	// set the distribution type combo box
	String[] distTypeList = { "Continuous", "Discrete" };
	distTypeCombo = new JComboBox(distTypeList);
	// set the default distribution type to continuous distribution
	distTypeCombo.setSelectedIndex(0);
	distTypeCombo.setToolTipText(DIST_TIP);
	distTypeCombo.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	    }
	});
	distTypeCombo.setAlignmentX(Component.BOTTOM_ALIGNMENT);
	distTypeCombo.setPreferredSize(new Dimension(100, 25));
	settingPanel.add(distTypeCombo);

	// setting the lower boundary label and text field
	lowerLabel = new JLabel(LOWER_LABEL_TEXT);
	settingPanel.add(lowerLabel);

	lowerText = new JTextField(LOWER_TEXT);
	lowerText.setBorder(BorderFactory.createLoweredBevelBorder());
	lowerText.setPreferredSize(new Dimension(50, 25));
	lowerText.setToolTipText(LOWER_TIP);
	settingPanel.add(lowerText);

	// setting the upper boundary label and text field
	upperLabel = new JLabel(UPPER_LABEL_TEXT);
	settingPanel.add(upperLabel);

	upperText = new JTextField(UPPER_TEXT);
	upperText.setBorder(BorderFactory.createLoweredBevelBorder());
	upperText.setPreferredSize(new Dimension(50, 25));
	upperText.setToolTipText(UPPER_TIP);
	settingPanel.add(upperText);

	fitPanel = new JPanel(new BorderLayout());
	fitPanel.add(settingPanel, BorderLayout.CENTER);
	Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	TitledBorder title = BorderFactory.createTitledBorder(raisedbevel,
		VARIABLE_SET);
	fitPanel.setBorder(title);
	this.add(fitPanel, BorderLayout.NORTH);

	loadPanel = new JPanel();

	// the the finish button
	doneButton = new JButton(DONE_TEXT);
	doneButton.setEnabled(false);
	doneButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// 1. send the information to the model
		doneButton.setEnabled(false);
		model.setVariable(variable);
		model.fitData(reader.getData(),
			(String)sortCriteriaCombo.getSelectedItem(),
			(String)distTypeCombo.getSelectedItem(), lowerText.getText(),
			upperText.getText());
		// 2. disable another tab
		doneButton.setEnabled(true);
		parentPanel.setEnabledAt(1, false);
	    }
	});
	doneButton.setAlignmentX(Component.BOTTOM_ALIGNMENT);

	// csv loading text field and button
	csvText = new JTextField("Enter Your Data File Directory Here:");
	csvText.setPreferredSize(new Dimension(250,25));
	csvText.setBorder(BorderFactory.createLoweredBevelBorder());
	loadButton = new JButton("Load");
	loadButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent evt) {
		String filePath = csvText.getText();
		File file = new File(filePath);
		if (!file.isFile()) {
		    csvText.setText("This is not a valid directory!!");
		} else {
		    try {
			// 2. load the csv file
			reader.readData(filePath);
			doneButton.setEnabled(true);
		    } catch (Exception e) {
			csvText.setText("This is not a .csv file!!");
		    }
		}
	    }
	});

	loadPanel.add(csvText);
	loadPanel.add(loadButton);
	loadPanel.add(doneButton);

	refreshButton = new JButton(REFRESH_TEXT);
	refreshButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// cancel the information sending
		// fill in the code later
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// enable the other tab
		parentPanel.setEnabledAt(1, true);
		model.setVariable(variable);
		model.refreshGeneration();
	    }
	});

	refreshButton.setAlignmentX(Component.BOTTOM_ALIGNMENT);
	loadPanel.add(refreshButton);

	// add loadpanel to fitpanel
	fitPanel.add(loadPanel, BorderLayout.NORTH);
	
	//JTextArea set-up for the dist results display
	fittedResults = new JTextArea();
	TitledBorder textAreaTitle = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
		TEXT_TITLE);
	fittedResults.setBorder(textAreaTitle);
	fittedResults.setEnabled(false);
	this.add(fittedResults,BorderLayout.LINE_START);

	// set-up the display panel
	displayPanel = new ImageDisplayPanel(model, variable);
	add(displayPanel, BorderLayout.CENTER);
    }

    @Override
    public void fitDataGenerated(String sb) {
	fittedResults.setText(sb);
    }

    @Override
    public String getVariable() {
	return variable;
    }
}
