package com.redorb.mcharts.data.criteria.structure;

import java.util.ArrayList;
import java.util.List;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Abstract list criteria, composed of a list of criteria.
 */
public abstract class AbstractListCriteriaDecorator implements ICriteriaDecorator {

	/*
	 * Attributes
	 */
	
	// list of criterias
	protected List<ICriteria> criterias = new ArrayList<ICriteria>();
	
	/*
	 * Ctors
	 */
	
	protected AbstractListCriteriaDecorator() {}
	
	/*
	 * Operations
	 */
	
	public void addCriteria(ICriteria criteria) {
		criterias.add(criteria);
	}
	
	public void removeCriteria(ICriteria criteria) {
		criterias.remove(criteria);
	}
	
	public abstract boolean match(Transaction transaction);

	/*
	 * Getters/Setters
	 */
	
	@Override
	public List<ICriteria> getCriterias() {
		return criterias;
	}
}
