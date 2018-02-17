package com.redorb.mcharts.data.criteria.structure;

import java.text.MessageFormat;
import java.util.List;

import com.redorb.mcharts.core.AccountingException;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Transaction;

public class NotATransferBetweenCriteria implements ICriteria {

	/*
	 * Attributes
	 */
	
	private List<Account> accounts = null;

	/*
	 * Ctors
	 */
	
	public NotATransferBetweenCriteria(List<Account> accounts) {
		this.accounts = accounts;
	}

	/*
	 * Operations
	 */
	
	@Override
	public boolean match(Transaction transaction) {
		
		if (accounts == null) {
			return true;
		}
		
		if (transaction.getLinkedTransaction() > 0) {
			
			Transaction linkedTransaction = Core.getInstance().getTransaction(transaction.getLinkedTransaction());
			
			if (linkedTransaction == null) {
				throw new AccountingException(MessageFormat.format("Transaction {0} hash linked  transaction {1}, which could not be found in map", transaction.getId(), transaction.getLinkedTransaction()));
			}
			
			return false;
		}
		
		return true;
	}
}
