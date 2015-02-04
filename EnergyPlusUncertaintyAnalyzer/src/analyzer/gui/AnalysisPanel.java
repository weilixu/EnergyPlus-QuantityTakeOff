package analyzer.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

import analyzer.graphs.GraphGenerator;
import analyzer.listeners.GraphGenerationListener;

/**
 * a TabbedPane that displays the analysis results in the form of time series
 * graphs and histogram graphs. This panel interact with the
 * <link>GraphGenerator<link> for graphs
 * 
 * @author Weili
 *
 */
public class AnalysisPanel extends JTabbedPane implements
	GraphGenerationListener {

    /*
     * set model and files
     */
    private final File resultFolder;
    private GraphGenerator graph;

    /*
     * set the time series graph panel and histo gram panel
     */
    private final JPanel timeSeriesPanel;
    private final JPanel histoGramPanel;

    /*
     * two scrolls to enable the browsing
     */
    private final JScrollPane timeSeriesScroll;
    private final JScrollPane histoGramScroll;

    /*
     * messages for the graphs
     */
    private final String TIME_TAB = "Time Series Results Display";
    private final String HIST_TAB = "Histogram Results Display";

    public AnalysisPanel(File r) {
	resultFolder = r;

	timeSeriesPanel = new JPanel(new GridLayout());
	histoGramPanel = new JPanel(new GridLayout());

	timeSeriesScroll = new JScrollPane(timeSeriesPanel);
	histoGramScroll = new JScrollPane(histoGramPanel);

	addTab(TIME_TAB, timeSeriesScroll);
	addTab(HIST_TAB, histoGramScroll);

	setMnemonicAt(0, KeyEvent.VK_1);
	setMnemonicAt(1, KeyEvent.VK_2);
    }

    /**
     * set the model in this class set the listener in the model
     * 
     * @param g
     */
    public void setGraph(GraphGenerator g) {
	graph = g;
	graph.addGraphGenerationListener(this);
    }

    /**
     * generates the graphs
     */
    public void generateGraph() {
	graph.setResults();
	graph.getTimeSeriesCharts();
	graph.getHistogramCharts();
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
}
