package com.redorb.mcharts.ui.charts;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create a new IChart.
 */
public class ChartFactory {
	
	/*
	 * Attributes
	 */
	
	private static final String PACKAGE_PREFIX = "com.redorb.mcharts.ui.charts.";
	
	private static final Logger log = LoggerFactory.getLogger(ChartFactory.class);
	
	/*
	 * Operations
	 */
	
	public static IChartCreator newInstance(String type) {
		
		log.debug("chart factory : newInstance");
		
		IChartCreator chart = null;
		
		try {

			// gets the restriction class

			Class<? extends IChartCreator> chartClass = Class.forName(PACKAGE_PREFIX + type).asSubclass(IChartCreator.class);

			// gets the constructor method

			Constructor<? extends IChartCreator> c = chartClass.getConstructor();

			// calls the constructor

			chart = c.newInstance();
			
			log.debug("Chart created : " + chart.getClass().getName());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return chart;
	}
}
