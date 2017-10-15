package com.redorb.mcharts.data.criteria.structure;

import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Abstract criteria for criterias on accounting objects.
 */
public abstract class AccountingObjectCriteria implements ICriteria {

	/*
	 * Attributes
	 */
	
	protected Class<? extends IAccountingObject> accountingObjectClass = null;
	
	/*
	 * Ctor
	 */
	
	protected AccountingObjectCriteria(
			Class<? extends IAccountingObject> accountingObjectClass) {
		this.accountingObjectClass = accountingObjectClass;
	}
	
	/*
	 * Operations
	 */
	
	public abstract boolean match(Transaction transaction);

	/**
	 * @return the accountingObjectClass
	 */
	public Class<? extends IAccountingObject> getAccountingObjectClass() {
		return accountingObjectClass;
	}
}
