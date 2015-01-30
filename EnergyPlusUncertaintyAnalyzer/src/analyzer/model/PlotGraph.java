package analyzer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PlotGraph extends ApplicationFrame {

	public PlotGraph(String title, double[] data, String month, int numMonths, int year) {
		super(title);
		XYDataset dataset = createDataset(data, month, numMonths, year);
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
	}

	private XYDataset createDataset(double[] data, String month, int numMonths, int year) {
		TimeSeries series = new TimeSeries("Random Data");
		
		Calendar cal = Calendar.getInstance();
		Month current = null;
		System.out.println("Current month:");
		System.out.println(month);
		try {
			cal.setTime(new SimpleDateFormat("MMM").parse(month));
			int monthInt = cal.get(Calendar.MONTH)+1;
			current = new Month(monthInt, year);
//			System.out.println(current);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for (int i = 0; i < numMonths; i++) {
			try {
				double value = data[i];						
				series.add(current, value);
				current = (Month) current.next();
			} catch (SeriesException e) {
				System.err.println("Error adding to series");
			}
			
		}
		return new TimeSeriesCollection(series);
	}

	private JFreeChart createChart(XYDataset dataset) {
		return ChartFactory.createTimeSeriesChart(getTitle(),
				"Time [Month]", "Energy Consumption [J]", dataset, false,
				false, false);
	}
	
	


}
