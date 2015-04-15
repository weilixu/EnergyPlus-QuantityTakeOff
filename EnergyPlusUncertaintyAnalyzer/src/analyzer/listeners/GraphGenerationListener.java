package analyzer.listeners;

import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import de.erichseifert.gral.ui.InteractivePanel;

/**
 * graph generation listener listens the <link>GraphGenerator<link> and get the
 * correspondent updates from the <link>GraphGenerator<link>, the updated currently includes
 * 1. a series of time series graphs generated
 * 2. a series of histogram series graphs generated
 * 
 * @author Weili
 *
 */
public interface GraphGenerationListener {
    
    /**
     * udpates the gui with the generated time series graphs
     * @param charts
     */
    public void timeSeriesGraphGenerated(List<ChartPanel> charts);
    
    /**
     * udpates the gui with the generated histogram graphs.
     * @param charts
     */
    public void histogramGraphGenerated(List<ChartPanel> charts);
    
    /**
     * updates the gui with the generated box plots
     * @param charts
     */
    public void boxPlotGraphGenerated(List<InteractivePanel> charts);

}
