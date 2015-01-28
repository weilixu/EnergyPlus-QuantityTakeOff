package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class MakeDistPanel extends JPanel{
    //set the parent tabbed pane
    private final JTabbedPane parentPane;
    
    //set-up two panels for this panel
    private final JPanel selectDistPanel;
    private final JPanel distInputPanel;
    private final JPanel displayPanel;
    private final JPanel comboBoxPane;
    
    //combo-box for distribution selection
    private final JComboBox selectBox;
    
    //distributions
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
    
    //elements
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
	    
    
    public MakeDistPanel(JTabbedPane tp){
	parentPane = tp;
	setLayout(new BorderLayout());
	
	/*
	 * setting-up the distribution selection panel
	 */
	selectDistPanel = new JPanel(new BorderLayout());
	Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	TitledBorder title = BorderFactory.createTitledBorder(raisedbevel,"Setting");
	selectDistPanel.setBorder(title);
	
	/*
	 * set up the cards panel
	 */
	distInputPanel = new JPanel(new CardLayout());
	distInputPanel.add(setExponential(),EXPONENTIAL);
	distInputPanel.add(setPossion(), POISSON);
	distInputPanel.add(setReyleigh(), REYLEIGH);
	distInputPanel.add(setBeta(), BETA);
	distInputPanel.add(setBinomial(), BINOMIAL);
	distInputPanel.add(setBirSau(), BS);
	distInputPanel.add(setExtreme(), EV);
	distInputPanel.add(setGamma(), GAMMA);
	distInputPanel.add(setInverse(), IG);
	distInputPanel.add(setLogistic(),LOG);
	distInputPanel.add(setLogLogistic(), LOGL);
	distInputPanel.add(setLogNormal(), LOGN);
	distInputPanel.add(setNaka(),NAKA);
	distInputPanel.add(setNegative(), NEG_BI);
	distInputPanel.add(setNormal(), NORMAL);
	distInputPanel.add(setRician(),RICIAN);
	distInputPanel.add(setUniform(),UNIFORM);
	distInputPanel.add(setWeibull(),WEIBULL);
	
	//set up the comboBox
	comboBoxPane = new JPanel();
	String comboBoxItems[] = {EXPONENTIAL,POISSON,REYLEIGH,BETA,
		BINOMIAL,BS,EV,GAMMA,IG,LOG,LOGL,LOGN,NAKA,NEG_BI,NORMAL,
		RICIAN,UNIFORM,WEIBULL};
	selectBox = new JComboBox(comboBoxItems);
	selectBox.setEditable(false);
	selectBox.addItemListener(new ItemListener(){
	    @Override
	    public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout)(distInputPanel.getLayout());
		cl.show(distInputPanel, (String)evt.getItem());
	    } 
	});
	comboBoxPane.add(selectBox);

	//add all the inputs to the selection panel
	selectDistPanel.add(distInputPanel, BorderLayout.CENTER);
	selectDistPanel.add(comboBoxPane, BorderLayout.PAGE_START);
	
	
	//add the selection panel at north
	add(selectDistPanel, BorderLayout.NORTH);
	

	//set-up the display panel
	displayPanel = new JPanel(new BorderLayout());
	add(displayPanel, BorderLayout.CENTER);
	
    }
    
    private JPanel setExponential(){
	JPanel expPanel = new JPanel();
	JTextField text = new JTextField(M);
	text.setToolTipText("mu - Mean Parameter");
	text.setPreferredSize(new Dimension(150,25));
	expPanel.add(text);
	expPanel.add(setDoneButton());
	expPanel.add(setRefreshButton());
	return expPanel;
    }
    
    private JPanel setPossion(){
	JPanel posPanel = new JPanel();
	JTextField text = new JTextField(L);
	text.setToolTipText("lambda - Mean");
	text.setPreferredSize(new Dimension(150,25));
	posPanel.add(text);
	posPanel.add(setDoneButton());
	posPanel.add(setRefreshButton());
	return posPanel;
    }
    
    private JPanel setReyleigh(){
	JPanel reyPanel = new JPanel();
	JTextField text = new JTextField(B);
	text.setToolTipText("b - Defining parameter");
	text.setPreferredSize(new Dimension(150,25));
	reyPanel.add(text);
	reyPanel.add(setDoneButton());
	reyPanel.add(setRefreshButton());
	return reyPanel;
    }
    
    private JPanel setBeta(){
	JPanel betaPanel = new JPanel();
	JTextField aText = new JTextField(A);
	aText.setToolTipText("a - First shape parameter");
	JTextField bText = new JTextField(B);
	bText.setToolTipText("b - Second shape parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	betaPanel.add(aText);
	betaPanel.add(bText);
	betaPanel.add(setDoneButton());
	betaPanel.add(setRefreshButton());
	return betaPanel;
    }
    
    private JPanel setBinomial(){
	JPanel binPanel = new JPanel();
	JTextField aText = new JTextField(N);
	aText.setToolTipText("N - Number of trials");
	JTextField bText = new JTextField(P);
	bText.setToolTipText("p - Probability of success");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	binPanel.add(aText);
	binPanel.add(bText);
	binPanel.add(setDoneButton());
	binPanel.add(setRefreshButton());
	return binPanel;
    }
    
    private JPanel setBirSau(){
	JPanel bsPanel = new JPanel();
	JTextField aText = new JTextField(BA);
	aText.setToolTipText("beta - Scale parameter");
	JTextField bText = new JTextField(G);
	bText.setToolTipText("gamma - Shape parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	bsPanel.add(aText);
	bsPanel.add(bText);
	bsPanel.add(setDoneButton());
	bsPanel.add(setRefreshButton());
	return bsPanel;
    }
    
    private JPanel setExtreme(){
	JPanel exPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Location parameter");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Scale parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	exPanel.add(aText);
	exPanel.add(bText);
	exPanel.add(setDoneButton());
	exPanel.add(setRefreshButton());
	return exPanel;
    }
    
    private JPanel setGamma(){
	JPanel gammaPanel = new JPanel();
	JTextField aText = new JTextField(A);
	aText.setToolTipText("a - Shape parameter");
	JTextField bText = new JTextField(B);
	bText.setToolTipText("b - Scale parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	gammaPanel.add(aText);
	gammaPanel.add(bText);
	gammaPanel.add(setDoneButton());
	gammaPanel.add(setRefreshButton());
	return gammaPanel;
    }
    
    private JPanel setInverse(){
	JPanel invPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Scale parameter");
	JTextField bText = new JTextField(L);
	bText.setToolTipText("lambda - Shape parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	invPanel.add(aText);
	invPanel.add(bText);
	invPanel.add(setDoneButton());
	invPanel.add(setRefreshButton());
	return invPanel;
    }
    
    private JPanel setLogistic(){
	JPanel logicPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Mean");
	JTextField bText = new JTextField(L);
	bText.setToolTipText("lambda - Shape parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	logicPanel.add(aText);
	logicPanel.add(bText);
	logicPanel.add(setDoneButton());
	logicPanel.add(setRefreshButton());
	return logicPanel;
    }
    
    private JPanel setLogLogistic(){
	JPanel logLogicPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Mean");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Scale parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	logLogicPanel.add(aText);
	logLogicPanel.add(bText);
	logLogicPanel.add(setDoneButton());
	logLogicPanel.add(setRefreshButton());
	return logLogicPanel;
    }
    
    private JPanel setLogNormal(){
	JPanel logNormalPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Log mean");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Log standard deviation");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	logNormalPanel.add(aText);
	logNormalPanel.add(bText);
	logNormalPanel.add(setDoneButton());
	logNormalPanel.add(setRefreshButton());
	return logNormalPanel;
    }
    
    private JPanel setNaka(){
	JPanel nakePanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Shape parameter");
	JTextField bText = new JTextField(O);
	bText.setToolTipText("omega - Scale parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	nakePanel.add(aText);
	nakePanel.add(bText);
	nakePanel.add(setDoneButton());
	nakePanel.add(setRefreshButton());
	return nakePanel;
    }
    
    private JPanel setNegative(){
	JPanel negativePanel = new JPanel();
	JTextField aText = new JTextField(R);
	aText.setToolTipText("R - Number of successes");
	JTextField bText = new JTextField(P);
	bText.setToolTipText("p - probability of success");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	negativePanel.add(aText);
	negativePanel.add(bText);
	negativePanel.add(setDoneButton());
	negativePanel.add(setRefreshButton());
	return negativePanel;
    }
    
    private JPanel setNormal(){
	JPanel normalPanel = new JPanel();
	JTextField aText = new JTextField(M);
	aText.setToolTipText("mu - Mean");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Standard deviation");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	normalPanel.add(aText);
	normalPanel.add(bText);
	normalPanel.add(setDoneButton());
	normalPanel.add(setRefreshButton());
	return normalPanel;
    }
    
    private JPanel setRician(){
	JPanel ricianPanel = new JPanel();
	JTextField aText = new JTextField(S);
	aText.setToolTipText("s - Noncentrality parameter");
	JTextField bText = new JTextField(SI);
	bText.setToolTipText("sigma - Scale parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	ricianPanel.add(aText);
	ricianPanel.add(bText);
	ricianPanel.add(setDoneButton());
	ricianPanel.add(setRefreshButton());
	return ricianPanel;
    }
    
    private JPanel setUniform(){
	JPanel uniformPanel = new JPanel();
	JTextField aText = new JTextField(LO);
	aText.setToolTipText("lower - Lower parameter");
	JTextField bText = new JTextField(UP);
	bText.setToolTipText("upper - Upper parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	uniformPanel.add(aText);
	uniformPanel.add(bText);
	uniformPanel.add(setDoneButton());
	uniformPanel.add(setRefreshButton());
	return uniformPanel;
    }
    
    private JPanel setWeibull(){
	JPanel weibullPanel = new JPanel();
	JTextField aText = new JTextField(A);
	aText.setToolTipText("a - Scale parameter");
	JTextField bText = new JTextField(B);
	bText.setToolTipText("b - Shape parameter");
	aText.setPreferredSize(new Dimension(150,25));
	bText.setPreferredSize(new Dimension(150,25));
	weibullPanel.add(aText);
	weibullPanel.add(bText);
	weibullPanel.add(setDoneButton());
	weibullPanel.add(setRefreshButton());
	return weibullPanel;
    }
    
    private JButton setDoneButton(){
	JButton done = new JButton("Done");
	done.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		//disable the selection
		//send the data to model
		//disable the other tab
		selectBox.setEnabled(false);
		parentPane.setEnabledAt(0, false);
	    }
	});
	return done;
    }
    
    private JButton setRefreshButton(){
	JButton refresh = new JButton("Re-do");
	refresh.addActionListener(new ActionListener(){
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
    

}
