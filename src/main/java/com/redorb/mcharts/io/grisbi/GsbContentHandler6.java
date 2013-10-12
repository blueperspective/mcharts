package com.redorb.mcharts.io.grisbi;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.TransactionComparator;
import com.redorb.mcharts.data.TransactionComparator.CompareBy;

public class GsbContentHandler6 implements ContentHandler {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(GsbContentHandler.class);

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
	public static final String XML_CHEQUE_NUMBER = "Pc";

	public static final String XML_ATT_KIND = "Kd";

	public static final String XML_VAL_KIND_INCOME = "0";

	// sub category

	public static final String XML_ATT_PARENT_CATEGORY = "Nbc";

	// date format
	
	private DateFormat gsbDateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/*
	 * Ctors
	 */

	/*
	 * Operations
	 */

	@Override
	public void startElement(
			String uri, 
			String localName, 
			String name,
			Attributes atts) throws SAXException {

		if (XML_ELT_ACCOUNT.equals(localName)) {
						
			BigDecimal amount = new BigDecimal(
					String.valueOf(Float.parseFloat(atts.getValue(XML_ATT_INITIAL_BALANCE)
							.replace(',', '.')
							.replace(" ", ""))));

			Long number = Long.parseLong(atts.getValue(XML_ATT_ACC_NUMBER));

			Account account = new Account(
					number,
					atts.getValue(XML_ATT_ACC_NAME),
					amount);

			Core.getInstance().addAccount(number, account);
		}
		else if (XML_ELT_CATEGORY.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));

			Category.Kind kind = Category.Kind.OUTCOME;

			if (XML_VAL_KIND_INCOME.equals(atts.getValue(XML_ATT_KIND))) {
				kind = Category.Kind.INCOME;
			}

			Category category = new Category(
					Long.parseLong(atts.getValue(XML_ATT_NUMBER)),
					atts.getValue(XML_ATT_NAME),
					kind);

			Core.getInstance().addCategory(number, category);
		}
		else if (XML_ELT_SUB_CATEGORY.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));
			Long parentNumber = Long.parseLong(atts.getValue(XML_ATT_PARENT_CATEGORY)); 

			Category category = new Category(
					number,
					atts.getValue(XML_ATT_NAME));

			Category parentCategory = Core.getInstance().getCategories().get(parentNumber);
			parentCategory.addSubCategory(number, category);
		}
		else if (XML_ELT_PAYEE.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));

			Payee payee = new Payee(
					number,
					atts.getValue(XML_ATT_NAME));

			Core.getInstance().addPayee(number, payee);
		}
		else if (XML_ELT_TRANSACTION.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));
			Long accountNumber = Long.parseLong(atts.getValue(XML_ATT_ACCOUNT));
			Date date = null;
			try {
				date = gsbDateFormat.parse(atts.getValue(XML_ATT_DATE));
			}
			catch (ParseException e) {
				throw new SAXException();
			}

			Account account = Core.getInstance().getAccount(accountNumber);
			
			BigDecimal amount = new BigDecimal(
					String.valueOf(Float.parseFloat(atts.getValue(XML_ATT_AMOUNT)
							.replace(',', '.')
							.replace(" ", ""))));
			
			Transaction transaction = new Transaction(
					number,
					Integer.parseInt(atts.getValue(XML_ATT_TRANSACTION_TYPE)),
					accountNumber,
					date,
					amount,
					Long.parseLong(atts.getValue(XML_ATT_PAYEE)),
					Long.parseLong(atts.getValue(XML_ATT_CATEGORY)),
					Long.parseLong(atts.getValue(XML_ATT_SUB_CATEGORY)));
			
			account.addTransaction(number, transaction);
		}
		else {
			log.debug("Element not used : " + localName);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {}

	@Override
	public void endDocument() throws SAXException {
		
		for (Account a : Core.getInstance().getAccounts().values()) {
		
			Collections.sort(a.getTransactions(), new TransactionComparator(CompareBy.DATE));
			
			BigDecimal balance = a.getInitialBalance();
			
			for (Transaction t : a.getTransactions()) {
				
				balance = balance.add(t.getAmount());
				t.setBalance(balance);				
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {}

	@Override
	public void setDocumentLocator(Locator locator) {}

	@Override
	public void skippedEntity(String name) throws SAXException {}

	@Override
	public void startDocument() throws SAXException {}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {}
}
