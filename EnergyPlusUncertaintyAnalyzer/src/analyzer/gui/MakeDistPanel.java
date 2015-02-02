package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;

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

import analyzer.model.Model;

public class MakeDistPanel extends JPanel {

    private final Model model;
    // set the parent tabbed pane
    private final JTabbedPane parentPane;
    private final String variable;

    // set-up two panels for this panel
    private final JPanel selectDistPanel;
    private final JPanel distInputPanel;
    private final JPanel displayPanel;
    private final JPanel comboBoxPane;

    // combo-box for distribution selection
    private final JComboBox selectBox;
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

    // distributions
    private final String EXPONENTIAL = "Exponential";
    private final String POISSON = "poisson";
    private final String REYLEIGH = "Reyleigh";
    private final String BETA = "Beta";
    private final String BINOMIAL = "Binomial";
    private final String BS = "Birnbaum-Saunders";
    private final String EV = "Extreme Value";
    private final String GAMMA = "Gamma";
    private final String IG = "Inverse Gaussian";
    private final String LOG = "Logistic";
    private final String LOGL = "Loglogistic";
    private final String LOGN = "LogNormal";
    private final String NAKA = "Nakagami";
    private final String NEG_BI = "Negative Binomial";
    private final String NORMAL = "Normal";
    private final String RICIAN = "Rician";
    private final String UNIFORM = "Uniform";
    private final String WEIBULL = "Weibull";

    // elements
    private final String M = "mu";
    private final String L = "lambda";
    private final String B = "b";
    private final String A = "a";
    private final String N = "N";
    private final String P = "p";
    private final String BA = "beta";
    private final String G = "gamma";
    private final String SI = "sigma";
    private final String O = "omega";
    private final String R = "R";
    private final String S = "s";
    private final String LO = "lower";
    private final String UP = "upper";

    public MakeDistPanel(JTabbedPane tp, Model m, String v) {
	model = m;
	parentPane = tp;
	variable = v;
	setLayout(new BorderLayout());

	/*
	 * setting-up the distribution selection panel
	 */
	selectDistPanel = new JPanel(new BorderLayout());
	Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	TitledBorder title = BorderFactory.createTitledBorder(raisedbevel,
		"Setting");
	selectDistPanel.setBorder(title);

	/*
	 * set up the cards panel
	 */
	distInputPanel = new JPanel(new CardLayout());
	distInputPanel.add(setExponential(), EXPONENTIAL);
	distInputPanel.add(setPossion(), POISSON);
	distInputPanel.add(setReyleigh(), REYLEIGH);
	distInputPanel.add(setBeta(), BETA);
	distInputPanel.add(setBinomial(), BINOMIAL);
	distInputPanel.add(setBirSau(), BS);
	distInputPanel.add(setExtreme(), EV);
	distInputPanel.add(setGamma(), GAMMA);
	distInputPanel.add(setInverse(), IG);
	distInputPanel.add(setLogistic(), LOG);
	distInputPanel.add(setLogLogistic(), LOGL);
	distInputPanel.add(setLogNormal(), LOGN);
	distInputPanel.add(setNaka(), NAKA);
	distInputPanel.add(setNegative(), NEG_BI);
	distInputPanel.add(setNormal(), NORMAL);
	distInputPanel.add(setRician(), RICIAN);
	distInputPanel.add(setUniform(), UNIFORM);
	distInputPanel.add(setWeibull(), WEIBULL);

	// set up the comboBox
	comboBoxPane = new JPanel();
	String comboBoxItems[] = { EXPONENTIAL, POISSON, REYLEIGH, BETA,
		BINOMIAL, BS, EV, GAMMA, IG, LOG, LOGL, LOGN, NAKA, NEG_BI,
		NORMAL, RICIAN, UNIFORM, WEIBULL };
	selectBox = new JComboBox(comboBoxItems);
	selectBox.setEditable(false);
	selectBox.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout) (distInputPanel.getLayout());
		cl.show(distInputPanel, (String) evt.getItem());
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

	// add all the inputs to the selection panel
	selectDistPanel.add(distInputPanel, BorderLayout.CENTER);
	selectDistPanel.add(comboBoxPane, BorderLayout.PAGE_START);

	// add the selection panel at north
	add(selectDistPanel, BorderLayout.NORTH);

