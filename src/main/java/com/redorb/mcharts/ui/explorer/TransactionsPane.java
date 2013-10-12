package com.redorb.mcharts.ui.explorer;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;

import com.redorb.commons.ui.GBC;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.ui.models.TransactionTableModel;
import com.redorb.mcharts.ui.renderers.AlternateRowsTableRenderer;

@SuppressWarnings("serial")
public class TransactionsPane extends JPanel {

	/*
	 * Attributes
	 */

	/** hold the transaction table */
	private JScrollPane scrolTransactions;

	/** transactions table */
	private JXTable tabTransactions;


	/** model for the table */
	private TransactionTableModel transactionTableModel;

	/*
	 * Ctors
	 */

	public TransactionsPane() {
		initComponents();
		initLayout();
	}

	/*
	 * Operations
	 */

	private void initComponents() {

		scrolTransactions = new JScrollPane();
		tabTransactions = new JXTable();

		scrolTransactions.setViewportView(tabTransactions);
	}

	private void initLayout() {

		// layout

		setLayout(new GridBagLayout());

		add(scrolTransactions, new GBC(0, 2, GBC.BOTH).setGridWidth(GBC.REMAINDER));
	}

	/*
	 * Getters/Setters
	 */

	public void setTransactions(final List<Transaction> transactions) {

		transactionTableModel = new TransactionTableModel(transactions);
		tabTransactions.setModel(transactionTableModel);

		for (int c = 0; c < tabTransactions.getColumnModel().getColumnCount(); c++) {
			TableColumn col = tabTransactions.getColumnModel().getColumn(c);
			col.setCellRenderer(new AlternateRowsTableRenderer());
		}

		// scrolls down the last elements
		tabTransactions.changeSelection(
				tabTransactions.getModel().getRowCount() - 1,
				0, true, true);
	}
}
