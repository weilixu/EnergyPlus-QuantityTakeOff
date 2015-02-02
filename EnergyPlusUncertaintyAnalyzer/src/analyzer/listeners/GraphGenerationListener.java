package analyzer.listeners;

import java.util.List;

import org.jfree.chart.ChartPanel;


public interface GraphGenerationListener {
    
    public void graphGenerated(List<ChartPanel> charts);

}
