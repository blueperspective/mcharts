package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Negates a criteria.
 */
public class NegativeCriteria extends AbstractUnaryCriteriaDecorator {

	public NegativeCriteria(ICriteria criteria) {
		super(criteria);
	}
	
	@Override
	public boolean match(Transaction transaction) {
		return ! criteria.match(transaction);
	}
}
