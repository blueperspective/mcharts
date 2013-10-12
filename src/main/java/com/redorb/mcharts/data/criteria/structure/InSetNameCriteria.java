package com.redorb.mcharts.data.criteria.structure;

import java.util.HashSet;
import java.util.Set;

import com.redorb.mcharts.core.accounting.AbstractAccountingObject;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Do the match using a list of accounting objects, but rely on name
 * and not on java equals, contraty to InSetCriteria
 */
public class InSetNameCriteria extends AccountingObjectCriteria {

	/*
	 * Attributes
	 */
		
	private Set<String> set = null;
	
	/*
	 * Ctors 
	 */
	
	public InSetNameCriteria(Class<AbstractAccountingObject> accountingObjectClass) {
		
		super(accountingObjectClass);
		this.set = new HashSet<String>();
	}
	
	public InSetNameCriteria(
			Class<AbstractAccountingObject> accountingObjectClass,
			Set<String> set) {
		
		super(accountingObjectClass);
		this.set = set;
	}
	
	/*
	 * Operations
	 */
	
	@Override
	public boolean match(Transaction transaction) {
		
		boolean res = false;
		
		String objectName = null;
		
		if (accountingObjectClass.equals(Account.class)) {
			objectName = transaction.getAccount().getName();
		}
		else if (accountingObjectClass.equals(Category.class)) {
			objectName = transaction.getCategory().getName();
		}
		else if (accountingObjectClass.equals(Payee.class)) {
			objectName = transaction.getPayee().getName();
		}
		
		res = set.contains(objectName);
		
		// search for sub category
		
		if (! res && accountingObjectClass.equals(Category.class)) {
			res = set.contains(transaction.getSubCategory().toString());
		}

		return res;
	}

	/*
	 * Getters
	 */

	public Set<String> getSet() {
		return set;
	}
	
	@Override
	public String toString() {
		
		StringBuffer sf = new StringBuffer();
		
		for (String s : set) {
			sf.append(s + " ; ");
		}
		
		return sf.toString();
	}
}
