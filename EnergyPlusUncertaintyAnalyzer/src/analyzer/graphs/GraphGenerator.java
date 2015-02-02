package analyzer.graphs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import analyzer.listeners.GraphGenerationListener;
import analyzer.model.AnalyzeResult;
import analyzer.model.GenerateStatistics;
import analyzer.model.PlotGraph;

import org.jfree.chart.ChartPanel;


public class GraphGenerator {
    
    private final double CONFIDENCE_INTERVAL = 0.95;
    
    private final File resultFolder;
    private final AnalyzeResult resultAnalyzer;
    
    private int numSimulation;
    private int year;
    private int numGraph;
    private int numMonths;
    
    private List<ChartPanel> allCharts;
    
    private List<GraphGenerationListener> graphListeners;
    
    public GraphGenerator(File rf, int num, int y){
	resultFolder = rf;
	resultAnalyzer = new AnalyzeResult(resultFolder.getAbsolutePath(),"");
	numSimulation = num;
	year = y;
	numGraph = resultAnalyzer.getVariableLength();
	numMonths = resultAnalyzer.getKeysLength();
	allCharts = new ArrayList<ChartPanel>();
	
	graphListeners = new ArrayList<GraphGenerationListener>();
    }
    
    public void addGraphGenerationListener(GraphGenerationListener g){
	graphListeners.add(g);
    }
    
    public int numberOfGraphs(){
	return numGraph;
    }
    
    public void getCharts(){
	resultAnalyzer.setHeader();
	resultAnalyzer.setData(numSimulation);
	resultAnalyzer.setStartYear(year);
	String startMonth = resultAnalyzer.getKey(0);
	for(int i=0; i<numGraph; i++){
	    double[] averages = new double[numMonths];
		double[] lowerCI = new double[numMonths];
		double[] upperCI = new double[numMonths];
		for (int j = 0; j < numMonths; j++) {
			String currentKey = resultAnalyzer.getKey(j);
			double[] temp = resultAnalyzer.getData(
					currentKey, i);
			GenerateStatistics stat = new GenerateStatistics(
					temp);
			averages[j] = stat.getMean();

			double[] ci = stat.getCI(CONFIDENCE_INTERVAL);
			lowerCI[j] = ci[0];
			upperCI[j] = ci[1];
			// System.out.println(Arrays.toString(ci));

		}

		String currentVariable = resultAnalyzer.getVariable(i);
		// System.out.println(currentVariable);
		String title = "Mean " + currentVariable + " with "
				+ (CONFIDENCE_INTERVAL * 100) + "% CI";
		PlotGraph plotGraph = new PlotGraph(title, averages,
				lowerCI, upperCI, startMonth,
				numMonths, year);

		
		ChartPanel chart = plotGraph.getChart();
		allCharts.add(chart);
	}
	
	
    }
    
    private void onUpdatedGraphGenerated(){
	for(GraphGenerationListener g: graphListeners){
	    g.graphGenerated(allCharts);
	}
    }

}
