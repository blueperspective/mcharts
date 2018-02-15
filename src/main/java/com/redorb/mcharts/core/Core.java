package com.redorb.mcharts.core;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private final List<Account> accounts = new ArrayList<>();

	/** list of payees */
	private final List<Payee> payees = new ArrayList<>();

	/** list of categories */
	private final List<Category> categories = new ArrayList<>();

	/** common date format */
	private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");

	/** for transaction without categories */
	public final static Category NULL_CATEGORY = new Category("Sans catégorie");

	/** for transaction without categories */
	public final static Payee NULL_PAYEE = new Payee("Sans tiers");

	public final static Category REMAINING_CATEGORY = new Category("Restant");

	public final static Payee REMAINING_PAYEE = new Payee("Restant");

	/*
	 * Ctor
	 */

	/*
	 * Operations
	 */

	public void addAccount(final Account account) {
		accounts.add(account);
	}

	public void addCategory(final Category category) {
		categories.add(category);	
	}

	public void addPayee(final Payee payee) {
		payees.add( payee);
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

		for (Account account : accounts) {
			res.addAll(account.getTransactions());
		}
		
		Collections.sort(res, new TransactionComparator(CompareBy.DATE));

		return res;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	
	public List<Payee> getPayees() {
		return payees;
	}

	public DateFormat getDateFormat() {
		return DATE_FORMAT;
	}
	
	public DecimalFormat getDecimalFormat() {
		return DECIMAL_FORMAT;
	}

	public List<Category> getCategories() {
		return categories;
	}
}
