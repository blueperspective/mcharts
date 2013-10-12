package com.redorb.mcharts.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.TransactionComparator;
import com.redorb.mcharts.data.TransactionComparator.CompareBy;

/**
 * Core class.
 * The core class stores:
 * - accounts
 * - payees
 * - categories
 */
public final class Core {

	/*
	 * Singleton design pattern
	 */

	/** logger */
	private final Logger log = LoggerFactory.getLogger(Core.class);

	/** Core's unique instance */
	private static Core instance = null;

	/**
	 * @return the unique instance of Core
	 */
	public static synchronized Core getInstance() {

		if (instance == null) {
			instance = new Core();
		}

		return instance;
	}

	/*
	 * Attributes
	 */

	/** list of accounts */
	private final Map<Long, Account> accounts = new HashMap<Long, Account>();

	/** list of payees */
	private final Map<Long, Payee> payees = new HashMap<Long, Payee>();

	/** list of categories */
	private final Map<Long, Category> categories = new HashMap<Long, Category>();

	/** common date format */
	private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	/** for transaction without categories */
	private final static Category NULL_CATEGORY = new Category(Long.valueOf(-1), "Sans catégorie");

	/** for transaction without categories */
	private final static Payee NULL_PAYEE = new Payee(Long.valueOf(-1), "Aucun tiers");

	private final static Category REMAINING_CATEGORY = new Category(Long.valueOf(-1), "Restant");

	private final static Payee REMAINING_PAYEE = new Payee(Long.valueOf(-1), "Restant");

	/*
	 * Ctor
	 */

	/*
	 * Operations
	 */

	public void addAccount(final Long number, final Account account) {
		accounts.put(number, account);
	}

	public void addCategory(final Long number, final Category category) {
		categories.put(number, category);	
	}

	public void addPayee(final Long number, final Payee payee) {
		payees.put(number, payee);
	}

	/*
	 * Getters/Setters
	 */

	public IAccountingObject getRemaining(final Class<? extends IAccountingObject> clazz) {

		IAccountingObject result = null;

		if (clazz.equals(Category.class)) {
			result = REMAINING_CATEGORY;
		}
		else if (clazz.equals(Payee.class)) {
			result = REMAINING_PAYEE;
		}
		else {
			log.error("remaining unknown " + clazz.getName());
		}

		return result;
	}

	public List<Transaction> getTransactions() {

		List<Transaction> res = new ArrayList<Transaction>();

		for (Account account : accounts.values()) {
			res.addAll(account.getTransactions());
		}
		
		Collections.sort(res, new TransactionComparator(CompareBy.DATE));

		return res;
	}

	public Map<Long, Account> getAccounts() {
		return accounts;
	}

	public Account getAccount(final Long number) {
		return accounts.get(number);
	}

	public Map<Long, Payee> getPayees() {
		return payees;
	}

	public DateFormat getDateFormat() {
		return DATE_FORMAT;
	}

	public Payee getPayee(final Long number) {

		Payee payee = payees.get(number);

		if (payee == null) {
			payee = NULL_PAYEE;
		}

		return payee;
	}

	public Map<Long, Category> getCategories() {
		return categories;
	}

	public Category getCategory(Long number) {
		Category category = categories.get(number);

		if (category == null) {
			category = NULL_CATEGORY;
		}

		return category;
	}
}
