package com.redorb.mcharts.data.aggregation.structure;

import java.util.List;

import com.redorb.mcharts.data.aggregation.Dimension;

/**
 * An accounting tree represents the result of a multi-dimensional aggregation.
 */
public class AccountingTree extends Tree {

	/*
	 * Attributes
	 */
	
	private List<Dimension> dimensions = null;

	/*
	 * Ctors
	 */
	
	public AccountingTree(List<Dimension> dimensions) {
		this.dimensions = dimensions;
	}

	/*
	 * Getters/Setters
	 */
	
	/**
	 * @return the dimensions
	 */
	public List<Dimension> getDimensions() {
		return dimensions;
	}
}
