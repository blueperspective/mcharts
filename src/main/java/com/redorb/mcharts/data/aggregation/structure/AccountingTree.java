package com.redorb.mcharts.data.aggregation.structure;

import com.redorb.mcharts.data.aggregation.Dimension;

/**
 * An accounting tree represents the result of a multi-dimensional aggregation.
 */
public class AccountingTree extends Tree {

	/*
	 * Attributes
	 */
	
	private Dimension[] dimensions = null;

	/*
	 * Ctors
	 */
	
	public AccountingTree(Dimension... dimensions) {
		this.dimensions = dimensions;
	}

	/*
	 * Getters/Setters
	 */
	
	/**
	 * @return the dimensions
	 */
	public Dimension[] getDimensions() {
		return dimensions;
	}
}
