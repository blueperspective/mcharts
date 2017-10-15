package com.redorb.mcharts.data.aggregation.aggregator;

import java.util.List;

import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;

public interface IAggregator {

	void aggregate(List<Transaction> transactions);
	
	Dimension[] getDimensions();
	
	boolean getSkipInternalTransactions();
	
	void setSkipInternalTransactions(boolean value);
}
