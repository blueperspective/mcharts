package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Compound criteria, makes a binary and between two criteria.
 */
public class AndBinaryCriteria extends AbstractBinaryCriteriaDecorator {

	public AndBinaryCriteria(ICriteria rightCriteria, ICriteria leftCriteria) {
		super(rightCriteria, leftCriteria);
	}
	
	@Override
	public boolean match(Transaction transaction) {
		return rightCriteria.match(transaction) && leftCriteria.match(transaction); 
	}

}
