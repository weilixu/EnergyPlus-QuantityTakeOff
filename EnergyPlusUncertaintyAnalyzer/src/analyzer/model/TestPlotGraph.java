package analyzer.model;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;

public class TestPlotGraph {

	public static void main(String[] args) {

		String source = "/Users/Adrian/Dropbox/testIDFJfreeChart/";
		String idfName = "One_Montgomery_Plaza";
		AnalyzeResult analyzeResult = new AnalyzeResult(source, idfName);
		// plot all graphs
		int numSimulation = 4;
		analyzeResult.setHeader();
		analyzeResult.setData(numSimulation);
		 analyzeResult.setStartYear(2013);
		double confidenceLevel = 0.95;
		int numVars = analyzeResult.getVariableLength();
		int numMonths = analyzeResult.getKeysLength();
		int startYear = analyzeResult.getStartYear();
		String startMonth = analyzeResult.getKey(0);
		List<JFreeChart> allCharts = new ArrayList<JFreeChart>();
		for (int i = 0; i < numVars; i++) {
			double[] averages = new double[numMonths];
			double[] lowerCI = new double[numMonths];
			double[] upperCI = new double[numMonths];
			for (int j = 0; j < numMonths; j++) {
				String currentKey = analyzeResult.getKey(j);
				double[] temp = analyzeResult.getData(
						currentKey, i);
				GenerateStatistics stat = new GenerateStatistics(
						temp);
				averages[j] = stat.getMean();

				double[] ci = stat.getCI(confidenceLevel);
				lowerCI[j] = ci[0];
				upperCI[j] = ci[1];
				// System.out.println(Arrays.toString(ci));

			}

			String currentVariable = analyzeResult.getVariable(i);
			// System.out.println(currentVariable);
			String title = "Mean " + currentVariable + " with "
					+ (confidenceLevel * 100) + "% CI";
			PlotGraph plotGraph = new PlotGraph(title, averages,
					lowerCI, upperCI, startMonth,
					numMonths, startYear);

			
			ChartPanel chart = plotGraph.getChart();
			allCharts.add(chart);
		}
		

		

		

	}
}
