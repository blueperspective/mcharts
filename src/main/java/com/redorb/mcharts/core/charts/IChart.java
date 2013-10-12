package com.redorb.mcharts.core.charts;

import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.restriction.IRestriction;
import com.redorb.mcharts.ui.charts.IChartCreator;

/**
 * Chart interface
 */
public interface IChart {

	String getName();
	
	void setName(String name);
	
	String getDescription();
	
	void setDescription(String description);
		
	/**
	 * @return the criteria
	 */
	ICriteria getCriteria();
	
	/**
	 * @return the aggregator
	 */
	IAggregator getAggregator();
	
	/**
	 * @return the restriction
	 */
	IRestriction getRestriction();

	/**
	 * @return chart creator
	 */
	IChartCreator getChartCreator();
}