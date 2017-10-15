package com.redorb.mcharts.io.grisbi;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.TransactionComparator;
import com.redorb.mcharts.data.TransactionComparator.CompareBy;

public class GsbDomReader {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(GsbDomReader.class);

	private Map<Long, Account> accounts = new HashMap<>();

	private Map<Long, Payee> payees = new HashMap<>();

	private Map<Long, Category> categories = new HashMap<>();

	private Map<Long, Map<Long, Category>> subCategories = new HashMap<>();

	private DateFormat gsbDateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/*
	 * Operations
	 */

	public void read(String filepath) {

		File fXmlFile = new File(filepath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			readAccounts(doc.getElementsByTagName(GsbConsts.XML_ELT_ACCOUNT));

			readPayees(doc.getElementsByTagName(GsbConsts.XML_ELT_PAYEE));

			readCategories(doc.getElementsByTagName(GsbConsts.XML_ELT_CATEGORY));

			readSubCategories(doc.getElementsByTagName(GsbConsts.XML_ELT_SUB_CATEGORY));

			readTransactions(doc.getElementsByTagName(GsbConsts.XML_ELT_TRANSACTION));
			
			computeBalance();

		} catch (ParserConfigurationException e) {
			log.error(e.getMessage(), e);
		} catch (SAXException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void readAccounts(NodeList eltAccounts) {

		for (int a = 0; a < eltAccounts.getLength(); a++) {

			Element eltAccount = (Element) eltAccounts.item(a);

			long number = Long.parseLong(eltAccount.getAttribute(GsbConsts.XML_ATT_ACC_NUMBER));

			BigDecimal amount = new BigDecimal(
					String.valueOf(Float.parseFloat(eltAccount.getAttribute(GsbConsts.XML_ATT_INITIAL_BALANCE)
							.replace(',', '.')
							.replace(" ", ""))));

			Account account = new Account(
					eltAccount.getAttribute(GsbConsts.XML_ATT_ACC_NAME),
					amount);

			accounts.put(number, account);

			Core.getInstance().addAccount(account);
		}
	}

	private void readPayees(NodeList eltPayees) {

		for (int p = 0; p < eltPayees.getLength(); p++) {

			Element eltPayee = (Element) eltPayees.item(p);

			long number = Long.parseLong(eltPayee.getAttribute(GsbConsts.XML_ATT_NUMBER));

			Payee payee = new Payee(eltPayee.getAttribute(GsbConsts.XML_ATT_NAME));

			payees.put(number, payee);

			Core.getInstance().addPayee(payee);
		}
	}

	private void readCategories(NodeList eltCategories) {

		for (int c = 0; c < eltCategories.getLength(); c++) {

			Element eltCategory = (Element) eltCategories.item(c);

			long number = Long.parseLong(eltCategory.getAttribute(GsbConsts.XML_ATT_NUMBER));

			Category.Kind kind = Category.Kind.OUTCOME;

			if (GsbConsts.XML_VAL_KIND_INCOME.equals(eltCategory.getAttribute(GsbConsts.XML_ATT_KIND))) {
				kind = Category.Kind.INCOME;
			}

			Category category = new Category(
					eltCategory.getAttribute(GsbConsts.XML_ATT_NAME),
					kind);

			categories.put(number, category);

			Core.getInstance().addCategory(category);
		}
	}

	private void readSubCategories(NodeList eltCategories) {

		for (int c = 0; c < eltCategories.getLength(); c++) {

			Element eltCategory = (Element) eltCategories.item(c);

			long number = Long.parseLong(eltCategory.getAttribute(GsbConsts.XML_ATT_NUMBER));
			long parentNumber = Long.parseLong(eltCategory.getAttribute(GsbConsts.XML_ATT_PARENT_CATEGORY)); 

			Category category = new Category(eltCategory.getAttribute(GsbConsts.XML_ATT_NAME));

			Category parentCategory = categories.get(parentNumber);
			parentCategory.addSubCategory(category);

			Map<Long, Category> subCats = subCategories.get(parentNumber);

			if (subCats == null) {
				subCats = new HashMap<>();
				subCategories.put(parentNumber, subCats);
			}

			subCats.put(number, category);
		}
	}

	private void readTransactions(NodeList eltTransactions) {

		for (int p = 0; p < eltTransactions.getLength(); p++) {

			Element eltTransaction = (Element) eltTransactions.item(p);

			long accountNumber = Long.parseLong(eltTransaction.getAttribute(GsbConsts.XML_ATT_ACCOUNT));
			long payeeNumber = Long.parseLong(eltTransaction.getAttribute(GsbConsts.XML_ATT_PAYEE));
			long categoryNumber = Long.parseLong(eltTransaction.getAttribute(GsbConsts.XML_ATT_CATEGORY));
			long subCategoryNumber =Long.parseLong(eltTransaction.getAttribute(GsbConsts.XML_ATT_SUB_CATEGORY));
			long linkedTransaction = Long.parseLong(eltTransaction.getAttribute(GsbConsts.XML_ATT_LINKED_TRANSACTION));
			
			Date date = null;
			try {
				date = gsbDateFormat.parse(eltTransaction.getAttribute(GsbConsts.XML_ATT_DATE));
			}
			catch (ParseException e) {
				throw new RuntimeException(MessageFormat.format(
						"Incorrect date format {0} (accepted format: {1}", 
						eltTransaction.getAttribute(GsbConsts.XML_ATT_DATE), 
						gsbDateFormat.toString()));
			}

			Account account = accounts.get(accountNumber);
			Payee payee = payees.get(payeeNumber);			
			if (payee == null) {
				payee = Core.NULL_PAYEE;
			}
			
			Category category = categories.get(categoryNumber);			
			if (category == null) {
				category = Core.NULL_CATEGORY;
			}
			
			Category subCategory = null;

			if (categoryNumber != 0 && subCategories.containsKey(categoryNumber)) {
				subCategory = subCategories.get(categoryNumber).get(subCategoryNumber);
			}

			BigDecimal amount = new BigDecimal(
					String.valueOf(Float.parseFloat(eltTransaction.getAttribute(GsbConsts.XML_ATT_AMOUNT)
							.replace(',', '.')
							.replace(" ", ""))));

			Transaction transaction = new Transaction(
					Integer.parseInt(eltTransaction.getAttribute(GsbConsts.XML_ATT_TRANSACTION_TYPE)),
					account,
					date,
					amount,
					payee,
					category,
					subCategory,
					linkedTransaction);

			account.addTransaction(transaction);
		}
	}
	
	private void computeBalance() {
		
		for (Account account : Core.getInstance().getAccounts()) {

			Collections.sort(account.getTransactions(), new TransactionComparator(CompareBy.DATE));

			BigDecimal balance = account.getInitialBalance();

			for (Transaction t : account.getTransactions()) {

				balance = balance.add(t.getAmount());
				t.setBalance(balance);				
			}
		}
	}
}
