package com.redorb.mcharts.ui.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.TableOrder;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

/**
 * Chart with multiple pie chart, 2d
 */
public class MultiplePieCharts implements IChartCreator {

	/*
	 * Attributes
	 */

	/** nb dimensions */
	private static final int NB_DIMENSIONS = 2;

	/*
	 * Operations
	 */

	@Override
	public JFreeChart createChart(AccountingTree tree, String title) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		DatasetComputer.compute2DDataset(tree, dataset);

		// credit chart

		JFreeChart chart = ChartFactory.createMultiplePieChart(
				I18n.getMessage("chart.legend.income"), 
				dataset,
				TableOrder.BY_COLUMN,
				true, 
				false, 
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
