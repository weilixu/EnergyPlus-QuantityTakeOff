package analyzer.graphs;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JPanel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

public class PlotHistogram {
	private static int numBins;
	private static String title;
	private ChartPanel chart;

	public PlotHistogram(String t, double[] data) {
		numBins = getNumBins(data);
		title = t;
		// JPanel jpanel = createDemoPanel(data);
		// jpanel.setPreferredSize(new Dimension(500, 270));
		// setContentPane(jpanel);
	}

	// public JPanel getHistogram() {
	// return jpanel;
	// }

	private int getNumBins(double[] data) {
		// Use Freedman-Diaconis' rule which is based on IQR to
		// calculate number of bins for histogram plot
		DescriptiveStatistics ds = new DescriptiveStatistics(data);
		double n = ds.getN();
		double iqr = ds.getPercentile(75) - ds.getPercentile(25);
		double min = ds.getMin();
		double max = ds.getMax();
		double h = 2*iqr/Math.cbrt(n);
		int numBins = (int) ((max-min)/h);
		return numBins;
	}

	private IntervalXYDataset createDataset(double[] data) {
		HistogramDataset histogramdataset = new HistogramDataset();
		histogramdataset.addSeries("simulation results", data, numBins);
		return histogramdataset;
	}

	private JFreeChart createChart(IntervalXYDataset intervalxydataset) {
		JFreeChart jfreechart = ChartFactory.createHistogram(title,
				"Frequency", "Energy [kWh]", intervalxydataset,
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setForegroundAlpha(0.85F);
		XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot
				.getRenderer();
		xybarrenderer.setDrawBarOutline(false);
		return jfreechart;
	}

	public ChartPanel createPanel(double[] data) {
		JFreeChart jfreechart = createChart(createDataset(data));
		return new ChartPanel(jfreechart);
	}
	//
	// public static void main(String args[]) throws IOException {
	// String source = "C:/Users/Weili/Desktop/New folder/Results/";
	// String idfName = "";
	// AnalyzeResult analyzeResult = new AnalyzeResult(source, idfName);
	// // plot all graphs
	// int numSimulation = 9;
	// analyzeResult.setHeader();
	// analyzeResult.setData(numSimulation);
	// int numVars = analyzeResult.getVariableLength();
	//
	// for (int i = 0; i < numVars; i++) {
	// double[] data = analyzeResult.getHistogramData(i);
	// System.out.println(Arrays.toString(data));
	// String currentVariable = analyzeResult.getVariable(i);
	// String t = "Distribution of " + currentVariable;
	// PlotHistogram histogramdemo1 = new PlotHistogram(t,
	// data, 10);
	// histogramdemo1.pack();
	// RefineryUtilities.centerFrameOnScreen(histogramdemo1);
	// histogramdemo1.setVisible(true);
	// }
	//
	// }
}
