package com.redorb.mcharts.batch;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.data.aggregation.aggregator.GlobalAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.criteria.filter.CriteriaFilter;
import com.redorb.mcharts.data.restriction.IRestriction;
import com.redorb.mcharts.io.chart.ChartReader;
import com.redorb.mcharts.io.grisbi.GsbReader;

public class BatchProcessing {
	
	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(BatchProcessing.class);
	
	/*
	 * Operations
	 */
	
	public void process(final File file, final File templateChart) {

		try {

			GsbReader gsbReader = new GsbReader();
			gsbReader.read(file.getAbsolutePath());

			ChartReader templateChartReader = 
					new ChartReader(templateChart.getAbsolutePath());
			templateChartReader.read();

			IChart chart = templateChartReader.getChart();

			// criteria

			List<Transaction> transactions = CriteriaFilter.filter(
					Core.getInstance().getTransactions(), 
					chart.getCriteria());

			// aggregator

			IAggregator aggregator = chart.getAggregator();
			aggregator.aggregate(transactions);

			if (aggregator instanceof KindAggregator) {

				KindAggregator agg = (KindAggregator) aggregator;

				System.out.println(agg.getIncomeTree());

				IRestriction restriction = chart.getRestriction();
				AccountingTree restrictedTree = restriction.computeRestriction(agg.getIncomeTree());

				System.out.println(restrictedTree);
			}
			else if (aggregator instanceof GlobalAggregator) {

				GlobalAggregator agg = (GlobalAggregator) aggregator;

				System.out.println(agg.getTree());

				IRestriction restriction = chart.getRestriction();
				AccountingTree restrictedTree = restriction.computeRestriction(agg.getTree());

				System.out.println(restrictedTree);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length > 0) {
			BatchProcessing processing = new BatchProcessing();
			processing.process(new File(args[0]), new File("/home/endymion/dev/main/mcharts/src/main/resources/charts.xml"));
		}
	}
}
