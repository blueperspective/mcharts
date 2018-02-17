package com.redorb.mcharts.core.accounting;

import java.math.BigDecimal;
import java.util.Date;

import com.redorb.mcharts.core.Core;

/**
 * Represents a single transaction:
 * - date
 * - amount
 * - payee
 * - category
 * - subcategory
 */
public class Transaction extends AbstractAccountingObject {

	public static final int TRANSACTION_TYPE_OUTCOME = 3;
	
	public static final int TRANSACTION_TYPE_INCOME = 2;
	
	/*
	 * Attributes
	 */
		
	private long id = -1;
	private int type = 0;
	private Account account = null;
	private Date date = null;	
	private BigDecimal amount = null;
	private Payee payee = null;
	private Category category = null;
	private Category subCategory = null;
	private BigDecimal balance = null;
	private long linkedTransaction = -1;
	
	/*
	 * Ctor
	 */
	
	/**
	 * Constructor used only for date searching purpose.
	 * @param date
	 */
	public Transaction(Date date) {
		super("");
		this.date = date;
	}
	
	/**
	 * Contrusct a new transaction.
	 * @param number
	 * @param type
	 * @param account
	 * @param date
	 * @param amount
	 * @param payee
	 * @param category
	 * @param subCategory
	 */
	public Transaction(
			long id,
			int type,
			Account account,
			Date date,
			BigDecimal amount,
			Payee payee,
			Category category,
			Category subCategory,
			long linkedTransaction) {
		super("");
		this.id = id;
		this.type = type;
		this.account = account;
		this.date = date;
		this.amount = amount;
		this.payee = payee;
		this.category = category;
		this.subCategory = subCategory;
		this.linkedTransaction = linkedTransaction;
	}
	
	/*
	 * Getters
	 */
	
	public long getId() {
		return id;
	}
	
	public int getType() {
		return type;
	}
	
	public Date getDate() {
		return date;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public Account getAccount() {
		return account;
	}

	public Payee getPayee() {
		return payee;
	}

	public Category getCategory() {
		return category;
	}
	
	public Category getSubCategory() {
		return subCategory;
	}
	
	public long getLinkedTransaction() {
		return linkedTransaction;
	}
		
	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}	
	
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		String res = type + " : " + Core.getInstance().getDateFormat().format(date) + " : " + amount + " to " + getPayee() + " in category " + getCategory();
		
		return res;
	}
}
