package com.redorb.mcharts.io.grisbi;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

	// date format

	private DateFormat gsbDateFormat = new SimpleDateFormat("MM/dd/yyyy");

	private Map<Long, Account> accounts = new HashMap<>();

	private Map<Long, Payee> payees = new HashMap<>();

	private Map<Long, Category> categories = new HashMap<>();

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

		if (GsbConsts.XML_ELT_ACCOUNT.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_ACC_NUMBER));

			BigDecimal amount = new BigDecimal(
					String.valueOf(Float.parseFloat(atts.getValue(GsbConsts.XML_ATT_INITIAL_BALANCE)
							.replace(',', '.')
							.replace(" ", ""))));

			Account account = new Account(
					atts.getValue(GsbConsts.XML_ATT_ACC_NAME),
					amount);

			accounts.put(number, account);

			Core.getInstance().addAccount(account);
		}
		else if (GsbConsts.XML_ELT_CATEGORY.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_NUMBER));

			Category.Kind kind = Category.Kind.OUTCOME;

			if (GsbConsts.XML_VAL_KIND_INCOME.equals(atts.getValue(GsbConsts.XML_ATT_KIND))) {
				kind = Category.Kind.INCOME;
			}

			Category category = new Category(
					atts.getValue(GsbConsts.XML_ATT_NAME),
					kind);

			categories.put(number, category);

			Core.getInstance().addCategory(category);
		}
		else if (GsbConsts.XML_ELT_SUB_CATEGORY.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_NUMBER));
			Long parentNumber = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_PARENT_CATEGORY)); 

			Category category = new Category(atts.getValue(GsbConsts.XML_ATT_NAME));

			Category parentCategory = categories.get(parentNumber);
			parentCategory.addSubCategory(category);
		}
		else if (GsbConsts.XML_ELT_PAYEE.equals(localName)) {

			Long number = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_NUMBER));

			Payee payee = new Payee(atts.getValue(GsbConsts.XML_ATT_NAME));

			payees.put(number, payee);

			Core.getInstance().addPayee(payee);
		}
		else if (GsbConsts.XML_ELT_TRANSACTION.equals(localName)) {

			long number = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_NUMBER));
			long accountNumber = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_ACCOUNT));
			long payeeNumber = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_PAYEE));
			long categoryNumber = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_CATEGORY));
			long subCategoryNumber =Long.parseLong(atts.getValue(GsbConsts.XML_ATT_SUB_CATEGORY));
			long linkedTransaction = Long.parseLong(atts.getValue(GsbConsts.XML_ATT_LINKED_TRANSACTION));
			
			Date date = null;
			try {
				date = gsbDateFormat.parse(atts.getValue(GsbConsts.XML_ATT_DATE));
			}
			catch (ParseException e) {
				throw new SAXException(MessageFormat.format("Incorrect date format {0} (accepted format: {1}", atts.getValue(GsbConsts.XML_ATT_DATE), gsbDateFormat.toString()));
			}

			Account account = accounts.get(accountNumber);
			Payee payee = payees.get(payeeNumber);
			Category category = categories.get(categoryNumber);
			Category subCategory = categories.get(subCategoryNumber);

			BigDecimal amount = new BigDecimal(
					String.valueOf(Float.parseFloat(atts.getValue(GsbConsts.XML_ATT_AMOUNT)
							.replace(',', '.')
							.replace(" ", ""))));

			Transaction transaction = new Transaction(
					Integer.parseInt(atts.getValue(GsbConsts.XML_ATT_TRANSACTION_TYPE)),
					account,
					date,
					amount,
					payee,
					category,
					subCategory,
					linkedTransaction);

			account.addTransaction(transaction);
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

		for (Account account : Core.getInstance().getAccounts()) {

			Collections.sort(account.getTransactions(), new TransactionComparator(CompareBy.DATE));

			BigDecimal balance = account.getInitialBalance();

			for (Transaction t : account.getTransactions()) {

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