	// set-up the display panel
	displayPanel = new ImageDisplayPanel(model,variable);
	add(displayPanel, BorderLayout.CENTER);

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

    private JPanel setReyleigh() {
	JPanel reyPanel = new JPanel();
	JTextField text = new JTextField(B);
	text.setToolTipText("b - Defining parameter");
	text.setPreferredSize(new Dimension(150, 25));
	reyPanel.add(text);
	reyPanel.add(setDoneButton(text));
	reyPanel.add(setRefreshButton());
	return reyPanel;
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

    private JPanel setBirSau() {
	JPanel bsPanel = new JPanel();
	JTextField aText = new JTextField(BA);
	aText.setToolTipText("beta - Scale parameter");
	JTextField bText = new JTextField(G);
	bText.setToolTipText("gamma - Shape parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	bsPanel.add(aText);
	bsPanel.add(bText);
	bsPanel.add(setDoneButton(aText, bText));
	bsPanel.add(setRefreshButton());
	return bsPanel;
    }

    private JPanel setExtreme() {
	JPanel exPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Location parameter");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Scale parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	exPanel.add(aText);
	exPanel.add(bText);
	exPanel.add(setDoneButton(aText, bText));
	exPanel.add(setRefreshButton());
	return exPanel;
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

    private JPanel setInverse() {
	JPanel invPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Scale parameter");
	JTextField bText = new JTextField(L);
	bText.setToolTipText("lambda - Shape parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	invPanel.add(aText);
	invPanel.add(bText);
	invPanel.add(setDoneButton(aText, bText));
	invPanel.add(setRefreshButton());
	return invPanel;
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

    private JPanel setLogLogistic() {
	JPanel logLogicPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Mean");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Scale parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	logLogicPanel.add(aText);
	logLogicPanel.add(bText);
	logLogicPanel.add(setDoneButton(aText, bText));
	logLogicPanel.add(setRefreshButton());
	return logLogicPanel;
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

    private JPanel setNegative() {
	JPanel negativePanel = new JPanel();
	JTextField aText = new JTextField(R);
	aText.setToolTipText("R - Number of successes");
	JTextField bText = new JTextField(P);
	bText.setToolTipText("p - probability of success");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	negativePanel.add(aText);
	negativePanel.add(bText);
	negativePanel.add(setDoneButton(aText, bText));
	negativePanel.add(setRefreshButton());
	return negativePanel;
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

    private JPanel setRician() {
	JPanel ricianPanel = new JPanel();
	JTextField aText = new JTextField(S);
	aText.setToolTipText("s - Noncentrality parameter");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Scale parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	ricianPanel.add(aText);
	ricianPanel.add(bText);
	ricianPanel.add(setDoneButton(aText, bText));
	ricianPanel.add(setRefreshButton());
	return ricianPanel;
    }

    private JPanel setUniform() {
	JPanel uniformPanel = new JPanel();
	JTextField aText = new JTextField(LO);
	aText.setToolTipText("lower - Lower parameter");
	JTextField bText = new JTextField(UP);
	bText.setToolTipText("upper - Upper parameter");
	aText.setPreferredSize(new Dimension(150, 25));
	bText.setPreferredSize(new Dimension(150, 25));
	uniformPanel.add(aText);
	uniformPanel.add(bText);
	uniformPanel.add(setDoneButton(aText, bText));
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
		double[] distrParm = new double[1];
		try {
		    done.setEnabled(false);
		    distrParm[0] = Double.parseDouble(field.getText());
		    // disable the selection
		    model.setVariable(variable);
		    model.generateRV(selectBox.getSelectedItem().toString(),
			    distrParm, lowerText.getText(), upperText.getText());
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
		double[] distrParm = new double[2];
		try {
		    distrParm[0] = Double.parseDouble(field1.getText());
		    distrParm[1] = Double.parseDouble(field2.getText());
		    done.setEnabled(false);
		    // disable the selection
		    model.setVariable(variable);
		    model.generateRV(selectBox.getSelectedItem().toString(),
			    distrParm, lowerText.getText(), upperText.getText());
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
		model.setVariable(variable);
		model.refreshGeneration();
	    }
	});
	return refresh;
    }

    // for error info
    private static void showErrorDialog(Component c, String title, String msg) {
	JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
    }

}
