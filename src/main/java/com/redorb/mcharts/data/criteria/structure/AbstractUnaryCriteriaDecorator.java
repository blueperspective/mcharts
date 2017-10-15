package com.redorb.mcharts.data.criteria.structure;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract unary criteria, containing only one criteria.
 */
public abstract class AbstractUnaryCriteriaDecorator implements ICriteriaDecorator {
	
	/*
	 * Attributes
	 */
	
	protected ICriteria criteria;
	
	/*
	 * Ctor
	 */
	
	protected AbstractUnaryCriteriaDecorator(ICriteria criteria) {
		this.criteria = criteria;
	}
	
	/*
	 * Getters/Setters
	 */
	
	@Override
	public List<ICriteria> getCriterias() {
		
		List<ICriteria> criterias = new ArrayList<ICriteria>();
		criterias.add(criteria);
		return criterias;
	}
}
