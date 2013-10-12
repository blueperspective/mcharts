package com.redorb.mcharts.ui.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public class Pie3DChart implements IChartCreator {

	/** nb dimensions */
	private static final int NB_DIMENSIONS = 1;
	
	@Override
	public JFreeChart createChart(AccountingTree tree, String title) {
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		DatasetComputer.computeDataset(tree, dataset);
		
		JFreeChart chart = ChartFactory.createPieChart3D(
            title, 
            dataset,
            true,
            true,
            true
        );
        
        PiePlot3D p = (PiePlot3D) chart.getPlot();
        p.setForegroundAlpha(1.0f);
        
        return chart;
	}

	@Override
	public int getNbDimensions() {
		return NB_DIMENSIONS;
	}
}
