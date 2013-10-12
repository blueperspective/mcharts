package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Do a AND on a list of criteria.
 */
public class AndListCriteria extends AbstractListCriteriaDecorator {

	public AndListCriteria() {
		super();
	}
	
	@Override
	public boolean match(Transaction transaction) {
		
		boolean match = true;
		
		for (int i = 0 ; i < criterias.size() && match; i++) {
			match = match && criterias.get(i).match(transaction);
		}
		
		return match;
	}
}
