package com.redorb.mcharts.core;

public class AccountingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4837234989276489962L;

	/*
	 * Ctors
	 */
	
	public AccountingException () {

    }

    public AccountingException (String message) {
        super (message);
    }

    public AccountingException (Throwable cause) {
        super (cause);
    }

    public AccountingException (String message, Throwable cause) {
        super (message, cause);
    }
}
