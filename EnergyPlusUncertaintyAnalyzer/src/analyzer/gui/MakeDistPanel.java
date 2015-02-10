package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import analyzer.distributions.DistributionType;
import analyzer.distributions.MakeDistributionModel;
import analyzer.recommender.Recommender;

public class MakeDistPanel extends JPanel {

    private final MakeDistributionModel model;
    private final Recommender recommender;
    // set the parent tabbed pane
    private final JTabbedPane parentPane;
    private final String object;
    private final String input;
    private final String unit;

    // set-up two panels for this panel
    private final JPanel selectDistPanel;
    private final JPanel distInputPanel;
    private final JPanel displayPanel;
    private final JPanel comboBoxPane;
    private JPanel recommendPanel;

    // combo-box for distribution selection
    private final JComboBox<DistributionType> selectBox;
    private final JLabel lowerLabel;
    private final JTextField lowerText;
    private final JLabel upperLabel;
    private final JTextField upperText;

    // text
    private final String LOWER_TEXT = "0";
    private final String LOWER_TIP = "lower bound where the generated random variables will be truncated to";
    private final String UPPER_TEXT = "1";
    private final String UPPER_TIP = "upper bound where the generated random variables will be truncated to";
    private final String LOWER_LABEL_TEXT = "Lower";
    private final String UPPER_LABEL_TEXT = "Upper";
    private final String VARIABLE_SET;

    // elements
    private final String M = "mu";
    private final String L = "lambda";
    private final String B = "b";
    private final String A = "a";
    private final String N = "N";
    private final String P = "p";
    private final String SI = "sigma";
    private final String O = "omega";
    private final String LO = "lower";
    private final String UP = "upper";

