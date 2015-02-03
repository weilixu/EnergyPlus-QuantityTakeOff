package analyzer.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import analyzer.listeners.GraphGenerationListener;

public class AnalysisPanel extends JPanel implements GraphGenerationListener{
    
    
    private final File resultFolder;
    
    public AnalysisPanel(File r){
	resultFolder = r;
    }

    @Override
    public void graphGenerated(List<ChartPanel> charts) {
	removeAll();
	setLayout(new GridLayout(2,4));
	
	Iterator<ChartPanel> iterator = charts.iterator();
	while(iterator.hasNext()){
	    ChartPanel tempChart = iterator.next();
	    tempChart.setBorder(BorderFactory.createLineBorder(Color.black));
	    add(tempChart);
	}
	revalidate();
	repaint();
    }
}
