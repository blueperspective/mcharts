package com.redorb.mcharts.ui.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.TimePeriodValuesCollection;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public class LinesChart implements IChartCreator {

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
		
		TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();
		
		DatasetComputer.compute2DDataset(tree, dataset);

		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title,
				"",
				I18n.getMessage("chart.legend.amount"),
				dataset,
				true,
				true,
				false);

		XYPlot plot = (XYPlot) chart.getPlot();

		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelAngle(Math.PI / 3);

		StandardXYItemRenderer renderer = new StandardXYItemRenderer();
		plot.setRenderer(renderer);
		renderer.setPlotLines(true);

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
