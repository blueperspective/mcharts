package com.redorb.mcharts.core.charts;

import java.io.File;

import com.redorb.mcharts.io.chart.ChartReader;

/**
 * Represent an item in a chart category.
 */
public class ChartFileInfo {

	/*
	 * Attributes
	 */
	
	private String name;
	
	private File file;

	private IChart chart;
	
	/*
	 * Ctors
	 */
	
	public ChartFileInfo(String name, String file) {
		this.name = name;
		this.file = new File(file);
	}

	public IChart getChart() {
		
		if (chart == null) {
			ChartReader reader = new ChartReader(file);
			reader.read();
			
			chart = reader.getChart();
		}
		
		return chart;
	}
	
	/*
	 * Getters/Setters
	 */
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
