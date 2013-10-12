package com.redorb.mcharts.data.criteria.structure;

import java.util.ArrayList;
import java.util.List;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Abstract binary criteria: composed of a right and a left criteria.
 */
public abstract class AbstractBinaryCriteriaDecorator implements ICriteriaDecorator {

	// right criteria
	protected ICriteria rightCriteria = null;
	
	// left criteria
	protected ICriteria leftCriteria = null;
	
	/*
	 * Ctor
	 */
	
	protected AbstractBinaryCriteriaDecorator(ICriteria firstCriteria, ICriteria secondCriteria) {
		this.rightCriteria = firstCriteria;
		this.leftCriteria = secondCriteria;
	}
	
	public abstract boolean match(Transaction transaction);

	/*
	 * Getters
	 */
	
	public ICriteria getFirstCriteria() {
		return rightCriteria;
	}

	public ICriteria getSecondCriteria() {
		return leftCriteria;
	}
	
	@Override
	public List<ICriteria> getCriterias() {
		
		List<ICriteria> criterias = new ArrayList<ICriteria>();
		criterias.add(leftCriteria);
		criterias.add(rightCriteria);
		return criterias;
	}
}

