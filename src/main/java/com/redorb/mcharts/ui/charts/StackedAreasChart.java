package com.redorb.mcharts.ui.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

/**
 * 2D stacked area chart creator.
 */
public class StackedAreasChart implements IChartCreator {

	/*
	 * Attributes
	 */

	/** nb dimensions */
	private static final int NB_DIMENSIONS = 2;

	/*
	 * Operations
	 */

	public JFreeChart createChart(AccountingTree tree, String title) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		DatasetComputer.compute2DDataset(tree, dataset);

		// chart

		Object dim1 = tree.getDimensions()[0];

		JFreeChart chart = ChartFactory.createStackedAreaChart(
				title,
				dim1.toString(),
				I18n.getMessage("chart.legend.amount"),
				dataset, 
				PlotOrientation.VERTICAL,
				true,
				true,
				false);

		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryMargin(0);
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		CategoryItemRenderer r = plot.getRenderer();
		r.setBaseItemLabelsVisible(true);
		r.setBaseSeriesVisible(true);

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
