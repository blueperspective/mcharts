package com.redorb.mcharts.data.aggregation.structure;

import java.math.BigDecimal;

/**
 * An AccountingNode is part of an AccountingTree.
 * The node represents an aggregated value, the leaf represents an aggregated
 * value + the amount value.
 */
public class AccountingNode extends AbstractNode<BigDecimal> {

	/*
	 * Ctors
	 */

	public AccountingNode(Object content) {
		super(content);
	}

	/*
	 * Operations
	 */

	@Override
	public BigDecimal getValue() {
		
		BigDecimal value = new BigDecimal(0);
		
		for (INode<BigDecimal> child : children) {
			value.add(child.getValue());
		}
		
		return value;
	}
}
