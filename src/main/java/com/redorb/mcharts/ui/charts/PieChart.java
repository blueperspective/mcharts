package com.redorb.mcharts.ui.charts;

import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public class PieChart implements IChartCreator {

	/*
	 * Attributes
	 */

	/** nb dimensions */
	private static final int NB_DIMENSIONS = 1;

	/*
	 * Operations
	 */

	@Override
	public JFreeChart createChart(AccountingTree tree, String title) {
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		DatasetComputer.computeDataset(tree, dataset);

		// chart

		JFreeChart chart = ChartFactory.createPieChart(
				title, 
				dataset,
				true,
				true,
				false);
		
		PiePlot p = (PiePlot) chart.getPlot();
		
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
				"{0}: {1} ({2})", 
				new DecimalFormat("0"), 
				new DecimalFormat("0%"));
		p.setLabelGenerator(gen);
		
		return chart;
	}
	
	/*
	 * Operations
	 */

	@Override
	public int getNbDimensions() {
		return NB_DIMENSIONS;
	}
}
