package analyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import analyzer.distributions.MakeDistributionModel;
import analyzer.graphs.PlotHistogram;
import analyzer.listeners.MakeDistGraphGeneratorListener;

public class MakeDistDisplayPanel extends JPanel implements MakeDistGraphGeneratorListener{
    
    private String variable;
    private String unit;
    private final MakeDistributionModel model;
    
    
    public MakeDistDisplayPanel(MakeDistributionModel m, String v, String u){
	model = m;
	model.addMakeDistGraphGeneratorListener(this);
	setLayout(new BorderLayout());
	setBackground(Color.WHITE);
	variable =v;
	unit = u;
    }

    @Override
    public void onUpdatedDistGenerated(double[] distSamples) {
	removeAll();
	PlotHistogram p = new PlotHistogram(variable,unit,distSamples);
	add(p.createPanel(),BorderLayout.CENTER);
	revalidate();
	repaint();
    }

}
