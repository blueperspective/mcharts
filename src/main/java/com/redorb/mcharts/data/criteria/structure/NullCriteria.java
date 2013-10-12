package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

public class NullCriteria implements ICriteria {

	@Override
	public boolean match(Transaction transaction) {
		return true;
	}
}
