package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

public class KindCriteria implements ICriteria {

	int type = -1;
	
	public KindCriteria(int type) {
		this.type = type;
	}
	
	@Override
	public boolean match(Transaction transaction) {
		return type == transaction.getType();
	}
}
