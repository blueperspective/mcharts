package com.redorb.mcharts.ui.explorer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.redorb.commons.ui.I18n;

@SuppressWarnings("serial")
public class ExplorerDialog extends JPanel {

	/*
	 * Attributes
	 */

	/** tabbed pane with : AccountsExplorerPane, CategoryExplorerPane and PayeeExplorerPane */
	private JTabbedPane jtabPane;

	/*
	 * Ctor
	 */

	public ExplorerDialog() {
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

		// build components

		jtabPane = new JTabbedPane();

		// tabbed pane

		jtabPane.addTab(
				I18n.getMessage("accountExplorerPane.title"), 
				new AccountsExplorerPane());
		jtabPane.addTab(
				I18n.getMessage("categoriesExplorerPane.title"), 
				new CategoriesExplorerPane());
		jtabPane.addTab(
				I18n.getMessage("payeesExplorerPane.title"), 
				new PayeesExplorerPane());
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		setLayout(new BorderLayout(4, 4));
		add(jtabPane, BorderLayout.CENTER);
	}
}
