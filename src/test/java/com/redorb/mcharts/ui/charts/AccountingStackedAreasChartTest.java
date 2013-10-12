package com.redorb.mcharts.ui.charts;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class AccountingStackedAreasChartTest {

	/*
	 * Getters/Setters
	 */
	
	public static void main(String[] args) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(155.0, "Alimentation", "Avril");
		dataset.addValue(165.0, "Divers", "Avril");
		dataset.addValue(84.0, "Automobile", "Avril");
		dataset.addValue(544.0, "Avril", "Avril");
		dataset.addValue(55.6, "Train", "Avril");
		
		dataset.addValue(105.0, "Alimentation", "Mai");
		dataset.addValue(125.0, "Divers", "Mai");
		dataset.addValue(150.0, "Automobile", "Mai");
		dataset.addValue(504.0, "Avril", "Mai");
		dataset.addValue(155.6, "Train", "Mai");

		JFreeChart chart = ChartFactory.createStackedAreaChart(
				"Test",
				"Categories",
				"Montant (€)",
				dataset, 
				PlotOrientation.VERTICAL,
				true,
				true,
				false);
		
		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		CategoryAxis categoryAxis = (CategoryAxis)plot.getDomainAxis();
		CategoryLabelPositions labelPositions = CategoryLabelPositions.UP_45;
		categoryAxis.setCategoryLabelPositions(labelPositions);

		CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0);
        
        ChartPanel chartPanel = new ChartPanel(chart);
		
		JFrame mainFrame = new JFrame();
		mainFrame.setContentPane(chartPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(400, 300);
		mainFrame.setVisible(true);
	}
}
