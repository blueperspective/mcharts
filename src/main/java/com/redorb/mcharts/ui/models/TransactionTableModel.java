package com.redorb.mcharts.ui.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.Core;
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
		I18n.getMessage("transactionTable.amount"),
		I18n.getMessage("transactionTable.amount")};

	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	private TransactionComparator transactionComparator = 
		new TransactionComparator(TransactionComparator.CompareBy.DATE);

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
		return String.class;
	}

	@Override
	public int getRowCount() {
		return transactions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object res = null;

		switch(columnIndex) {
		case 0:
			res = Core.getInstance().getDateFormat().format(transactions.get(rowIndex).getDate());
			break;
		case 1:
			res = transactions.get(rowIndex).getPayee().getName();
			break;
		case 2:
			res = transactions.get(rowIndex).getCategory().getName();
			break;
		case 3:
			res = transactions.get(rowIndex).getAmount();
			break;
		case 4:
			res = transactions.get(rowIndex).getBalance();
			break;
		default:
			res = "";
			break;
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
