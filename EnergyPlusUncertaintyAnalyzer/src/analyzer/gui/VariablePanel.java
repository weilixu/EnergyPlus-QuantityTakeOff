package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import analyzer.model.Model;

public class VariablePanel extends JPanel {

    private final Model model;

    // MESSAGE or Titles
    private final String FIT_DIST_TITLE = "Fit Distribution";
    private final String MAKE_DIST_TITLE = "Make Distribution";

    // fitting panel
    private final JPanel fittingPanel;
    // variable selection panel
    private final JPanel selectPanel;
    //contains the text field specify the number of simulations
    private JPanel simulationPanel;
    private File eplusFile;
    private File parentFile;

    private JPanel variableSelectPanel = new JPanel();
    
    private JTextField simulationText;
    private JLabel idfDirLabel;
    private JTextField idfDirText;
    
    public VariablePanel(Model m, File file) {
	model = m;
	fittingPanel = new JPanel();
	fittingPanel.setLayout(new CardLayout());
	selectPanel = new JPanel();
	selectPanel.setLayout(new BorderLayout());
	eplusFile = file;
	parentFile = eplusFile.getParentFile();
	simulationPanel = initSimulationPanel();
	initPanel();
    }

    public void changeVariables(ArrayList<String> variableList, ArrayList<String> variableInfo) {

	variableSelectPanel = new JPanel();
	variableSelectPanel.setLayout(new GridLayout(variableList.size(), 0));
	
	//adding tabbedpanes and buttons to the panel
	for (int i = 0; i<variableList.size(); i++) {
	    String s = variableList.get(i);
	    JTabbedPane vbtnTP = fitPanel(s);
	    fittingPanel.add(vbtnTP, s);

	    JButton vbtn = new JButton(s);
	    vbtn.setToolTipText(variableInfo.get(i));
	    vbtn.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    CardLayout cardLayout = (CardLayout)(fittingPanel.getLayout());
		    if(e.getSource()==vbtn){
			cardLayout.show(fittingPanel, s);
		    }
		}
	    });
	    variableSelectPanel.add(vbtn);
	}
	// tabbedpanels
	add(fittingPanel, BorderLayout.CENTER);
	// revalidate();
	// repaint();

	selectPanel.removeAll();
	selectPanel.add(variableSelectPanel, BorderLayout.CENTER);

    }
    
    public String getIdfDir(){
	return idfDirText.getText();
    }
    
    public Integer getSimulationNumber(){
	return Integer.parseInt(simulationText.getText());
    }

    private void initPanel() {
	setLayout(new BorderLayout());
	add(selectPanel, BorderLayout.WEST);
	add(fittingPanel, BorderLayout.CENTER);
	add(simulationPanel,BorderLayout.PAGE_START);
    }

    // a JTabbedPane to represents two fitting method
    // once one pane is selected and inputs is complete
    // user can hit the done button to process the data
    // a inputs checking mechanism is required to be complete in the future
    private JTabbedPane fitPanel(String variableName) {
	JTabbedPane tp = new JTabbedPane();
	model.setSource(parentFile.getAbsolutePath());
	tp.addTab(FIT_DIST_TITLE, new FitDistPanel(tp,model,variableName));// index 0
	tp.addTab(MAKE_DIST_TITLE, new MakeDistPanel(tp,model,variableName));// index 1

	return tp;
    }
    
    private JPanel initSimulationPanel(){
	JPanel tempPanel = new JPanel();
	idfDirLabel = new JLabel("File: ");
	simulationText = new JTextField("Enter the Number of Simulations (>=1)");
	simulationText.setMinimumSize(new Dimension(150,15));
	simulationText.setBorder(BorderFactory.createLoweredBevelBorder());
	idfDirText = new JTextField(eplusFile.getAbsolutePath());
	idfDirText.setPreferredSize(new Dimension(150,15));
	idfDirText.setBorder(BorderFactory.createLoweredBevelBorder());
	tempPanel.add(idfDirLabel);
	tempPanel.add(idfDirText);
	tempPanel.add(simulationText);
	tempPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	return tempPanel;
    }
}
