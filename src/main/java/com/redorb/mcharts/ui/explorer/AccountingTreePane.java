package com.redorb.mcharts.ui.explorer;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.Utils;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.ui.models.AccountingTreeModel;
import com.redorb.mcharts.ui.renderers.AccountingTreeRenderer;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

/**
 * Represents an accounting tree in a JTree.
 */
@SuppressWarnings("serial")
public class AccountingTreePane extends JPanel {

	/*
	 * Attributes
	 */

	/** accounting tree (aggregation) */
	private AccountingTree tree;

	/** categories */
	private JLabel lblTree;

	/** panel for buttons (expand & collapse) */
	private JPanel pnlButtons;

	/** expands the tree */
	private JButton butExpand;

	/** collapses the tree */
	private JButton butCollapse;

	/** scrolls the tree */
	private JScrollPane scrolTree;

	/** tree of categories */
	private JTree treeAccounting;

	/*
	 * Ctors
	 */

	public AccountingTreePane() {
		initComponents();
		initLayout();
	}

	/*
	 * Operations
	 */

	/**
	 * @return the tree
	 */
	public AccountingTree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(AccountingTree tree) {
		
		this.tree = tree;
		
		AccountingTreeModel treeModel = new AccountingTreeModel(tree);
		treeAccounting.setModel(treeModel);
		expandActionPerformed();
	}

	/**
	 * Initializes the components and the layout.
	 */
	private void initComponents() {

		lblTree = new JLabel();
		pnlButtons = new JPanel();
		scrolTree = new JScrollPane();
		treeAccounting = new JTree();

		lblTree.setText(I18n.getMessage("categoriesExplorerPane.jlblCategories"));

		treeAccounting.setCellRenderer(new AccountingTreeRenderer());
		treeAccounting.setRootVisible(false);
		scrolTree.setViewportView(treeAccounting);

		butCollapse = Utils.createButtonNoBorder(
				"/images/22x22/collapse.png",
				I18n.getMessage("categoriesExplorerPane.collapse.tooltip"),
				I18n.getMessage("categoriesExplorerPane.collapse"), 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						collapseActionPerformed();
					}});

		butExpand = Utils.createButtonNoBorder(
				"/images/22x22/expand.png", 
				I18n.getMessage("categoriesExplorerPane.expand.tooltip"),
				I18n.getMessage("categoriesExplorerPane.expand"), 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						expandActionPerformed();
					}});
	}
	
	private void initLayout() {
		
		// layout

		setLayout(new GridBagLayout());
		
		add(lblTree, new GBC(0, 0).setAnchor(GBC.WEST));
		add(scrolTree, new GBC(0, 1, GBC.BOTH));
		
		// button panel

		pnlButtons.setLayout(new GridBagLayout());
		
		pnlButtons.add(butCollapse, new GBC(0, 0).setAnchor(GBC.NORTH).setInsets(0, 0, 4, 4));
		pnlButtons.add(butExpand, new GBC(0, 1).setAnchor(GBC.NORTH).setInsets(4, 0, 4, 4));
		pnlButtons.add(Box.createGlue(), new GBC(0, 2, GBC.VERTICAL));
		
		add(pnlButtons, new GBC(1, 1));
	}

	private void expandActionPerformed() {

		int n = treeAccounting.getRowCount();

		for (int i = n - 1; i >= 0; i--) {
			treeAccounting.expandRow(i);
		}
	}

	private void collapseActionPerformed() {

		for (int i = 0; i < treeAccounting.getRowCount(); i++) {
			treeAccounting.collapseRow(i);
		}
	}
}
