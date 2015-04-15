package analyzer.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.Calendar;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.BoxPlot;
import de.erichseifert.gral.plots.BoxPlot.BoxWhiskerRenderer;
import de.erichseifert.gral.plots.XYPlot.XYNavigationDirection;
import de.erichseifert.gral.plots.colors.SingleColor;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;

public class BoxPlotGraph {
    /** Define colors */
    private static final Color BORDERCOLOR = new Color(0, 0, 0);
    private static final Color BACKGROUNDCOLOR = new Color(224, 224, 224);

    private String title;
    private int numOfColumns;
    private int year;
    private DataTable data;
    private String[] axisTick;

    public BoxPlotGraph(String t, int numMonths, int year) {
	title = t;
	numOfColumns = numMonths;
	if (year < 1000) {
	    Calendar cal = Calendar.getInstance();
	    this.year = cal.get(Calendar.YEAR);
	} else {
	    this.year = year;
	}

	data = new DataTable(numOfColumns, Double.class);
    }

    public void addRows(String[] axisTick, Double[][] rowData) {
	Double[] row = null;
	for (int i = 0; i < rowData[0].length; i++) {
	    row = new Double[rowData.length];
	    for (int j = 0; j < rowData.length; j++) {
		Double result = rowData[j][i];
		row[j] = result;
	    }
	    data.add(row);
	}

	this.axisTick = axisTick;
    }

    public InteractivePanel getChart() {
	// Create new box-and-whisker plot
	DataSource boxData = BoxPlot.createBoxData(data);
	BoxPlot plot = new BoxPlot(boxData);

	// Format plot
	plot.setInsets(new Insets2D.Double(20.0, 80.0, 80.0, 20.0));

	// Format axes
	// Double tick = 0.0;
	// Double[] tickList = new Double[axisTick.length];
	// for (int i = 0; i < axisTick.length; i++) {
	// axisTick[i] = axisTick[i] + " " + year;
	// tickList[i] = tick+=1.0;
	// }

	// plot.getAxisRenderer(BoxPlot.AXIS_X).setCustomTicks(
	// DataUtils.map(tickList, axisTick));

	// Format boxes
	Stroke stroke = new BasicStroke(2.0f);
	// ScaledContinuousColorMapper colors = new
	// LinearGradient(GraphicsUtils.deriveBrighter(COLOR1),Color.WHITE);
	// colors.setRange(1.0, 3.0);
	SingleColor colors = new SingleColor(BACKGROUNDCOLOR);

	BoxWhiskerRenderer pointRenderer = (BoxWhiskerRenderer) plot
		.getPointRenderer(boxData);
	pointRenderer.setWhiskerStroke(stroke);
	pointRenderer.setBoxBorderStroke(stroke);
	pointRenderer.setBoxBackground(colors);
	pointRenderer.setBoxBorderColor(BORDERCOLOR);
	pointRenderer.setWhiskerColor(BORDERCOLOR);
	pointRenderer.setCenterBarColor(BORDERCOLOR);

	plot.getNavigator().setDirection(XYNavigationDirection.VERTICAL);

	plot.getTitle().setText(title +" (kWh)");
	plot.getAxisRenderer(BoxPlot.AXIS_X).setLabel(
		"From " + axisTick[0] + " " + year + " to "
			+ axisTick[axisTick.length - 1] + " " + year);
	plot.getAxisRenderer(BoxPlot.AXIS_X).setTickSpacing(1.0);

	InteractivePanel panel = new InteractivePanel(plot);
	return panel;
    }

}
