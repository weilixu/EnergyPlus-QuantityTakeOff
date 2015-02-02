package analyzer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Month;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public class PlotGraph extends ApplicationFrame {
    private JFreeChart chart;

	public PlotGraph(String title, double[] mean, double[] lower,
			double[] upper, String month, int numMonths, int year) {
		super(title);
		XYDataset dataset = createDataset(mean, lower, upper, month,
				numMonths, year);
		chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
	}
	
	public JFreeChart getChart(){
	    return chart;
	}

	private XYDataset createDataset(double[] mean, double[] lower,
			double[] upper, String month, int numMonths, int year) {
		
		TimeSeries tsLower = new TimeSeries("lower");
		TimeSeries tsUpper = new TimeSeries("upper");
		TimeSeries series = new TimeSeries("mean");

		Calendar cal = Calendar.getInstance();
		Month current = null;

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
		return ChartFactory.createTimeSeriesChart(getTitle(),
				"Time [Month]", "Energy Consumption [J]",
				dataset, true, false, false);
	}

}
