package com.redorb.mcharts.core.accounting;

/**
 * Abstract class for AccountingObject (category, payee, account, bank, transaction).
 * Store the object id (Long) and name.
 */
public abstract class AbstractAccountingObject implements IAccountingObject {

	/*
	 * Attributes
	 */
	
	/** object's number (identifier) */
	protected Long number = null;
	
	/** object's name */
	protected String name =  null;
	
	/*
	 * Ctor
	 */
	
	protected AbstractAccountingObject(final Long number, final String name) {
		this.number = number;
		this.name = name;
	}
	
	protected AbstractAccountingObject(final AbstractAccountingObject accountingObject) {
		// no need to clone number and name : String and Long are immutable
		this(accountingObject.number, accountingObject.name);
	}
	
	/*
	 * Getters/Setters
	 */
	
	public Long getNumber() {
		return number;
	}

	public String getName() {		
		return name;
	}
	
	/*
	 * Useful methods
	 */
	
	@Override
	public boolean equals(final Object object) {
		
		boolean res = (object instanceof AbstractAccountingObject);
		
		res = res && ((AbstractAccountingObject) object).number.equals(number);
		
		return res;
	}
	
	@Override
	public int hashCode() {
		return (int) (number % Integer.MAX_VALUE);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
