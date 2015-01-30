package analyzer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

		

		int numVars = analyzeResult.getVariableLength();
		int numMonths = analyzeResult.getKeysLength();
		Statistics statistics = new Statistics();
		int startYear = 2013;
		String startMonth = analyzeResult.getKey(0);
		for (int i = 0; i < numVars; i++) {
			double[] averages = new double[numMonths];
			for (int j = 0; j < numMonths; j++) {
				String currentKey = analyzeResult.getKey(j);
				double[] temp = analyzeResult.getData(currentKey, i);
				averages[j] = statistics.calculateMean(temp);
//				System.out.println(currentKey);
			}
			String currentVariable = analyzeResult.getVariable(i);
//			System.out.println(currentVariable);
			
			PlotGraph plotGraph = new PlotGraph(currentVariable,
					averages, startMonth, numMonths, startYear);
			plotGraph.pack();
			RefineryUtilities.positionFrameRandomly(plotGraph);
			plotGraph.setVisible(true);
		}



	}
}
