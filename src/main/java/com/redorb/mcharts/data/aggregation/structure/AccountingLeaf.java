package com.redorb.mcharts.data.aggregation.structure;

import java.math.BigDecimal;

import com.redorb.mcharts.utils.MutableFloat;

public class AccountingLeaf extends AbstractLeaf<MutableFloat> {

	/*
	 * Ctors
	 */
	
	public AccountingLeaf(Object content) {
		super(content, new MutableFloat());
	}
	
	public void add(BigDecimal d) {
		value.add(d);
	}
}
