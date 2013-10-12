package com.redorb.mcharts.ui.charts;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class AccountingBarCharTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(155.0, "Montant", "Alimentation");
		dataset.addValue(165.0, "Montant", "Divers");
		dataset.addValue(84.0, "Montant", "Automobile");
		dataset.addValue(544.0, "Montant", "Train");
		dataset.addValue(55.6, "Montant", "Bus");

		JFreeChart chart = ChartFactory.createBarChart(
				"Test",
				"Categories",
				"Montant (€)",
				dataset, 
				PlotOrientation.VERTICAL,
				true,
				true,
				false);

		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(500, 400));

		JFrame mainFrame = new JFrame();
		mainFrame.setContentPane(chartPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
}
