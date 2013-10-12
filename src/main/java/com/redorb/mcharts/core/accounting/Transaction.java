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

	public static final int TRANSACTION_TYPE_DEDIT = 3;
	
	public static final int TRANSACTION_TYPE_CREDIT = 2;
	
	/*
	 * Attributes
	 */
		
	private int type = 0;	
	private Long account = null;	
	private Date date = null;	
	private BigDecimal amount = null;
	private Long payee = null;	
	private Long category = null;	
	private Long subCategory = null;
	private BigDecimal balance = null;
	
	/*
	 * Ctor
	 */
	
	/**
	 * Constructor used only for date searching purpose.
	 * @param date
	 */
	public Transaction(Date date) {
		super(Long.valueOf(-1L), null);
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
			Long number,
			int type,
			Long account,
			Date date,
			BigDecimal amount,
			Long payee,
			Long category,
			Long subCategory) {
		super(number, "");
		this.type = type;
		this.account = account;
		this.date = date;
		this.amount = amount;
		this.payee = payee;
		this.category = category;
		this.subCategory = subCategory;
	}
	
	/*
	 * Getters
	 */
	
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
		return Core.getInstance().getAccount(account);
	}

	public Payee getPayee() {
		return Core.getInstance().getPayee(payee);
	}

	public Category getCategory() {
		return Core.getInstance().getCategory(category);
	}

	public Category getSubCategory() {
		
		if (getCategory() != null) {
			return getCategory().getSubCategory(subCategory);
		}
		
		return null;
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
