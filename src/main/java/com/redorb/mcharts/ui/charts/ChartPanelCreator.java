package com.redorb.mcharts.ui.charts;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.data.aggregation.aggregator.GlobalAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.criteria.filter.CriteriaFilter;
import com.redorb.mcharts.perf.Perf;

@SuppressWarnings("serial")
public class ChartPanelCreator extends JPanel {

	/*
	 * Attributes
	 */
	
	private final Logger log = LoggerFactory.getLogger(ChartPanelCreator.class);

	/** chart container */
	protected Container container = null;

	/** criteria for selection */
	protected IChart chart = null;

	/** result of filtering */
	protected List<Transaction> filteredTransactions = null;

	/** result of aggregation */
	protected List<AccountingTree> trees = new ArrayList<AccountingTree>();

	/*
	 * Ctors
	 */

	public ChartPanelCreator(IChart chart) {
		this.chart = chart;

		setLayout(new GridBagLayout());
	}

	/*
	 * Operations
	 */

	/**
	 * @see ui.charts.IChart#compute()
	 */
	public void compute() throws Exception {

		// selection and aggregation
		
		log.info("Compute chart panel");

		filteredTransactions = CriteriaFilter.filter(
				Core.getInstance().getTransactions(), chart.getCriteria());

		chart.getAggregator().aggregate(filteredTransactions);

		if (chart.getAggregator() instanceof GlobalAggregator) {

			GlobalAggregator agg = (GlobalAggregator) chart.getAggregator();

			AccountingTree tree = agg.getTree();

			if (chart.getRestriction() != null) {
				tree = chart.getRestriction().computeRestriction(tree);
			}

			Perf.getInstance().takeMeasure("start chart creation");

			trees.add(tree);
			JFreeChart jfchart = chart.getChartCreator().createChart(tree, "");
			ChartPanel chartPanel = new ChartPanel(jfchart);			
			add(chartPanel, new GBC(0, 0, GBC.BOTH));

			Perf.getInstance().takeMeasure("end chart creation");
		}
		else if (chart.getAggregator() instanceof KindAggregator) {

			KindAggregator agg = (KindAggregator) chart.getAggregator();

			AccountingTree incomeTree = agg.getIncomeTree();
			AccountingTree outcomeTree = agg.getOutcomeTree();

			// apply optional restriction

			if (chart.getRestriction() != null) {
				incomeTree = chart.getRestriction().computeRestriction(incomeTree);
				outcomeTree = chart.getRestriction().computeRestriction(outcomeTree);
			}

			Perf.getInstance().takeMeasure("start chart creation");

			trees.add(incomeTree);
			JFreeChart incomeChart = chart.getChartCreator().createChart(incomeTree, I18n.getMessage("chart.legend.income"));
			add(new ChartPanel(incomeChart), new GBC(0, 0, GBC.BOTH));

			trees.add(outcomeTree);
			JFreeChart outcomeChart = chart.getChartCreator().createChart(outcomeTree, I18n.getMessage("chart.legend.outcome"));
			add(new ChartPanel(outcomeChart), new GBC(1, 0, GBC.BOTH));

			Perf.getInstance().takeMeasure("end chart creation");
		}
	}

	/*
	 * Getters/Setters
	 */

	/** @return the filteredTransactions
	 */
	public List<Transaction> getFilteredTransactions() {
		return filteredTransactions;
	}

	/**
	 * @return the chart
	 */
	public IChart getChart() {
		return chart;
	}
}
