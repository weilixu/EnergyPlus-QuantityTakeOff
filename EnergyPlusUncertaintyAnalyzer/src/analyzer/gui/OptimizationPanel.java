package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jmetal.util.JMException;
import analyzer.model.Model;

public class OptimizationPanel extends JPanel{
    /*
     * Set model and files;
     */
    private final Model model;
    
    /*
     * set the execuation buttons
     */
    private final JButton startOptimization;
    
    //panel to set-up 
    private final JPanel objectivePanel;
    //panel to choose algorithm
    private final JPanel algorithmPanel;
    //panel that includes objective and algorithm
    private final JPanel inputPanel;
    
    //updates the optimization process
    private final JPanel graphPanel;
    //updates the simulation status
    private final JPanel statusPanel;
    
    //objects in objectivePanel
    private final JComboBox<String> optimizationType;
    private final JComboBox<String> objectives;
    
    //algorithmPanel;
    private final JComboBox<String> algorithms;
    
    //status
    private final JTextField statusTextField;
    
    
    //alldata related to inputs
    private final String[] optimizationTypeList = {"Single-Objective", "Multi-Objectives"};
    private final String[] objectiveList = {"EUI"};
    private final String[] algorithmList = {"GA"};
    
    public OptimizationPanel(Model m){
	setLayout(new BorderLayout());
	model = m;
	
	inputPanel = new JPanel();
	inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.Y_AXIS));
	inputPanel.setBackground(Color.WHITE);
	
	//set-up objectivePanel
	objectivePanel = new JPanel();
	objectivePanel.setLayout(new BoxLayout(objectivePanel, BoxLayout.Y_AXIS));
	
	optimizationType = new JComboBox<String>(optimizationTypeList);
	optimizationType.addItemListener(new ItemListener(){
	    @Override
	    public void itemStateChanged(ItemEvent event) {
		//set the optimizationtype
	    }
	});
	objectivePanel.add(optimizationType);
	
	objectives = new JComboBox<String>(objectiveList);
	objectives.addItemListener(new ItemListener(){

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		// set the objective
	    }
	});
	objectivePanel.add(objectives);
	inputPanel.add(objectivePanel);

	//set-up algorithm panel
	algorithmPanel = new JPanel();
	algorithmPanel.setLayout(new BoxLayout(algorithmPanel, BoxLayout.Y_AXIS));
	
	algorithms = new JComboBox<String>(algorithmList);
	algorithms.addItemListener(new ItemListener(){
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		// set-up algorithms panel
		
	    }
	});
	algorithmPanel.add(algorithms);
	inputPanel.add(algorithmPanel);
	
	startOptimization = new JButton("Optimization");
	startOptimization.addActionListener(new ActionListener(){

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    model.singleObjectiveOptimization();
		} catch (ClassNotFoundException | JMException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	});
	inputPanel.add(startOptimization);
	add(inputPanel,BorderLayout.WEST);
	
	//add graph
	graphPanel = new JPanel();
	add(graphPanel,BorderLayout.CENTER);
	
	//status
	statusPanel = new JPanel();
	statusPanel.setLayout(new BorderLayout());
	
	statusTextField = new JTextField("STATUS:");
	statusPanel.add(statusTextField, BorderLayout.CENTER);
	add(statusPanel,BorderLayout.PAGE_END);
    }
    
}
