package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Do or OR with a list of criteria.
 */
public class OrListCriteria extends AbstractListCriteriaDecorator {

	public OrListCriteria() {
		super();
	}
	
	@Override
	public boolean match(Transaction transaction) {
		
		boolean match = false;
		
		for (int i = 0 ; i < criterias.size() && ! match; i++) {
			match = match || criterias.get(i).match(transaction);
		}
		
		return match;
	}
}
