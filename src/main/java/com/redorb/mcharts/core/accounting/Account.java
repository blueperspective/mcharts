package com.redorb.mcharts.core.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * An account represents a set of transactions.
 * It is the root object after reading a gsb file.
 */
public class Account extends AbstractAccountingObject {

	/*
	 * Attributes
	 */

	/** account's initial balance */
	private BigDecimal initialBalance = new BigDecimal(0.0f);

	/** account's transactions */
	private final List<Transaction> transactions = new ArrayList<Transaction>();

	/*
	 * Ctors
	 */

	public Account(final Long number, final String name, final BigDecimal initialBalance) {
		super(number, name);
		this.initialBalance = initialBalance;
	}

	/*
	 * Operations
	 */

	public final void addTransaction(final Long number, final Transaction transaction) {

		transactions.add(transaction);
	}

	/*
	 * Getters/Setters
	 */

	public final BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public final List<Transaction> getTransactions() {
		return transactions;
	}
}
