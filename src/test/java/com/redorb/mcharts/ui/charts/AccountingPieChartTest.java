package com.redorb.mcharts.ui.charts;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.TableOrder;

public class AccountingPieChartTest {

	public static void main(String[] args) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(155.0, "Avril", "Alimentation");
		dataset.addValue(165.0, "Avril", "Divers");
		dataset.addValue(84.0, "Avril", "Automobile");
		dataset.addValue(544.0, "Avril", "Soins");
		dataset.addValue(55.6, "Avril", "Train");

		dataset.addValue(105.0, "Mai", "Alimentation");
		dataset.addValue(125.0, "Mai", "Divers");
		dataset.addValue(150.0, "Mai", "Automobile");
		dataset.addValue(504.0, "Mai", "Soins");
		dataset.addValue(155.6, "Mai", "Train");

		JFreeChart chart = ChartFactory.createMultiplePieChart(
				"Multiple Pie Chart",  // chart title
				dataset,               // dataset
				TableOrder.BY_ROW,
				true,                  // include legend
				true,
				false);

		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setPreferredSize(new Dimension(500, 400));

		JFrame mainFrame = new JFrame();
		mainFrame.setContentPane(chartPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(400, 300);
		mainFrame.setVisible(true);
	}
}
