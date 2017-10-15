package com.redorb.mcharts.data.criteria.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.TransactionComparator;
import com.redorb.mcharts.data.criteria.structure.AndListCriteria;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;
import com.redorb.mcharts.perf.Perf;

/**
 * Filter a collection of transactions using a criteria.
 */
public class CriteriaFilter {
	
	/**
	 * Filters transaction using a criteria.
	 * @param transactions
	 * @param criteria
	 * @return
	 */
	public static List<Transaction> filter(List<Transaction> transactions, ICriteria criteria) {
		
		Perf.getInstance().takeMeasure("start filter " + transactions.size());
		
		if (criteria instanceof AndListCriteria) {
			AndListCriteria andList = (AndListCriteria) criteria;
			if (andList.getCriterias().get(0) instanceof PeriodCriteria) {
				transactions = filter(transactions, (PeriodCriteria) andList.getCriterias().get(0));
				andList.removeCriteria(andList.getCriterias().get(0));
			}
		}
		
		List<Transaction> filteredTransactions = new ArrayList<Transaction>(transactions.size());

		for (Transaction transaction : transactions) {

			if (criteria == null || criteria.match(transaction)) {
				filteredTransactions.add(transaction);
			}
		}

		Perf.getInstance().takeMeasure("end filter");
		
		return filteredTransactions;
	}
	
	/**
	 * Faster filtering for date interval.
	 * Use binary search to get start and end index in the original transactions
	 * list (which MUST be date ascending sorted).
	 * @param transactions
	 * @param period
	 * @return
	 */
	private static List<Transaction> filter(List<Transaction> transactions, PeriodCriteria period) {
		
		int startIndex = 0, endIndex = 0;
		
		TransactionComparator dateComparator = new TransactionComparator(TransactionComparator.CompareBy.DATE);
		
		// search for start and end index
		// the transaction won't be found, but the insertion point returned by
		// the binarySearch gives the start (resp. end) index.
		
		startIndex = Collections.binarySearch(transactions, new Transaction(period.getStartDate()), dateComparator);
		
		if (startIndex < 0) {
			startIndex = - startIndex - 1;
		}
		
		endIndex = Collections.binarySearch(transactions, new Transaction(period.getEndDate()), dateComparator);
		
		if (endIndex < 0) {
			endIndex = - endIndex - 1;
		}
		
		if (endIndex >= transactions.size()) {
			endIndex = transactions.size() - 1;
		}
				
		// copy elements between start and end index
		
		List<Transaction> filteredTransactions = new ArrayList<Transaction>((endIndex - startIndex) + 1);
		
		for (int i = startIndex; i < endIndex; i++) {
			filteredTransactions.add(transactions.get(i));
		}
		
		return filteredTransactions;
	}
}
