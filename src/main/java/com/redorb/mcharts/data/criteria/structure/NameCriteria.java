package com.redorb.mcharts.data.criteria.structure;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Matches the name of an object with a name or a regular expression.
 */
public class NameCriteria extends AccountingObjectCriteria {

	/*
	 * Attributes
	 */
	
	// the exact name
	String name = null;
	
	// the pattern
	Pattern patternName = null;
	
	/*
	 * Ctors
	 */
	
	public NameCriteria(
			Class<? extends IAccountingObject> accountingObjectClass,
			String name) {
		super(accountingObjectClass);
		this.name = name;
	}
	
	public NameCriteria(
			Class<? extends IAccountingObject> accountingObjectClass,
			Pattern patternName) {
		super(accountingObjectClass);
		this.patternName = patternName;
	}
	
	/*
	 * Operations
	 */
	
	@Override
	public boolean match(Transaction transaction) {
		
		boolean res = false;
		
		// gets the object name
		
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
		
		// matches
		
		if (objectName != null && name != null) {
			res = name.equals(objectName);
		}
		else if (objectName != null && patternName != null) {
			Matcher m = patternName.matcher(objectName);
			res = m.matches();
		}
		else {
			res = true;
		}

		return res;
	}

	/*
	 * Getters
	 */
	
	public String getName() {
		return name;
	}

	public Pattern getPatternName() {
		return patternName;
	}
}
