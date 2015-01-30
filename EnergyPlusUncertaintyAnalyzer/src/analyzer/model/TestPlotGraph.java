package analyzer.model;


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
//		analyzeResult.setStartYear(2013);

		int numVars = analyzeResult.getVariableLength();
		int numMonths = analyzeResult.getKeysLength();
		Statistics statistics = new Statistics();
		int startYear = analyzeResult.getStartYear();
		String startMonth = analyzeResult.getKey(0);
		for (int i = 0; i < numVars; i++) {
			double[] averages = new double[numMonths];
			for (int j = 0; j < numMonths; j++) {
				String currentKey = analyzeResult.getKey(j);
				double[] temp = analyzeResult.getData(currentKey, i);
				averages[j] = statistics.calculateMean(temp);
			}
			String currentVariable = analyzeResult.getVariable(i);
			
			PlotGraph plotGraph = new PlotGraph(currentVariable,
					averages, startMonth, numMonths, startYear);
			plotGraph.pack();
			RefineryUtilities.positionFrameRandomly(plotGraph);
			plotGraph.setVisible(true);
		}

	}
}
