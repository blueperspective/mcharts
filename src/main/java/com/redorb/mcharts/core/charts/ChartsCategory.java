package com.redorb.mcharts.core.charts;

import java.util.ArrayList;
import java.util.List;


/**
 * A category of charts to be displayed in a tree, which contains a list of 
 * instanciated charts.
 */
public class ChartsCategory {
	
	/*
	 * Attributes
	 */
	
	/** category's name */
	private String name = null;
	
	/** charts names */
	private List<ChartFileInfo> charts = new ArrayList<>();
	
	/*
	 * Ctors
	 */
	
	/**
	 * Build the category with a given name
	 * @param name the category name
	 */
	public ChartsCategory(String name) {
		this.name = name;
	}

	/*
	 * Operations
	 */
	
	/**
	 * Adds a chart
	 * @param chart
	 */
	public void add(ChartFileInfo chartFileInfo) {		
		charts.add(chartFileInfo);
	}
	
	public void remove(ChartFileInfo chartFileInfo) {		
		charts.remove(chartFileInfo);
	}
	
	/*
	 * Getters/Setters
	 */
	
	/**
	 * @param index
	 * @return the chart at the given index
	 */
	public ChartFileInfo getChart(int index) {
		return charts.get(index);
	}
		
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

	public List<ChartFileInfo> getCharts() {
		return charts;
	}

	/**
	 * @return the number of the charts
	 */
	public int getCount() {
		return charts.size();
	}
	
	public int indexOf(ChartFileInfo chartFileInfo) {
		return charts.indexOf(chartFileInfo);
	}

	@Override
	public String toString() {
		return name;
	}
}
