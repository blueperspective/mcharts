package com.redorb.mcharts.ui.charts;

import org.jfree.chart.JFreeChart;

import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public interface IChartCreator {

	JFreeChart createChart(AccountingTree tree, String title);
	
	int getNbDimensions();
}
