package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class FitDistPanel extends JPanel{
    
    /*
     * Setting all the panels
     */
    private final JTabbedPane parentPanel;
    //to set up the fit distribution settings
    private final JPanel settingPanel;
    //to capture the distribution display once the set-up is complete
    private final JPanel displayPanel;
    
    /*
     * All the texts relates to the setting components
     */
    private final String SIMULATION_TEXT = "No. of Simulation";
    private final String SORT_CRITERIA_TEXT = "Sorting Criteria";
    private final String DIST_TYPE_TEXT = "Distribution Type";
    
    /*
     * Setting the comboboxes/text fields for distribution generation specification
     */
    private final JTextField simulationNumberText;
    private final JLabel sortCriteriaLabel;
    private final JComboBox sortCriteriaCombo;
    private final JLabel distTypeLabel;
    private final JComboBox distTypeCombo;
    
    /*
     * Setting the finish button
     */
    private final JButton doneButton;
    private final String DONE_TEXT = "Done";
    
    
    public FitDistPanel(JTabbedPane tp){
	
	parentPanel = tp;
	this.setLayout(new BorderLayout());
	
	/*
	 * Setting panel set-up
	 */
	settingPanel = new JPanel();
	Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	TitledBorder title = BorderFactory.createTitledBorder(raisedbevel,"Setting");
	settingPanel.setBorder(title);
	
	settingPanel.setLayout(new BoxLayout(settingPanel,BoxLayout.PAGE_AXIS));
	
	/*
	 * initialize all the distribution inputs for the setting panel
	 */
	simulationNumberText = new JTextField(SIMULATION_TEXT);
	simulationNumberText.setAlignmentX(Component.LEFT_ALIGNMENT);
	simulationNumberText.setMaximumSize(new Dimension(200,200));
	settingPanel.add(simulationNumberText);
	settingPanel.add(Box.createVerticalGlue());
	//label to indicate the sorting criteria combo box
	sortCriteriaLabel = new JLabel(SORT_CRITERIA_TEXT);
	sortCriteriaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	settingPanel.add(sortCriteriaLabel);
	
	// set sorting criteria combo box
	String[] sortingList = {"BIC","AIC"};
	// set the default sorting criteria to BIC
	sortCriteriaCombo = new JComboBox(sortingList);
	sortCriteriaCombo.setSelectedIndex(0);
	sortCriteriaCombo.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		// do nothing first
	    }	    
	});
	sortCriteriaCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
	sortCriteriaCombo.setMaximumSize(new Dimension(150,100));
	settingPanel.add(sortCriteriaCombo);
	settingPanel.add(Box.createVerticalGlue());
	
	//label to indicate the distribution type combo box
	distTypeLabel = new JLabel(DIST_TYPE_TEXT);
	distTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	settingPanel.add(distTypeLabel);
	
	//set the distribution type combo box
	String[] distTypeList = {"Continuous","Discrete"};
	distTypeCombo = new JComboBox(distTypeList);
	//set the default distribution type to continuous distribution
	distTypeCombo.setSelectedIndex(0);
	distTypeCombo.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	    }
	});
	distTypeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
	distTypeCombo.setMaximumSize(new Dimension(150,100));
	settingPanel.add(distTypeCombo);
	settingPanel.add(Box.createVerticalGlue());
	
	//the the finish button
	doneButton = new JButton(DONE_TEXT);
	doneButton.addActionListener(new ActionListener(){

	    @Override
	    public void actionPerformed(ActionEvent e) {		
		//1. send the information to the model
		//fill in the code later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//2. disable another tab
		parentPanel.setEnabledAt(1, false);
	    }
	});
	
	doneButton.setAlignmentX(Component.LEFT_ALIGNMENT);
	settingPanel.add(doneButton);
	
	this.add(settingPanel, BorderLayout.EAST);
	
	//set-up the display panel
	displayPanel = new JPanel(new BorderLayout());
	add(displayPanel, BorderLayout.CENTER);
    }
        
}
