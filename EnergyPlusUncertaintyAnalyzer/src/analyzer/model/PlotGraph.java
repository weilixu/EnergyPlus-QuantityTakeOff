package analyzer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javafx.scene.text.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PlotGraph extends ApplicationFrame {
	private JFreeChart chart;

	/**
	 * 
	 * @param title
	 *                chart title
	 * @param mean
	 *                mean consumption for each month
	 * @param lower
	 *                lower bound given by confidence interval for each
	 *                month
	 * @param upper
	 *                upper bound given by confidence interval for each
	 *                month
	 * @param month
	 *                starting month
	 * @param numMonths
	 *                number of months to plot
	 * @param year
	 *                starting year
	 */
	public PlotGraph(String title, double[] mean, double[] lower,
			double[] upper, String month, int numMonths, int year) {
		super(title);
		XYDataset dataset = createDataset(mean, lower, upper, month,
				numMonths, year);
		chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
		chartPanel.setMouseZoomable(true, false);
		TextTitle mainTitle = new TextTitle(getTitle(), new java.awt.Font(
				"SansSerif", java.awt.Font.BOLD, 15));
		chart.setTitle(mainTitle);
		setContentPane(chartPanel);
	}

	public JFreeChart getChart() {
		return chart;
	}

	/**
	 * create time series data of mean with confidence bounds specified by
	 * lower and upper
	 * 
	 * @param mean
	 *                mean consumption for each month
	 * @param lower
	 *                lower bound given by confidence interval for each
	 *                month
	 * @param upper
	 *                upper bound given by confidence interval for each
	 *                month
	 * @param month
	 *                starting month
	 * @param numMonths
	 *                number of months to plot
	 * @param year
	 *                starting year
	 * @return
	 */
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
				tsLower.add(current, lower[i]);
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

		return ChartFactory.createTimeSeriesChart("", // chart title
				"Time [Month]", // x-axis label
				"Energy [kWh]", // y-axislabel
				dataset, // data
				true, // include legend
				false, // tooltips
				false);
	}
}
