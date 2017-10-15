package com.redorb.mcharts.data.aggregation.structure;

import java.math.BigDecimal;

public class AccountingLeaf extends AbstractLeaf<BigDecimal> {

	/*
	 * Ctors
	 */
	
	public AccountingLeaf(Object content) {
		super(content, new BigDecimal(0));
	}
	
	public void add(BigDecimal d) {
		value = value.add(d);
	}
}
