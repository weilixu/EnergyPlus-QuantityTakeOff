package analyzer.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import analyzer.gui.AnalyzerInterface;
import analyzer.model.Model;

public class Analyzer extends JPanel{

    private static final String TOOL_NAME = "EnergyPlus Uncertainty Analyzer";
    // build the parent frame to determine which functionality you want to
    // choose
    private final JFrame parentFrame;
    // A label to tell user to enter EnergyPlus file
    private final JLabel loadEnergyPlusLabel;
    private final String LOAD_TEXT = " Please Enter Your EnergyPlus Directory here";
    // A text field to allow user to enter Energyplus directory
    private final JTextField energyPlusDir;
    // two buttons to select interfaces
    private final JButton createIDFButton;
    private final String CREATE_IDF = "Create IDF Files";
    private final JButton analyzeResultButton;
    private final String ANALYZE_IDF = "Analyze Results";

    // Messages
    private final String ERROR_INVALID_FILE = "Invalid File Direcotry!";

    public Analyzer(JFrame frame) {
	this.parentFrame = frame;

	loadEnergyPlusLabel = new JLabel(LOAD_TEXT);
	energyPlusDir = new JTextField(20);

	createIDFButton = new JButton(CREATE_IDF);
	createIDFButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		String directory = energyPlusDir.getText();
		File eplusFile = new File(directory);
		if (!eplusFile.isFile() && !eplusFile.isDirectory()) {
		    loadEnergyPlusLabel.setText(ERROR_INVALID_FILE);
		} else {
		    try {
			startCreateIDFInterface(eplusFile);
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		}
	    }
	});

	analyzeResultButton = new JButton(ANALYZE_IDF);
	analyzeResultButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		String directory = energyPlusDir.getText();
		File eplusFile = new File(directory);
		if (!eplusFile.isFile() && !eplusFile.isDirectory()) {
		    loadEnergyPlusLabel.setText(ERROR_INVALID_FILE);
		} else {
		    try {
			startAnalyzeDataInterface(eplusFile);
		    } catch (IOException e2) {
			loadEnergyPlusLabel.setText(ANALYZE_IDF);
			e2.printStackTrace();
		    }
		}
	    }
	});

	JPanel setupPanel = new JPanel();
	setupPanel.setLayout(new BorderLayout());
	setupPanel.add(energyPlusDir, BorderLayout.NORTH);
	setupPanel.add(createIDFButton, BorderLayout.CENTER);
	setupPanel.add(analyzeResultButton, BorderLayout.SOUTH);

	add(setupPanel);
	setVisible(true);

    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame frame = new JFrame("Tool Set Up");
		frame.add(new Analyzer(frame));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	    }
	});
    }

    private void startCreateIDFInterface(File file) throws IOException {
	parentFrame.remove(this);
	Model model = new Model();
	AnalyzerInterface analysis = new AnalyzerInterface(model, parentFrame, file);
    }

    private void startAnalyzeDataInterface(File file) throws IOException {
	parentFrame.remove(this);
	Model model = new Model();
	AnalyzerInterface analysis = new AnalyzerInterface(model, parentFrame, file);
    }
}
