package analyzer.listeners;

import java.util.List;

import org.jfree.chart.ChartPanel;

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

}
