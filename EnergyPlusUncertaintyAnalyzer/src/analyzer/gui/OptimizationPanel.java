package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

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
    
    public OptimizationPanel(Model m){
	setLayout(new BorderLayout());
	model = m;
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
	
	add(startOptimization, BorderLayout.CENTER);
    }
    
}
