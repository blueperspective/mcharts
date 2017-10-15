package com.redorb.mcharts.ui.dialogs;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.redorb.commons.ui.CenteredDialog;
import com.redorb.commons.ui.CommonProperties;
import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.accounting.AbstractAccountingObject;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.ui.models.AccountingObjectListModel;
import com.redorb.mcharts.ui.renderers.AlternateRowsListRenderer;

/**
 * Allow to select a set of elements.
 */
@SuppressWarnings("serial")
public class SetSelectDialog extends CenteredDialog {

	/*
	 * Attributes
	 */

	private JPanel jpnlListButtons;
	private JPanel jpnlButtons;
	private JScrollPane jscrolSelected;
	private JScrollPane jscrolAvailables;
	private JButton jbutSelect;
	private JButton jbutSelectAll;
	private JButton jbutUnselect;
	private JButton jbutUnselectAll;
	private JLabel jlblAvailables;
	private JLabel jlblSelection;
	private JList<IAccountingObject> jlstAvailables;
	private JList<IAccountingObject> jlstSelected;

	private List<IAccountingObject> originalList = null;

	private Class<? extends IAccountingObject> accountingObjectClass = null;

	private AccountingObjectListModel selectedModel = null;

	private AccountingObjectListModel availablesModel = null;

	/*
	 * Ctors
	 */

	/** 
	 * Creates new form SetSelectFrame 
	 */
	public SetSelectDialog(
			Window owner,
			List<IAccountingObject> selected,
			Class<? extends IAccountingObject> accountingObjectClass) {

		super(owner);

		this.originalList = selected;
		this.accountingObjectClass = accountingObjectClass;

		initComponents();
		initLayout();

		CommonProperties.restoreWindowProperties(this);
		setLocationRelativeTo(null);
	}

	/*
	 * Operations
	 */

	/** 
	 * Inits all components.
	 */
	private void initComponents() {

		jscrolSelected = new JScrollPane();
		jlstSelected = new JList<IAccountingObject>();
		jscrolAvailables = new JScrollPane();
		jlstAvailables = new JList<IAccountingObject>();
		jlblSelection = new JLabel(I18n.getMessage("setSelectDialog.lblSelection"));
		jlblAvailables = new JLabel(I18n.getMessage("setSelectDialog.lblAvailables"));
		jpnlListButtons = new JPanel();

		jpnlButtons = Utils.createOkCancelPane(
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						ok();
					}
				}, 
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						cancel();
					}
				});

		selectedModel = new AccountingObjectListModel(originalList);

		jlstSelected.setModel(selectedModel);
		jlstSelected.setCellRenderer(new AlternateRowsListRenderer<IAccountingObject>());
		jscrolSelected.setViewportView(jlstSelected);

		availablesModel = new AccountingObjectListModel(accountingObjectClass);

		jlstAvailables.setModel(availablesModel);
		jlstAvailables.setCellRenderer(new AlternateRowsListRenderer<IAccountingObject>());
		jscrolAvailables.setViewportView(jlstAvailables);

		// select buttons

		jbutSelect = Utils.createButtonNoBorder(
				"/images/22x22/select.png", "", "", 
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						select();
					}
				});

		jbutUnselect = Utils.createButtonNoBorder(
				"/images/22x22/unselect.png", "", "", 
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						unselect();
					}
				});

		jbutSelectAll = Utils.createButtonNoBorder(
				"/images/22x22/selectAll.png", "", "", 
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						selectAll();
					}
				});

		jbutUnselectAll = Utils.createButtonNoBorder(
				"/images/22x22/unselectAll.png", "", "", 
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						unselectAll();
					}
				});
	}

	private void initLayout() {

		// button list

		jpnlListButtons.setLayout(new GridLayout(4, 1, 0, 4));

		jpnlListButtons.add(jbutSelect);
		jpnlListButtons.add(jbutUnselect);        
		jpnlListButtons.add(jbutSelectAll);        
		jpnlListButtons.add(jbutUnselectAll);

		// layout

		setLayout(new GridBagLayout());

		add(jlblAvailables, new GBC(0, 0).setAnchor(GBC.WEST).setInsets(0, 4, 0, 0));
		add(jscrolAvailables, new GBC(0, 1, GBC.BOTH).setInsets(4, 4, 0, 4));

		add(jpnlListButtons, new GBC(1, 1).setAnchor(GBC.NORTH).setInsets(4, 4, 0, 4));
		add(Box.createGlue(), new GBC(1, 2));

		add(jlblSelection, new GBC(2, 0).setAnchor(GBC.WEST).setInsets(0, 4, 0, 0));
		add(jscrolSelected, new GBC(2, 1, GBC.BOTH).setInsets(4, 4, 0, 4));

		add(jpnlButtons, new GBC(2, 3).setAnchor(GBC.EAST));
	}

	/*
	 * Events
	 */

	private void ok() {

		originalList = selectedModel.getObjects();
		setVisible(false);

		CommonProperties.saveWindowProperties(this);
	}

	private void cancel() {

		setVisible(false);
	}	

	private void select() {

		List<IAccountingObject> objects = jlstAvailables.getSelectedValuesList();

		for (IAccountingObject accountingObject : objects) {
			selectedModel.add(accountingObject);
			availablesModel.remove(accountingObject);
		}
	}

	private void unselect() {

		List<IAccountingObject> objects = jlstSelected.getSelectedValuesList();

		for (IAccountingObject accountingObject : objects) {
			availablesModel.add(accountingObject);
			selectedModel.remove(accountingObject);
		}
	}

	private void selectAll() {

		Object[] objects = availablesModel.getObjects().toArray();

		for (Object o : objects) {
			AbstractAccountingObject accountingObject = (AbstractAccountingObject)o;
			selectedModel.add(accountingObject);
			availablesModel.remove(accountingObject);
		}
	}

	private void unselectAll() {

		Object[] objects = selectedModel.getObjects().toArray();

		for (Object o : objects) {
			AbstractAccountingObject accountingObject = (AbstractAccountingObject)o;
			availablesModel.add(accountingObject);
			selectedModel.remove(accountingObject);
		}
	}

	/*
	 * Getters/Setters
	 */

	public List<IAccountingObject> getOriginalList() {
		return originalList;
	}

	public void setOriginalList(List<IAccountingObject> originalList) {
		this.originalList = originalList;
	}

	public List<IAccountingObject> getSelectedList() {
		return selectedModel.getObjects();
	}
}
