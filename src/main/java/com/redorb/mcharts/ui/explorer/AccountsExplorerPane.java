package com.redorb.mcharts.ui.explorer;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.criteria.filter.CriteriaFilter;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.ui.control.CriteriaPane;
import com.redorb.mcharts.ui.renderers.MapListRenderer;

/**
 * After selecting an accounts, shows all of its transactions in a table.
 * The table can be sorted by clicking on column header (a second click reverse
 * the order).
 * A criteria pane allows to filter the transactions.
 */
@SuppressWarnings("serial")
public class AccountsExplorerPane extends JPanel {

	/*
	 * Attributes
	 */

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(AccountsExplorerPane.class);

	/** criteria pane for filtering transactions */
	private CriteriaPane criteriaPane;
	
	/** account */
	private JLabel lblAccount;
	
	/** combo box for selecting the account */
	private JComboBox<Account> cmbAccount;
	
	/** transactions */
	private JLabel lblTransactions;
	
	/** show / hide criteria pane */
	private JToggleButton butShowCriteriaPane;
	
	/** apply the criteria defined in the criteria pane */
	private JButton butApplyCriteria;
	
	/** table of transaction */
	private TransactionsPane transactionPane;
	
	/** current selected account */
	private Account currentAccount;

	/*
	 * Ctors
	 */

	public AccountsExplorerPane() {
		initComponents();
		initLayout();
	}

	/*
	 * Operations
	 */

	/**
	 * Initializes the components.
	 */
	private void initComponents() {

		criteriaPane = new CriteriaPane();
		
		lblAccount = new JLabel();
		cmbAccount = new JComboBox<>();
		lblTransactions = new JLabel();
		butShowCriteriaPane = new JToggleButton();
		butApplyCriteria = new JButton();
		transactionPane = new TransactionsPane();
		
		lblAccount.setText(I18n.getMessage("accountExplorerPane.jlblAccount"));

		// fills the combo box with accounts using ObjetLabelModel

		DefaultComboBoxModel<Account> accountsModel = new DefaultComboBoxModel<>();
		for (Account account : Core.getInstance().getAccounts()) {
			accountsModel.addElement(account);
		}
		
		Map<Account, String> mapAccounts = new HashMap<>();
		for (Account account : Core.getInstance().getAccounts()) {
			mapAccounts.put(account, account.getName());
		}
		
		cmbAccount.setRenderer(new MapListRenderer<Account>(mapAccounts));
		cmbAccount.setModel(accountsModel);

		cmbAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				changeAccount();
			}			
		});

		lblTransactions.setText(I18n.getMessage("accountExplorerPane.jlblOperations"));
		
		butShowCriteriaPane.setText(I18n.getMessage(
				"accountsExplorerPane.jbutShowCriteriaPane"));
		
		butShowCriteriaPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
			showCriteriaPane(evt);
			}
		});
		
		butApplyCriteria.setText(I18n.getMessage("accountsExplorerPane.jbutApplyCriteria"));
		
		butApplyCriteria.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				applyCriteria();
			}			
		});

		// if there is at least one account, select it
		
		if (cmbAccount.getModel().getSize() >= 1) {
			cmbAccount.setSelectedIndex(0);
		}
		
		showCriteriaPane(null);
	}
	
	/**
	 * Initializes the layout.
	 */
	public void initLayout() {
			
		setLayout(new GridBagLayout());

		add(lblAccount, new GBC(0, 0).setAnchor(GBC.WEST));
		add(cmbAccount, new GBC(1, 0, GBC.HORIZONTAL));

		add(lblTransactions, new GBC(0, 1).setAnchor(GBC.WEST));		
		add(transactionPane, 
				new GBC(0, 2, GBC.BOTH)
					.setAnchor(GBC.WEST)
					.setGridWidth(GBC.REMAINDER));
		
		add(butShowCriteriaPane, new GBC(0, 3).setAnchor(GBC.WEST));
		
		add(criteriaPane, new GBC(0, 4, GBC.HORIZONTAL).setGridWidth(GBC.REMAINDER));
		
		add(butApplyCriteria, new GBC(1, 5).setAnchor(GBC.EAST));
	}
	
	/*
	 * Events
	 */

	/**
	 * Called when the user changes the current account.
	 * @param evt
	 */
	private void changeAccount() {

		Object o = cmbAccount.getModel().getSelectedItem();

		if (o instanceof Account) {
			
			currentAccount = (Account) o;
			transactionPane.setTransactions(currentAccount.getTransactions());
		}
	}
	
	/**
	 * Apply the criteria to the table.
	 */
	private void applyCriteria() {
		
		log.debug("applyCriteria");
		
		ICriteria criteria = criteriaPane.buildCriteria();
				
		List<Transaction> transactions = 
			CriteriaFilter.filter(currentAccount.getTransactions(), criteria);
		
		log.info(transactions.size() + " elements filtered");
		
		transactionPane.setTransactions(transactions);
	}
	
	/**
	 * Show / hide the criteria pane
	 * @param evt
	 */
	private void showCriteriaPane(ActionEvent evt) {
		
		boolean visible = butShowCriteriaPane.isSelected();
		
		criteriaPane.setVisible(visible);
		
		butApplyCriteria.setVisible(visible);
	}
}
