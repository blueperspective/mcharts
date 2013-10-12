package com.redorb.mcharts.io.grisbi;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

public class GsbContentHandler5 implements ContentHandler {

	/*
	 * Attributes
	 */
	
	private final Logger log = LoggerFactory.getLogger(GsbContentHandler5.class);
	
	private static final String XML_ELT_ACCOUNT = "Compte";
	private static final String XML_ELT_ACCOUNT_DETAILS = "Details";
	private static final String XML_ELT_ACCOUNT_NAME= "Nom";
	private static final String XML_ELT_ACCOUNT_NUMBER = "No_de_compte";
	private static final String XML_ELT_ACCOUNT_INITIAL_BALANCE = "Solde_initial";
	
	private static final String XML_ELT_TRANSACTION = "Operation";
	
	private static final String XML_ELT_PAYEES_DETAILS = "Detail_des_tiers";
	private static final String XML_ELT_PAYEE = "Tiers";
	
	private static final String XML_ELT_CATEGORIES_DETAILS = "Detail_des_categories";
	private static final String XML_ELT_CATEGORY = "Categorie";
	private static final String XML_ELT_SUB_CATEGORY = "Sous-categorie";
	
	// generic attributes
	
	public static final String XML_ATT_NUMBER = "No";
	public static final String XML_ATT_NAME = "Nom";
	
	// account
	
	public static final String XML_ATT_TRANSACTION_TYPE = "Ty";
	public static final String XML_ATT_DATE = "D";
	public static final String XML_ATT_PAYEE = "T";
	public static final String XML_ATT_CATEGORY = "C";
	public static final String XML_ATT_SUB_CATEGORY = "Sc";
	public static final String XML_ATT_AMOUNT = "M";
	public static final String XML_CHEQUE_NUMBER = "Ct";
	
	/** date format */
	private DateFormat gsbDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	/** stack of elements */
	private Stack<String> elements = new Stack<String>();

	/** last node value in case of <node>value</value> */
	private StringBuffer lastContent = null;
		
	/** last account : for transactions, the parent account */
	private Account lastAccount = null;
	
	/** for sub categories, the parent category */
	private Category lastCategory = null;
	
	/**
	 * for nodes who need sub node values to build an object : for instance
	 * to build the Account we need the "Nom" node value.
	 */
	private Map<String, String> collectedValues = new HashMap<String, String>();
	
	/*
	 * Ctor
	 */
	
	public GsbContentHandler5() {}
	
	/*
	 * Operations
	 */
	
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		
		BigDecimal balance = new BigDecimal(0);
		
		if (XML_ELT_CATEGORY.equals(localName)) {
			
			if (XML_ELT_CATEGORIES_DETAILS.equals(elements.peek())) {
				Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));
	
				Category category = new Category(
						Long.parseLong(atts.getValue(XML_ATT_NUMBER)),
						atts.getValue(XML_ATT_NAME));
	
				log.debug("category : " + category);
				
				Core.getInstance().addCategory(number, category);
				
				lastCategory = category;
			}
			else {
				log.error("category has an unknow parent : " + elements.peek());
			}
		}
		else if (XML_ELT_SUB_CATEGORY.equals(localName)) {
			
			Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));
			Long parentNumber = lastCategory.getNumber(); 

			Category category = new Category(
					number,
					atts.getValue(XML_ATT_NAME));
			
			log.debug("sub category : " + category);

			Category parentCategory = Core.getInstance().getCategories().get(parentNumber);
			parentCategory.addSubCategory(number, category);
		}
		else if (XML_ELT_PAYEE.equals(localName)) {
			
			if (!elements.empty()
					&& XML_ELT_PAYEES_DETAILS.equals(elements.peek())) {
				Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));
	
				Payee payee = new Payee(
						number,
						atts.getValue(XML_ATT_NAME));
				
				log.debug("payee : " + payee);
	
				Core.getInstance().addPayee(number, payee);
			}
		}
		else if (XML_ELT_TRANSACTION.equals(localName)) {
		
			Long number = Long.parseLong(atts.getValue(XML_ATT_NUMBER));
			Long accountNumber = lastAccount.getNumber();
			Date date = null;
			try {
				date = gsbDateFormat.parse(atts.getValue(XML_ATT_DATE));
			}
			catch (ParseException e) {
				throw new SAXException();
			}
			
			BigDecimal amount = new BigDecimal(
					Float.parseFloat(atts.getValue(XML_ATT_AMOUNT)
							.replace(',', '.')
							.replace(" ", "")));

			balance = balance.add(amount);

			Transaction transaction = new Transaction(
					number,
					Integer.parseInt(atts.getValue(XML_ATT_TRANSACTION_TYPE)),
					accountNumber,
					date,
					amount,
					Long.parseLong(atts.getValue(XML_ATT_PAYEE)),
					Long.parseLong(atts.getValue(XML_ATT_CATEGORY)),
					Long.parseLong(atts.getValue(XML_ATT_SUB_CATEGORY)));
			
			log.debug("transaction : " + transaction);

			Account account = Core.getInstance().getAccount(accountNumber);
			account.addTransaction(number, transaction);
		}
		else {
			//log.debug("Element not used : " + localName);
		}
		
		// push the current element in the stack of opened elements
		
		//log.debug("push " + localName);
		elements.push(localName);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
				
		if (XML_ELT_ACCOUNT_DETAILS.equals(localName)) {
			Long number = Long.parseLong(collectedValues.get(XML_ELT_ACCOUNT_NUMBER));

			Account account = new Account(
					number,
					collectedValues.get(XML_ELT_ACCOUNT_NAME),
					new BigDecimal(Float.parseFloat(collectedValues.get(XML_ELT_ACCOUNT_INITIAL_BALANCE).replace(',', '.').replace(" ", ""))));

			Core.getInstance().addAccount(number, account);
			
			lastAccount = account;
		}
		
		// pop the current element of stack of opened elements
		
		if (! elements.empty()) {
			elements.pop();
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		lastContent = new StringBuffer();
		lastContent.ensureCapacity(length);
		
		for (int i = 0 ; i < length; i++) {
			lastContent.append(ch[start + i]);
		}
		
		// collects values when in details of the account
		
		if (elements.size() == 4
				&& XML_ELT_ACCOUNT_DETAILS.equals(elements.get(elements.size() - 2))
				&& XML_ELT_ACCOUNT.equals(elements.get(elements.size() - 3))) {
			collectedValues.put(elements.peek(), lastContent.toString());
			log.debug("collected " + elements.peek() + " = " + lastContent);
		}
	}

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

	
	@Override
	public void endDocument() throws SAXException {}
}
