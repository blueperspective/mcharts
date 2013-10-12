package com.redorb.mcharts.data.criteria.structure;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Do the match using a list of accounting objects.
 */
public class InSetCriteria extends AccountingObjectCriteria {

	/*
	 * Attributes
	 */
	
	private final Logger log = LoggerFactory.getLogger(InSetCriteria.class);
	
	private Set<IAccountingObject> set = null;
	
	/*
	 * Ctors 
	 */
	
	public InSetCriteria(Class<? extends IAccountingObject> accountingObjectClass) {
		super(accountingObjectClass);
		this.set = new HashSet<IAccountingObject>();
	}
	
	public InSetCriteria(
			Class<? extends IAccountingObject> accountingObjectClass,
			Set<IAccountingObject> set) {
		super(accountingObjectClass);
		this.set = set;
	}
	
	/*
	 * Operations
	 */
	
	@Override
	public boolean match(Transaction transaction) {
		
		boolean res = false;
		
		if (accountingObjectClass.equals(Account.class)) {
			res = set.contains(transaction.getAccount());
		}
		else if (accountingObjectClass.equals(Category.class)) {
			log.debug("category is : " + transaction.getCategory().getName());
			res = set.contains(transaction.getCategory())
			|| set.contains(transaction.getSubCategory());
		}
		else if (accountingObjectClass.equals(Payee.class)) {
			res = set.contains(transaction.getPayee());
		}
		else if (accountingObjectClass.equals(Transaction.class)) {
			res = set.contains(transaction);
		}

		return res;
	}

	/*
	 * Getters
	 */

	public Set<IAccountingObject> getSet() {
		return set;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sf = new StringBuilder();
		
		for (IAccountingObject o : set) {
			sf.append(o.getName());
			sf.append(" : ");
			sf.append(o.toString());
		}
		
		return sf.toString();
	}
}
