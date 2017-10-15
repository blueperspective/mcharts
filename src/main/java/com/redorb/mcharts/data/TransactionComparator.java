package com.redorb.mcharts.data;

import java.io.Serializable;
import java.util.Comparator;

import com.redorb.mcharts.core.accounting.Transaction;

/**
 * Compare two transactions.
 */
public class TransactionComparator implements Comparator<Transaction>, Serializable {

	/*
	 * Attributes
	 */

	private static final long serialVersionUID = -7172345250959252039L;

	public enum CompareBy {
		DATE, 
		PAYEE,
		CATEGORY,
		AMOUNT
	};
	
	/** how to compares the transactions */
	private CompareBy compareBy = null;
	
	/** ascending order = true ; descending = false */
	private boolean ascending = true;

	/*
	 * Ctors
	 */
	
	public TransactionComparator(CompareBy compareBy) {
		this(compareBy, true);
	}
	
	public TransactionComparator(CompareBy compareBy, boolean ascending) {
		this.compareBy = compareBy;
		this.ascending = ascending;
	}

	/*
	 * Operations
	 */

	@Override
	public int compare(Transaction t1, Transaction t2) {
		
		int res = 0;
		
		// sort nulls so they appear last, regardless of sort order
		
        if (t1 == null && t2 == null) {
            res = 0;
        } 
        else if (t1 == null) {
            res = 1;
        } 
        else if (t2 == null) {
            res = -1;
        }
        else {
        	
        	switch(compareBy) {
        	case DATE:
        		res = t1.getDate().compareTo(t2.getDate());
        		break;
        	case PAYEE:
        		res = t1.getPayee().getName().compareTo(t2.getPayee().getName());
        		break;
        	case CATEGORY:
        		res = t1.getCategory().getName().compareTo(t2.getCategory().getName());
        		break;
        	case AMOUNT:
        		res = t1.getAmount().compareTo(t2.getAmount());
        		break;
        	}
        }
        
        // inverse order if not ascending
        
        if (!ascending) {
        	res = -res;
        }
		
		return res;
	}
	
	/*
	 * Getters/Setters
	 */

	/**
	 * @return the compareBy
	 */
	public CompareBy getCompareBy() {
		return compareBy;
	}

	/**
	 * @param compareBy the compareBy to set
	 */
	public void setCompareBy(CompareBy compareBy) {
		this.compareBy = compareBy;
	}

	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param ascending the ascending to set
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}
