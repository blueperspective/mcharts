package com.redorb.mcharts.io.grisbi;

public final class GsbConsts {

	// elements

	public static final String XML_ELT_ACCOUNT = "Account";
	public static final String XML_ELT_TRANSACTION = "Transaction";
	public static final String XML_ELT_PAYEE = "Party";
	public static final String XML_ELT_CATEGORY = "Category";
	public static final String XML_ELT_SUB_CATEGORY = "Sub_category";

	// generic attributes

	public static final String XML_ATT_NUMBER = "Nb";
	public static final String XML_ATT_NAME = "Na";

	// account

	public static final String XML_ATT_ACC_NUMBER = "Number";
	public static final String XML_ATT_ACC_NAME = "Name";
	public static final String XML_ATT_INITIAL_BALANCE = "Initial_balance";

	// transaction

	public static final String XML_ATT_ACCOUNT = "Ac";
	public static final String XML_ATT_TRANSACTION_TYPE = "Pn";
	public static final String XML_ATT_DATE = "Dt";
	public static final String XML_ATT_PAYEE = "Pa";
	public static final String XML_ATT_CATEGORY = "Ca";
	public static final String XML_ATT_SUB_CATEGORY = "Sca";
	public static final String XML_ATT_AMOUNT = "Am";
	public static final String XML_ATT_CHEQUE_NUMBER = "Pc";
	public static final String XML_ATT_LINKED_TRANSACTION = "Trt";

	public static final String XML_ATT_KIND = "Kd";

	public static final String XML_VAL_KIND_INCOME = "0";

	// sub category

	public static final String XML_ATT_PARENT_CATEGORY = "Nbc";
}
