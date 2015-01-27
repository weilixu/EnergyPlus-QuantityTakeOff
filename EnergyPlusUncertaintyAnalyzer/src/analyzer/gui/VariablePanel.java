package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import analyzer.model.Model;

public class VariablePanel extends JPanel implements ItemListener {

    private final Model model;

    // MESSAGE or Titles
    private final String VARIABLE_TITLE = "   Varibles";
    private final String FIT_DIST_TITLE = "Fit Distribution";
    private final String MAKE_DIST_TITLE = "Make Distribution";

    // Lable
    private final JLabel variableLabel;

    // fitting panel
    private final JPanel fittingPanel;
    // variable selection panel
    private final JPanel selectPanel;

    private JPanel variableSelectPanel = new JPanel();

    public VariablePanel(Model m) {
	model = m;
	variableLabel = new JLabel(VARIABLE_TITLE);
	fittingPanel = new JPanel();
	fittingPanel.setLayout(new CardLayout());
	selectPanel = new JPanel();
	selectPanel.setLayout(new BorderLayout());
	initPanel();
    }

    public void changeVariables(ArrayList<String> variableList) {
	// dead code, DEMONSTRATION ONLY!!!
	variableList = new ArrayList<String>();
	variableList.add("abc");
	variableList.add("def");
	variableList.add("ghi");
	variableList.add("jkl");
	variableList.add("mno");
	variableList.add("pqr");
	variableList.add("stu");
	variableList.add("vwx");


	variableSelectPanel = new JPanel();
	variableSelectPanel.setLayout(new GridLayout(variableList.size(), 0));

	for (String s : variableList) {
	    JButton vbtn = new JButton(s);
	    JTabbedPane vbtnTP = fitPanel();
	    vbtn.addItemListener(this);
	    variableSelectPanel.add(vbtn);

	    fittingPanel.add(vbtnTP, s);
	    // vbtn.addActionListener(new ActionListener() {
	    // @Override
	    // public void actionPerformed(ActionEvent e) {
	    // fittingPanel.removeAll();
	    // fittingPanel.add(vbtnTP);
	    // revalidate();
	    // repaint();
	    // }
	    // });
	}
	// tabbedpanels
	add(fittingPanel, BorderLayout.CENTER);
	revalidate();
	repaint();

	selectPanel.removeAll();
	selectPanel.add(variableSelectPanel, BorderLayout.CENTER);

    }

    public void itemStateChanged(ItemEvent evt) {
	CardLayout c1 = (CardLayout) (fittingPanel.getLayout());
	c1.show(fittingPanel, (String) evt.getItem());
    }

    private void initPanel() {
	setLayout(new BorderLayout());
	add(variableLabel, BorderLayout.NORTH);
	add(selectPanel, BorderLayout.WEST);
	add(fittingPanel, BorderLayout.CENTER);
    }

    // a JTabbedPane to represents two fitting method
    // once one pane is selected and inputs is complete
    // user can hit the done button to process the data
    // a inputs checking mechanism is required to be complete in the future
    private JTabbedPane fitPanel() {
	JTabbedPane tp = new JTabbedPane();

	tp.addTab(FIT_DIST_TITLE, new FitDistPanel(tp));// index 0
	tp.addTab(MAKE_DIST_TITLE, new MakeDistPanel());// index 1

	// add the done button
	JButton fbtn = new JButton("Done");
	fbtn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// add check inputs warnings in the future
		if (tp.isEnabledAt(0)) {
		    tp.setEnabledAt(1, false);
		} else {
		    tp.setEnabledAt(0, false);
		}
	    }
	});
	return tp;
    }
}
