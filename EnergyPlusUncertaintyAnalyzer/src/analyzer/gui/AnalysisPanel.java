package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

import analyzer.graphs.PlotHistogram;
import analyzer.lifecyclecost.squaremeterestimation.BuildingType;
import analyzer.listeners.GraphGenerationListener;
import analyzer.listeners.SquareMeterCostModelListener;
import analyzer.model.Model;

/**
 * a TabbedPane that displays the analysis results in the form of time series
 * graphs and histogram graphs. This panel interact with the
 * <link>GraphGenerator<link> for graphs
 * 
 * @author Weili
 *
 */
public class AnalysisPanel extends JTabbedPane implements
	GraphGenerationListener, SquareMeterCostModelListener{

    /*
     * set model and files
     */
    private final Model model;

    /*
     * set the time series graph panel and histo gram panel
     */
    private final JPanel timeSeriesPanel;
    private final JPanel histoPanel;
    private final JPanel histoGramPanel;

    /*
     * two scrolls to enable the browsing
     */
    private final JScrollPane timeSeriesScroll;
    private final JScrollPane histoGramScroll;
    
    private final JPanel selectionPanel;
    private final JComboBox<BuildingType> selection;
    private final JButton generateButton;
    //private PlotHistogram histogramGraph;

    /*
     * messages for the graphs
     */
    private final String TIME_TAB = "Time Series Results Display";
    private final String HIST_TAB = "Histogram Results Display";

    public AnalysisPanel(Model m) {
	model = m;
	model.addGraphGenerationListener(this);
	model.addCostModelListener(this);
	
	histoPanel = new JPanel(new BorderLayout());
	
	selectionPanel = new JPanel();
	selection = new JComboBox<BuildingType>(BuildingType.values());
	selectionPanel.add(selection);
	
	generateButton = new JButton("Generate Total Cost");
	generateButton.addActionListener(new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
		model.generateTotalCost((BuildingType)selection.getSelectedItem());
	    }
	});
	generateButton.setToolTipText("Estimate the probability of your total project cost");
	
	
	selectionPanel.add(generateButton);
	histoPanel.add(selectionPanel, BorderLayout.PAGE_START);

	timeSeriesPanel = new JPanel(new GridLayout());
	histoGramPanel = new JPanel(new GridLayout());
	histoPanel.add(histoGramPanel, BorderLayout.CENTER);

	timeSeriesScroll = new JScrollPane(timeSeriesPanel);
	histoGramScroll = new JScrollPane(histoPanel);

	addTab(TIME_TAB, timeSeriesScroll);
	addTab(HIST_TAB, histoGramScroll);

	setMnemonicAt(0, KeyEvent.VK_1);
	setMnemonicAt(1, KeyEvent.VK_2);
    }

    /**
     * generates the graphs
     */
    public void generateGraph() {
	model.generateGraphs();
    }

    @Override
    public void timeSeriesGraphGenerated(List<ChartPanel> charts) {
	timeSeriesPanel.removeAll();
	Iterator<ChartPanel> iterator = charts.iterator();
	while (iterator.hasNext()) {
	    ChartPanel tempChart = iterator.next();
	    tempChart.setBorder(BorderFactory.createLineBorder(Color.black));
	    timeSeriesPanel.add(tempChart);
	}
	timeSeriesPanel.revalidate();
	timeSeriesPanel.repaint();
    }

    @Override
    public void histogramGraphGenerated(List<ChartPanel> charts) {
	histoGramPanel.removeAll();
	Iterator<ChartPanel> iterator = charts.iterator();
	while (iterator.hasNext()) {
	    ChartPanel tempChart = iterator.next();
	    tempChart.setBorder(BorderFactory.createLineBorder(Color.black));
	    histoGramPanel.add(tempChart);
	}
	histoGramPanel.revalidate();
	histoGramPanel.repaint();
    }

    @Override
    public void costInfoUpdated(HashMap<String, double[]> costInfo) {
	System.out.println("being called");
	PlotHistogram histogramGraph = new PlotHistogram("Total Project Cost","$/m2",costInfo.get("Total project cost"));
	histoGramPanel.add(histogramGraph.createPanel());
	histoGramPanel.revalidate();
	histoGramPanel.repaint();
    }
}
