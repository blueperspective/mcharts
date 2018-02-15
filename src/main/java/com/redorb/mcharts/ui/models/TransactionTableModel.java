package com.redorb.mcharts.ui.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.TransactionComparator;

/**
 * Table model for showing a list of transactions.
 */
@SuppressWarnings("serial")
public class TransactionTableModel extends AbstractTableModel {

	/*
	 * Attributes
	 */

	private static final String[] cols = {
		I18n.getMessage("transactionTable.date"), 
		I18n.getMessage("common.payee"), 
		I18n.getMessage("common.category"),
		I18n.getMessage("common.subcategory"),
		I18n.getMessage("common.outcome"),
		I18n.getMessage("common.income"),
		I18n.getMessage("common.balance")};

	private List<Transaction> transactions = new ArrayList<Transaction>();

	private TransactionComparator transactionComparator = 
			new TransactionComparator(TransactionComparator.CompareBy.DATE);

	private final Logger log = LoggerFactory.getLogger(TransactionTableModel.class);
	
	/*
	 * Ctors
	 */

	public TransactionTableModel(List<Transaction> transactions) {

		Collections.sort(transactions, transactionComparator);
		this.transactions = transactions;
	}

	/*
	 * Getters/Setters
	 */

	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return cols[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex){
		
		Class<?> res = String.class;
		
		switch(columnIndex) {
		case 0:
			res = Date.class;
			break;
		case 4:
		case 5:
		case 6:
			res = BigDecimal.class;
		}			
			
		return res;
	}

	@Override
	public int getRowCount() {
		return transactions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object res = null;

		try {

			switch(columnIndex) {
			case 0:
				res = transactions.get(rowIndex).getDate();
				break;
			case 1:
				res = transactions.get(rowIndex).getPayee().getName();
				break;
			case 2:
				res = transactions.get(rowIndex).getCategory().getName();
				break;
			case 3:
				if (transactions.get(rowIndex).getSubCategory() != null) {
					res = transactions.get(rowIndex).getSubCategory().getName();
				}
				break;
			case 4:
				if (transactions.get(rowIndex).getAmount().signum() <= 0) {
					res = transactions.get(rowIndex).getAmount();
				}				
				break;
			case 5:
				if (transactions.get(rowIndex).getAmount().signum() > 0) {
					res = transactions.get(rowIndex).getAmount();
				}
				break;
			case 6:
				res = transactions.get(rowIndex).getBalance();
				break;
			default:
				res = "";
				break;
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return res;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
		fireTableDataChanged();
	}
}
