package com.redorb.mcharts.ui.explorer;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Payee;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.ui.models.StringListModel;
import com.redorb.mcharts.ui.renderers.AlternateRowsListRenderer;

/**
 * Explorer pane showing the list of payees.
 */
@SuppressWarnings("serial")
public class PayeesExplorerPane extends JPanel {

	/*
	 * Attributes
	 */

	/** payees */
	private JLabel lblPayees;

	/** list of payees */
	private JList<String> lstPayees;
	
	/** scrolls the list of payees */
	private JScrollPane scrolPayees;

	/** list of payee's name (for the list model) */
	private List<String> payeesNames = new ArrayList<String>();

	/*
	 * Ctor
	 */

	public PayeesExplorerPane() {	
		initComponents();
		initLayout();
		initContents();
	}

	/*
	 * Operations
	 */

	/**
	 * Initializes the components.
	 */
	private void initComponents() {

		lblPayees = new JLabel(I18n.getMessage("payeesExplorerPane.jlblPayees"));
		scrolPayees = new JScrollPane();
		lstPayees = new JList<String>();

		scrolPayees.setViewportView(lstPayees);
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		// layout

		setLayout(new GridBagLayout());

		add(lblPayees, new GBC(0, 0).setAnchor(GBC.WEST));
		add(scrolPayees, new GBC(0, 1, GBC.BOTH));
	}

	/**
	 * Initializes the content of the list : build the model.
	 */
	private void initContents() {

		// get all payees

		for (Payee p : Core.getInstance().getPayees()) {
			payeesNames.add(p.getName());
		}

		// sort it in alpha order

		Collections.sort(payeesNames);

		// set the model

		lstPayees.setModel(new StringListModel(payeesNames));
		
		lstPayees.setCellRenderer(new AlternateRowsListRenderer<String>());
	}
}
