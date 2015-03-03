package analyzer.graphs;

import java.awt.Dimension;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;





import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class PlotGraph {
    private ChartPanel chart;
    private String title;

	public PlotGraph(String t, double[] mean, double[] lower,
			double[] upper, String month, int numMonths, int year) {
		title = t;
		XYDataset dataset = createDataset(mean, lower, upper, month,
				numMonths, year);
		JFreeChart jChart = createChart(dataset);
		
		XYPlot xyPlot=(XYPlot)jChart.getPlot();
		NumberAxis range = (NumberAxis)xyPlot.getRangeAxis();
		range.setLowerBound(0);
		
		
		chart = new ChartPanel(jChart);
		chart.setPreferredSize(new Dimension(560, 370));
		chart.setMouseZoomable(true, false);
	}
	
	public ChartPanel getChart(){
	    return chart;
	}

	private XYDataset createDataset(double[] mean, double[] lower,
			double[] upper, String month, int numMonths, int year) {
		
		TimeSeries tsLower = new TimeSeries("lower");
		TimeSeries tsUpper = new TimeSeries("upper");
		TimeSeries series = new TimeSeries("mean");

		Calendar cal = Calendar.getInstance();
		Month current = null;
		
		//year threshold set to 1000
		if(year<=1000){
		    year = cal.get(Calendar.YEAR);
		}

		try {
			cal.setTime(new SimpleDateFormat("MMM").parse(month));
			int monthInt = cal.get(Calendar.MONTH) + 1;
			current = new Month(monthInt, year);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < numMonths; i++) {
			try {
				series.add(current, mean[i]);
				tsLower.add(current,lower[i]);
				tsUpper.add(current, upper[i]);
				current = (Month) current.next();
			} catch (SeriesException e) {
				System.err.println("Error adding to series");
			}
		}
		TimeSeriesCollection allDataSets = new TimeSeriesCollection();
		allDataSets.addSeries(series);
		allDataSets.addSeries(tsLower);
		allDataSets.addSeries(tsUpper);
		
		return allDataSets;
	}

	private JFreeChart createChart(XYDataset dataset) {
		return ChartFactory.createTimeSeriesChart(title,
				"Time [Month]", "Energy [kWh]",
				dataset, true, false, false);
	}
}
