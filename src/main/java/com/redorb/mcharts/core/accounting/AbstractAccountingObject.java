package com.redorb.mcharts.core.accounting;

/**
 * Abstract class for AccountingObject (category, payee, account, bank, transaction).
 * Store the object id (Long) and name.
 */
public abstract class AbstractAccountingObject implements IAccountingObject {

	/*
	 * Attributes
	 */
		
	/** object's name */
	protected String name =  null;
	
	/*
	 * Ctor
	 */
	
	protected AbstractAccountingObject(final String name) {
		this.name = name;
	}
	
	protected AbstractAccountingObject(final AbstractAccountingObject accountingObject) {
		// no need to clone number and name : String and Long are immutable
		this(accountingObject.name);
	}
	
	/*
	 * Getters/Setters
	 */

	public String getName() {		
		return name;
	}
	
	/*
	 * Useful methods
	 */
		
	@Override
	public String toString() {
		return name;
	}
}
