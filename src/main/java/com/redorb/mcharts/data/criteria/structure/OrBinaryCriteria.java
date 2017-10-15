package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Implements a OR of two criterias.
 */
public class OrBinaryCriteria extends AbstractBinaryCriteriaDecorator {

	public OrBinaryCriteria(ICriteria rightCriteria, ICriteria leftCriteria) {
		super(rightCriteria, leftCriteria);
	}
	
	@Override
	public boolean match(Transaction transaction) {
		return rightCriteria.match(transaction) || leftCriteria.match(transaction); 
	}
}
