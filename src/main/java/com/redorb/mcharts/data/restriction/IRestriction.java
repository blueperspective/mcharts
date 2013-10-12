package com.redorb.mcharts.data.restriction;

import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public interface IRestriction {

	AccountingTree computeRestriction(AccountingTree tree);
	
	String getValue();
}
