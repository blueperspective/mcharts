package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

public interface ICriteria {
	
	public boolean match(Transaction transaction);
}
