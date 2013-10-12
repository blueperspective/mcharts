package com.redorb.mcharts.ui.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public class Bar3DChart implements IChartCreator {

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

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		// fills dataset

		DatasetComputer.computeDatasetSeparate(tree, dataset);

		// credit chart

		JFreeChart chart = ChartFactory.createBarChart3D(
				title,
				"",
				I18n.getMessage("chart.legend.amount"),
				dataset, 
				PlotOrientation.VERTICAL,
				false,
				true,
				false);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

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