    public MakeDistPanel(JTabbedPane tp, MakeDistributionModel m,
	    Recommender r, String s, String o, String i, String u) {
	model = m;
	recommender = r;
	parentPane = tp;
	VARIABLE_SET = s;
	object = o;
	input = i;
	unit = u;
	setLayout(new BorderLayout());

	/*
	 * setting-up the distribution selection panel
	 */
	selectDistPanel = new JPanel(new BorderLayout());
	Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	TitledBorder title = BorderFactory.createTitledBorder(raisedbevel,
		VARIABLE_SET);
	selectDistPanel.setBorder(title);

	// set up the comboBox
	comboBoxPane = new JPanel();
	selectBox = new JComboBox<DistributionType>(DistributionType.values());
	selectBox.setEditable(false);
	selectBox.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout) (distInputPanel.getLayout());
		DistributionType type = (DistributionType) evt.getItem();
		//if uniform, then things is a little different
		if (type.toString().equals("Uniform Distribution (Discrete)")
			|| type.toString().equals(
				"Uniform Distribution (Continuous)")) {
		    lowerText.setEnabled(false);
		    upperText.setEnabled(false);
		} else {
		    lowerText.setEnabled(true);
		    upperText.setEnabled(true);
		}
		cl.show(distInputPanel, type.toString());
	    }
	});
	comboBoxPane.add(selectBox);

	// setting the lower boundary label and text field
	lowerLabel = new JLabel(LOWER_LABEL_TEXT);
	comboBoxPane.add(lowerLabel);

	lowerText = new JTextField(LOWER_TEXT);
	lowerText.setBorder(BorderFactory.createLoweredBevelBorder());
	lowerText.setPreferredSize(new Dimension(50, 25));
	lowerText.setToolTipText(LOWER_TIP);
	comboBoxPane.add(lowerText);

	// setting the upper boundary label and text field
	upperLabel = new JLabel(UPPER_LABEL_TEXT);
	comboBoxPane.add(upperLabel);

	upperText = new JTextField(UPPER_TEXT);
	upperText.setBorder(BorderFactory.createLoweredBevelBorder());
	upperText.setPreferredSize(new Dimension(50, 25));
	upperText.setToolTipText(UPPER_TIP);
	comboBoxPane.add(upperText);

	/*
	 * set up the cards panel
	 */
	distInputPanel = new JPanel(new CardLayout());
	distInputPanel.add(setExponential(), DistributionType.EXPON.toString());
	distInputPanel.add(setPossion(), DistributionType.POISSON.toString());
	distInputPanel.add(setBeta(), DistributionType.BETA.toString());
	distInputPanel.add(setBinomial(), DistributionType.BINOM.toString());
	distInputPanel.add(setGamma(), DistributionType.GAMMA.toString());
	distInputPanel.add(setLogistic(), DistributionType.LOGISTIC.toString());
	distInputPanel.add(setLogNormal(),
		DistributionType.LOGNORMAL.toString());
	distInputPanel.add(setNaka(), DistributionType.NAKAGAMI.toString());
	distInputPanel.add(setNormal(), DistributionType.NORMAL.toString());
	distInputPanel.add(setUniformC(), DistributionType.UNIFORMC.toString());
	distInputPanel.add(setUniformD(), DistributionType.UNIFORMD.toString());
	distInputPanel.add(setWeibull(), DistributionType.WEIBULL.toString());

	// add all the inputs to the selection panel
	selectDistPanel.add(distInputPanel, BorderLayout.CENTER);
	selectDistPanel.add(comboBoxPane, BorderLayout.PAGE_START);

	// add the selection panel at north
	add(selectDistPanel, BorderLayout.NORTH);

	// set-up the display panel
	displayPanel = new MakeDistDisplayPanel(model, input, unit);
	add(displayPanel, BorderLayout.CENTER);

	try {
	    recommendPanel = new RecommenderPanel(recommender.getPartialTree(
		    object, input));
	} catch (NullPointerException e) {
	    recommendPanel = new RecommenderPanel(recommender.getTree());
	}
	add(recommendPanel, BorderLayout.LINE_START);
    }

    private JPanel setExponential() {
	JPanel expPanel = new JPanel();
	JTextField text = new JTextField(M);
	text.setToolTipText("mu - Mean Parameter");
	text.setPreferredSize(new Dimension(150, 25));
	expPanel.add(text);
	expPanel.add(setDoneButton(text));
	expPanel.add(setRefreshButton());
	return expPanel;
    }

    private JPanel setPossion() {
	JPanel posPanel = new JPanel();
	JTextField text = new JTextField(L);
	text.setToolTipText("lambda - Mean");
	text.setPreferredSize(new Dimension(150, 25));
	posPanel.add(text);
	posPanel.add(setDoneButton(text));
	posPanel.add(setRefreshButton());
	return posPanel;
    }

    private JPanel setBeta() {
	JPanel betaPanel = new JPanel();
	JTextField aText = new JTextField(A);
	aText.setToolTipText("a - First shape parameter");
	JTextField bText = new JTextField(B);
	bText.setToolTipText("b - Second shape parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	betaPanel.add(aText);
	betaPanel.add(bText);
	betaPanel.add(setDoneButton(aText, bText));
	betaPanel.add(setRefreshButton());
	return betaPanel;
    }

    private JPanel setBinomial() {
	JPanel binPanel = new JPanel();
	JTextField aText = new JTextField(N);
	aText.setToolTipText("N - Number of trials");
	JTextField bText = new JTextField(P);
	bText.setToolTipText("p - Probability of success");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	binPanel.add(aText);
	binPanel.add(bText);
	binPanel.add(setDoneButton(aText, bText));
	binPanel.add(setRefreshButton());
	return binPanel;
    }

    private JPanel setGamma() {
	JPanel gammaPanel = new JPanel();
	JTextField aText = new JTextField(A);
	aText.setToolTipText("a - Shape parameter");
	JTextField bText = new JTextField(B);
	bText.setToolTipText("b - Scale parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	gammaPanel.add(aText);
	gammaPanel.add(bText);
	gammaPanel.add(setDoneButton(aText, bText));
	gammaPanel.add(setRefreshButton());
	return gammaPanel;
    }

    private JPanel setLogistic() {
	JPanel logicPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Mean");
	JTextField bText = new JTextField(L);
	bText.setToolTipText("lambda - Shape parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	logicPanel.add(aText);
	logicPanel.add(bText);
	logicPanel.add(setDoneButton(aText, bText));
	logicPanel.add(setRefreshButton());
	return logicPanel;
    }

    private JPanel setLogNormal() {
	JPanel logNormalPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Log mean");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Log standard deviation");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	logNormalPanel.add(aText);
	logNormalPanel.add(bText);
	logNormalPanel.add(setDoneButton(aText, bText));
	logNormalPanel.add(setRefreshButton());
	return logNormalPanel;
    }

    private JPanel setNaka() {
	JPanel nakePanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Shape parameter");
	JTextField bText = new JTextField(O);
	bText.setToolTipText("omega - Scale parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	nakePanel.add(aText);
	nakePanel.add(bText);
	nakePanel.add(setDoneButton(aText, bText));
	nakePanel.add(setRefreshButton());
	return nakePanel;
    }

    private JPanel setNormal() {
	JPanel normalPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Mean");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Standard deviation");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	normalPanel.add(aText);
	normalPanel.add(bText);
	normalPanel.add(setDoneButton(aText, bText));
	normalPanel.add(setRefreshButton());
	return normalPanel;
    }

    private JPanel setUniformC() {
	JPanel uniformPanel = new JPanel();
	JTextField aText = new JTextField(LO);
	aText.setToolTipText("lower - Lower parameter");
	JTextField bText = new JTextField(UP);
	bText.setToolTipText("upper - Upper parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	uniformPanel.add(aText);
	uniformPanel.add(bText);
	uniformPanel.add(setUniformDoneButton(aText, bText));
	uniformPanel.add(setRefreshButton());
	return uniformPanel;
    }

    private JPanel setUniformD() {
	JPanel uniformPanel = new JPanel();
	JTextField aText = new JTextField(LO);
	aText.setToolTipText("lower - Lower parameter");
	JTextField bText = new JTextField(UP);
	bText.setToolTipText("upper - Upper parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	uniformPanel.add(aText);
	uniformPanel.add(bText);
	uniformPanel.add(setUniformDoneButton(aText, bText));
	uniformPanel.add(setRefreshButton());
	return uniformPanel;
    }

    private JPanel setWeibull() {
	JPanel weibullPanel = new JPanel();
	JTextField aText = new JTextField(A);
	aText.setToolTipText("a - Scale parameter");
	JTextField bText = new JTextField(B);
	bText.setToolTipText("b - Shape parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	weibullPanel.add(aText);
	weibullPanel.add(bText);
	weibullPanel.add(setDoneButton(aText, bText));
	weibullPanel.add(setRefreshButton());
	return weibullPanel;
    }

    /**
     * JButton method used for single input distribution
     * 
     * @param field
     * @return
     */
    private JButton setDoneButton(JTextField field) {
	JButton done = new JButton("Done");
	done.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		double[] distrParm = new double[4];
		try {
		    done.setEnabled(false);
		    distrParm[0] = Double.parseDouble(field.getText());
		    distrParm[2] = Double.parseDouble(lowerText.getText());
		    distrParm[3] = Double.parseDouble(upperText.getText());
		    model.setDistributionType((DistributionType) selectBox
			    .getSelectedItem());
		    // disable the selection
		    model.generateRnd(distrParm);

		    // disable the other tab
		    done.setEnabled(true);
		    selectBox.setEnabled(false);
		    parentPane.setEnabledAt(0, false);
		} catch (NumberFormatException ne) {
		    showErrorDialog(new JFrame(), "Error Found in input",
			    "Enter Integer or Double values! e.g (100)");
		    done.setEnabled(true);
		}
	    }
	});
	return done;
    }

    /**
     * JButton method used for two inputs methods
     * 
     * @param field1
     * @param field2
     * @return
     */
    private JButton setDoneButton(JTextField field1, JTextField field2) {
	JButton done = new JButton("Done");
	done.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		double[] distrParm = new double[4];
		try {
		    distrParm[0] = Double.parseDouble(field1.getText());
		    distrParm[1] = Double.parseDouble(field2.getText());
		    distrParm[2] = Double.parseDouble(lowerText.getText());
		    distrParm[3] = Double.parseDouble(upperText.getText());
		    model.setDistributionType((DistributionType) selectBox
			    .getSelectedItem());
		    // disable the selection
		    model.generateRnd(distrParm);
		    // disable the other tab
		    done.setEnabled(true);
		    selectBox.setEnabled(false);
		    parentPane.setEnabledAt(0, false);
		} catch (NumberFormatException ne) {
		    done.setEnabled(true);
		    showErrorDialog(new JFrame(), "Error Found in input",
			    "Enter Integer or Double values! e.g (100)");
		}

	    }
	});
	return done;
    }

    private JButton setUniformDoneButton(JTextField field1, JTextField field2) {
	JButton done = new JButton("Done");
	done.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		double[] distrParm = new double[4];
		try {
		    distrParm[0] = Double.parseDouble(field1.getText());
		    distrParm[1] = Double.parseDouble(field2.getText());
		    distrParm[2] = 0.0;
		    distrParm[3] = 0.0;
		    model.setDistributionType((DistributionType) selectBox
			    .getSelectedItem());
		    // disable the selection
		    model.generateRnd(distrParm);
		    // disable the other tab
		    done.setEnabled(true);
		    selectBox.setEnabled(false);
		    parentPane.setEnabledAt(0, false);
		} catch (NumberFormatException ne) {
		    done.setEnabled(true);
		    showErrorDialog(new JFrame(), "Error Found in input",
			    "Enter Integer or Double values! e.g (100)");
		}

	    }
	});
	return done;
    }

    private JButton setRefreshButton() {
	JButton refresh = new JButton("Re-do");
	refresh.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// cancel the selection of the distribution first
		// enable the select box again
		selectBox.setEnabled(true);
		// enable the other tab
		parentPane.setEnabledAt(0, true);
	    }
	});
	return refresh;
    }

    // for error info
    private static void showErrorDialog(Component c, String title, String msg) {
	JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}
