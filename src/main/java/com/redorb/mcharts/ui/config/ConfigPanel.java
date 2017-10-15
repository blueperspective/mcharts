package com.redorb.mcharts.ui.config;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;

/**
 * Main dialog for all config panes, organized in a tabbed pane.
 * Launches the store and load for all config panes.
 */
@SuppressWarnings("serial")
public final class ConfigPanel extends JPanel {

	/*
	 * Attributes
	 */

	/** indicate the config dialog is shown in the first run case */
	private boolean firstRun;

	/** main tabbed pane */
	private JTabbedPane tabPane;
	
	private LanguagePane languagePane;

	/** panel for bottom buttons (only OK) */
	private JPanel pnlButtons;


	/*
	 * Ctors
	 */

	public ConfigPanel(boolean firstRun) {

		this.firstRun = firstRun;

		initComponents();
		initLayout();
		
		if (! firstRun) {			
			restoreProperties();
		}		
	}

	/*
	 * Operations
	 */

	private void initComponents() {

		tabPane = new JTabbedPane();
		pnlButtons = Utils.createOkCancelPane(
				new ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						ok();
					}
				},
				new ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						cancel();
					}
				});

		// tabbed pane

		if (! firstRun) {
			
			tabPane.addTab(
					I18n.getMessage("displayPrefPane.title"),
					new DisplayPrefPanel());

			tabPane.addTab(
					I18n.getMessage("ignoreListPane.title"),
					new IgnoreListPane());
			

			tabPane.addTab(
					I18n.getMessage("languagePane.title"),
					new LanguagePane());
		}
		else {
			languagePane = new LanguagePane();
		}
	}

	public void initLayout() {

		// layout

		setLayout(new GridBagLayout());

		// tabbed pane
		
		if (! firstRun) {

			add(tabPane, 
					new GBC(0, 0, GBC.BOTH).
					setGridWidth(GBC.REMAINDER));
		}
		else {
			add(languagePane,
					new GBC(0, 0, GBC.BOTH).
					setGridWidth(GBC.REMAINDER));
		}
		
		add(Box.createGlue(), new GBC(0, 1, GBC.HORIZONTAL));

		// add the buttons panel

		add(pnlButtons, new GBC(2, 1, GBC.HORIZONTAL));
	}

	/*
	 * Events
	 */

	/**
	 * Restore all properties in all IConfig panes.
	 */
	public void restoreProperties() {

		for (int i = 0; i < tabPane.getTabCount(); i++) {

			if (tabPane.getComponentAt(i) instanceof IConfigPane) {

				IConfigPane configPane = (IConfigPane) tabPane.getComponentAt(i);
				configPane.restoreProperties();
			}
		}
	}

	/**
	 * Save all properties.
	 */
	public void saveProperties() {

		if (firstRun) {
			languagePane.saveProperties();
		}
		else {
		for (int i = 0; i < tabPane.getTabCount(); i++) {

			if (tabPane.getComponentAt(i) instanceof IConfigPane) {

				IConfigPane configPane = (IConfigPane) tabPane.getComponentAt(i);
				configPane.saveProperties();
			}
		}
		}
	}
	
	/**
	 * Ok event.
	 */
	public void ok() {

		saveProperties();

		setVisible(false);
	}

	/**
	 * Cancel event.
	 */
	public void cancel() {

		restoreProperties();

		setVisible(false);
	}
}
