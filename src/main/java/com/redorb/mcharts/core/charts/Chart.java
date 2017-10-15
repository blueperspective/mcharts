package com.redorb.mcharts.core.charts;

import com.redorb.mcharts.ui.charts.IChartCreator;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.restriction.IRestriction;

/**
 * Template for creating charts.
 */
public class Chart implements IChart {

	/*
	 * Attributes
	 */

	/** Chart title. */
	protected String name = null;

	/** Chart description. */
	protected String description = null;

	/** Criteria. */
	protected ICriteria criteria = null;

	/** Aggregator. */
	protected IAggregator aggregator = null;

	/** Restriction. */
	protected IRestriction restriction = null;

	/** Chart. */
	protected IChartCreator accountingChart = null;

	/*
	 * Ctors
	 */

	public Chart(
			String name,
			String description,
			ICriteria criteria,
			IAggregator aggregator,
			IRestriction restriction,
			IChartCreator accountingChart) {
		this.name = name;
		this.description = description;
		this.criteria = criteria;
		this.aggregator = aggregator;
		this.restriction = restriction;
		this.accountingChart = accountingChart;
	}

	/*
	 * Operations
	 */

	/*
	 * Getters/Setters
	 */

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the criteria
	 */
	public ICriteria getCriteria() {
		return criteria;
	}

	/**
	 * @return the aggregator
	 */
	public IAggregator getAggregator() {
		return aggregator;
	}

	/**
	 * @return the restriction
	 */
	public IRestriction getRestriction() {
		return restriction;
	}

	/**
	 * @return the accountingChart
	 */
	public IChartCreator getChartCreator() {
		return accountingChart;
	}

	public String toString() {
		return name;
	}
}
