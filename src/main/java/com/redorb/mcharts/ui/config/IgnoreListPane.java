package com.redorb.mcharts.ui.config;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.ui.Conf;
import com.redorb.mcharts.ui.Ui;
import com.redorb.mcharts.ui.dialogs.ListPane;
import com.redorb.mcharts.ui.renderers.MapListRenderer;

/**
 * This pane shows a list of expressions to ignore everywhere (explorer, charts).
 * An example is a specified category used to signal a transfer into another 
 * account : maybe you do not want to see it as a loss of money.
 * It is allowed to add new ones and remove some.
 */
@SuppressWarnings("serial")
public class IgnoreListPane extends JPanel implements IConfigPane {

	/*
	 * Attributes
	 */

	private JCheckBox chkUseIgnoreList;
	private JLabel lblExpression;
	private JTextField txtExpression;
	private JLabel lblAccountingObject;
	private JComboBox<Class<?>> cmbAccountingObject;
	private ListPane<String> listPane;
	private JLabel lblExpressions;

	private DefaultComboBoxModel<Class<?>> comboModel = new DefaultComboBoxModel<Class<?>>();

	/*
	 * Ctors
	 */

	public IgnoreListPane() {
		super();
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

		chkUseIgnoreList = new JCheckBox();
		lblExpression = new JLabel();
		txtExpression = new JTextField();
		lblAccountingObject = new JLabel();
		cmbAccountingObject = new JComboBox<>();
		lblExpressions = new JLabel();
		listPane = new ListPane<>(ListPane.Orientation.TOP);

		chkUseIgnoreList.setText(I18n.getMessage("ignoreListPane.jchkUseIgnoreList"));

		lblExpression.setText(I18n.getMessage("ignoreListPane.jlblExpression"));

		lblAccountingObject.setText(I18n.getMessage("ignoreListPane.jlblAccountingObject"));

		comboModel.addElement(Category.class);
		comboModel.addElement(Payee.class);
		comboModel.addElement(Transaction.class);

		cmbAccountingObject.setRenderer(new MapListRenderer(Ui.getMapAccountObjectNames()));
		cmbAccountingObject.setModel(comboModel);

		lblExpressions.setText(I18n.getMessage("ignoreListPane.jlblExpressions"));

		listPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				addExpression(evt);
			}
		});
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		setLayout(new GridBagLayout());

		add(chkUseIgnoreList, new GBC(0, 0).setAnchor(GBC.WEST));
		add(lblExpression, new GBC(0, 1).setAnchor(GBC.WEST));
		add(txtExpression, new GBC(0, 2, GBC.HORIZONTAL).setAnchor(GBC.NORTH));
		add(lblAccountingObject, new GBC(0, 3).setAnchor(GBC.WEST));
		add(cmbAccountingObject, new GBC(0, 4, GBC.HORIZONTAL).setAnchor(GBC.NORTH));
		add(lblExpressions, new GBC(0, 5).setAnchor(GBC.WEST));
		add(listPane, new GBC(0, 6, GBC.BOTH));
	}

	/**
	 * Load the list from the ignore list.
	 */
	public void restoreProperties() {

		chkUseIgnoreList.setSelected(Conf.getProps().getBoolean(Conf.PROP_USE_IGNORE_LIST, false));

		if (Conf.getInstance().getIgnoreList() != null) {
			for (String expression : Conf.getInstance().getIgnoreList()) {
				listPane.getModel().addElement(expression);
			}
		}
	}

	/**
	 * Store the item in the ignore list.
	 */
	public void saveProperties() {

		List<String> expressionsList = Conf.getInstance().getIgnoreList();

		for (int i = 0; i < listPane.getModel().getSize(); i++) {
			expressionsList.add((String) listPane.getModel().getElementAt(i));
		}
	}

	/**
	 * Add an expression
	 * @param evt
	 */
	public void addExpression(ActionEvent evt) {

		if (evt.getSource() instanceof DefaultListModel<?>) {

			if (comboModel.getSelectedItem() != null 
					&& ! txtExpression.getText().isEmpty()) {

				Class<?> accountingObject = (Class<?>) comboModel.getSelectedItem();

				DefaultListModel<String> model = (DefaultListModel<String>) evt.getSource();
				model.addElement(accountingObject.getSimpleName() + ":" + txtExpression.getText());
				txtExpression.setText("");
			}
		}
	}
}
