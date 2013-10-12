package com.redorb.mcharts.ui.charts;

import java.awt.BasicStroke;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Chart to show evolution of an account balance.
 */
public class BalanceChart {

	/*
	 * Attributes
	 */
	
	private final Logger log = LoggerFactory.getLogger(BalanceChart.class);

	/*
	 * Operations
	 */

	public JFreeChart createChart(String title, Account account, Date startDate, Date endDate) {
		
		JFreeChart chart = null;
		
		try {

			Calendar calendar = Calendar.getInstance();

			calendar.setTime(startDate);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			TimePeriodValuesCollection dataset = new TimePeriodValuesCollection();		
			TimePeriodValues currentSerie = new TimePeriodValues(account.getName());

			List<Transaction> transactions = account.getTransactions();

			Date currentDate = calendar.getTime();
			int currentTransaction = 0;

			while (currentDate.compareTo(endDate) < 0
					&& currentTransaction < transactions.size()) {

				// find the last transaction of the given day

				while (currentTransaction < transactions.size() - 1
						&& transactions.get(currentTransaction).getDate().compareTo(currentDate) < 0) {
					currentTransaction++;
				}

				currentSerie.add(
						new Day(transactions.get(currentTransaction).getDate()), 
						transactions.get(currentTransaction).getBalance().doubleValue());

				calendar.add(Calendar.DATE, 1);
				currentDate = calendar.getTime();			
			}

			dataset.addSeries(currentSerie);

			chart = ChartFactory.createTimeSeriesChart(
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
			renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return chart;
	}
}
