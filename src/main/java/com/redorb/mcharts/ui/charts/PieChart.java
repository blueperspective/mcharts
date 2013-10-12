package com.redorb.mcharts.ui.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
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
