package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import analyzer.model.Model;
import analyzer.recommender.Recommender;

public class VariablePanel extends JPanel {

    private final Model model;
    private final Recommender recommender;

    // MESSAGE or Titles
    private final String FIT_DIST_TITLE = "Fit Distribution";
    private final String MAKE_DIST_TITLE = "Make Distribution";

    // fitting panel
    private final JPanel fittingPanel;
    // variable selection panel
    private JPanel selectPanel;
    // contains the text field specify the number of simulations
    // private JPanel simulationPanel;

    // all about the JList
    // private final String VARIABLE_LABEL = "Variables";
    private JList<String> variablesList;
    private DefaultListModel<String> listModel;
    private JScrollPane listScroll;
    private boolean[] enabledFlags;

    // the data retrieve from the model
    private ArrayList<String> variableList;
    private ArrayList<String> variableDescription;
    private ArrayList<String[]> variableKeySets;

    public VariablePanel(Model m) {
	model = m;
	recommender = new Recommender();
	fittingPanel = new JPanel();
	fittingPanel.setLayout(new CardLayout());
	variableList = new ArrayList<String>();
	variableKeySets = new ArrayList<String[]>();
	enabledFlags = new boolean[0];
	selectPanel = initSelectionPanel();
	initPanel();
	
	listModel = new DefaultListModel<String>();
	variablesList = new JList(listModel);
	variablesList.setFont(new Font("Helvetica", Font.BOLD, 20));
	variablesList.setCellRenderer(new DiabledItemListCellRenderer());
    }
    
    public int getVariableListSize(){
	return variableList.size();
    }

    public void changeVariables(ArrayList<String> vl, ArrayList<String[]> vk) {
	variableList = vl;
	variableDescription = new ArrayList<String>();
	variableKeySets = vk;
	
	removeDuplicatesFromList(vl, vk);

	// set the variable description for display
	for (String[] sList : variableKeySets) {
	    StringBuffer sb = new StringBuffer("SETTINGS FOR: ");
	    sb.append(sList[0]);
	    sb.append(";    INPUT: ");
	    sb.append(sList[2]);
	    variableDescription.add(sb.toString());
	}

	// set all the variables enabled
	// adding tabbedpanes and buttons to the panel
	int startIndex = enabledFlags.length;

	System.out.println("This is start: " + startIndex);
	System.out.println("This is variableSize: " + variableList.size());

	for (int i = startIndex; i < variableList.size(); i++) {
	    // variable name
	    String s = variableList.get(i);
	    // variable des;
	    String des = variableDescription.get(i);
	    // variable object
	    String o = variableKeySets.get(i)[0];
	    // variable field
	    String f = variableKeySets.get(i)[2];
	    // variable unit
	    String u = variableKeySets.get(i)[3];

	    JTabbedPane vbtnTP = fitPanel(s, des, o, f, u);
	    fittingPanel.add(vbtnTP, s);
	    
	    listModel.addElement(s);
	    
	    variablesList.addListSelectionListener(new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent evt) {
		    CardLayout cardLayout = (CardLayout) (fittingPanel
			    .getLayout());
		    String selection = variablesList.getSelectedValue()
			    .toString();
		    if (selection.equals(s)) {
			cardLayout.show(fittingPanel, selection);
		    }
		}
	    });
	}
	fittingPanel.revalidate();
	fittingPanel.repaint();
	
	//beining condition
	if (enabledFlags.length == 0 && variableList.size()==0){
	    enabledFlags = copyEnabledFlag();
	    // tabbedpanels
	    add(fittingPanel, BorderLayout.CENTER);

	    listScroll = new JScrollPane(variablesList);
	    selectPanel.add(listScroll, BorderLayout.CENTER);
	//varaibleList updated condition
	} else if(enabledFlags.length != variableList.size()){
	    //selectPanel = initSelectionPanel();
	    enabledFlags = copyEnabledFlag();
	    add(fittingPanel, BorderLayout.CENTER);
	    //listScroll.remove(variablesList);
	    variablesList.revalidate();
	    variablesList.repaint();
	    //selectPanel.add(listScroll, BorderLayout.CENTER);

	//variableList hasn't updated condition
	}else {
	    //selectPanel = initSelectionPanel();
	    //listScroll = new JScrollPane(variablesList);
	    //selectPanel.add(listScroll, BorderLayout.CENTER);
	}
	revalidate();
	repaint();
    }

    private boolean[] copyEnabledFlag() {
	boolean[] temp = new boolean[variableList.size()];
	for (int i = 0; i < temp.length; i++) {
	    temp[i] = true;
	}
	if (temp.length > enabledFlags.length) {
	    for (int i = 0; i < enabledFlags.length; i++) {
		temp[i] = enabledFlags[i];
	    }
	}
	return temp;
    }

    private void initPanel() {
	setLayout(new BorderLayout());
	add(selectPanel, BorderLayout.WEST);
	add(fittingPanel, BorderLayout.CENTER);
    }

    private JPanel initSelectionPanel() {
	JPanel temp = new JPanel(new BorderLayout());

	TitledBorder title = BorderFactory.createTitledBorder(
		BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
		"Variables");
	title.setTitleJustification(TitledBorder.RIGHT);
	temp.setBorder(title);

	return temp;
    }

    // a JTabbedPane to represents two fitting method
    // once one pane is selected and inputs is complete
    // user can hit the done button to process the data
    // a inputs checking mechanism is required to be complete in the future
    private JTabbedPane fitPanel(String variableName, String setting,
	    String object, String input, String unit) {
	JTabbedPane tp = new JTabbedPane();
	tp.addTab(FIT_DIST_TITLE, new FitDistPanel(tp, model, variableName,
		setting));// index
	// 0
	tp.addTab(MAKE_DIST_TITLE, new MakeDistPanel(tp, model, recommender,
		variableName, setting, object, input, unit));// index
	// 1
	return tp;
    }
    
    private void removeDuplicatesFromList(ArrayList<String> list, ArrayList<String[]> keySets){
	
	Set<String> tempSet = new HashSet<String>();
	ArrayList<String> tempList =new ArrayList<String>();
	ArrayList<String[]> keySetList = new ArrayList<String[]>();
	for(int i=0; i<list.size(); i++){
	    if(!tempSet.contains(list.get(i))){
		tempList.add(list.get(i));
		keySetList.add(keySets.get(i));
	    }
	    tempSet.add(list.get(i));
	}
	variableList = tempList;
	variableKeySets = keySetList;
    }

    // change the state of the flag
    public void changeFlagState(String variable) {
	int index = variableList.indexOf(variable);
	if (enabledFlags[index] == true) {
	    enabledFlags[index] = false;
	} else {
	    enabledFlags[index] = true;
	}
    }

    // cell renderer overriding
    private class DiabledItemListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
		int index, boolean isSelected, boolean cellHasFocus) {
	    Component comp = super.getListCellRendererComponent(list, value,
		    index, false, false);
	    JComponent jc = (JComponent) comp;
	    if (enabledFlags[index]) {
		if (isSelected & cellHasFocus) {
		    comp.setForeground(Color.BLACK);
		    comp.setBackground(Color.RED);
		    jc.setBorder(BorderFactory.createLoweredBevelBorder());
		} else {
		    comp.setForeground(Color.BLACK);
		    jc.setBorder(BorderFactory.createRaisedBevelBorder());
		}
		return comp;
	    }
	    comp.setEnabled(false);
	    return comp;
	}
    }
}
