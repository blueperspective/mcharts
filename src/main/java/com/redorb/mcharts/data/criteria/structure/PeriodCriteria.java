package com.redorb.mcharts.data.criteria.structure;

import java.util.Date;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Period criteria match a transaction when it is between the start and end
 * dates of the criteria.
 */
public class PeriodCriteria implements ICriteria {

	// start date of period
	private Date startDate = null;
	
	// end date of period
	private Date endDate = null;
	
	/*
	 * Ctor
	 */
	
	public PeriodCriteria(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/*
	 * Operations
	 */
	
	@Override
	public boolean match(Transaction transaction) {
		boolean res = startDate.before(transaction.getDate()) 
		&& transaction.getDate().before(endDate);
		
		return res;
	}

	/*
	 * Getters
	 */
	
	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
