package com.redorb.mcharts.data.aggregation.structure;

import com.redorb.mcharts.utils.MutableFloat;

/**
 * An AccountingNode is part of an AccountingTree.
 * The node represents an aggregated value, the leaf represents an aggregated
 * value + the amount value.
 */
public class AccountingNode extends AbstractNode<MutableFloat> {

	/*
	 * Attributes
	 */

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
	public MutableFloat getValue() {
		
		MutableFloat value = new MutableFloat();
		
		for (INode<MutableFloat> child : children) {
			value.add(child.getValue());
		}
		
		return value;
	}
}
