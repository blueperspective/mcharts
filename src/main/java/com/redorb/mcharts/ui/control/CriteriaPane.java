package com.redorb.mcharts.ui.control;

import java.awt.GridBagLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.ui.Ui;
import com.redorb.mcharts.ui.dialogs.SetSelectDialog;
import com.redorb.mcharts.ui.models.ListComboModel;
import com.redorb.mcharts.ui.renderers.MapListRenderer;
import com.redorb.mcharts.utils.Utils;
import com.michaelbaranov.microba.calendar.DatePicker;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.data.criteria.structure.AndListCriteria;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.InSetCriteria;
import com.redorb.mcharts.data.criteria.structure.NameCriteria;
import com.redorb.mcharts.data.criteria.structure.NegativeCriteria;
import com.redorb.mcharts.data.criteria.structure.NullCriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;

@SuppressWarnings("serial")
public class CriteriaPane extends JPanel {

	/*
	 * Attributes 
	 */

	private final static int CURRENT_MONTH = 0;
	private final static int LAST_MONTH = 1;
	private final static int CURRENT_TRIMESTER = 2;
	private final static int LAST_TRIMESTER = 3;
	private final static int CURRENT_SEMESTER = 4;
	private final static int LAST_SEMESTER = 5;
	private final static int CURRENT_YEAR = 6;
	private final static int LAST_YEAR = 7;

	private final static String[] DATE_PRESETS = new String[] {
		I18n.getMessage("criteriaPane.currentMonth"),
		I18n.getMessage("criteriaPane.lastMonth"),
		I18n.getMessage("criteriaPane.currentTrimester"),
		I18n.getMessage("criteriaPane.lastTrimester"),
		I18n.getMessage("criteriaPane.currentSemester"),
		I18n.getMessage("criteriaPane.lastSemester"),
		I18n.getMessage("criteriaPane.currentYear"),
		I18n.getMessage("criteriaPane.lastYear")
	};

	private final Logger log = LoggerFactory.getLogger(CriteriaPane.class);

	// date interval
	private JCheckBox chkDate;
	private JLabel lblStartDate;
	private DatePicker dpStartDate;
	private JLabel lblEndDate;
	private DatePicker dpEndDate;
	private JCheckBox chkInvertDate;
	private JLabel lblDatePreset;
	private JComboBox<String> cmbDatePreset;

	// name
	private JCheckBox chkName;
	private JLabel lblName;
	private JLabel lblNameObject;
	private JComboBox<String> cmbNameObject;
	private JComboBox<? extends IAccountingObject> cmbName;
	private JCheckBox chkInverseName;

	private ComboBoxModel<? extends IAccountingObject> accountsModel;
	private ComboBoxModel<Category> categoryModel;
	private ComboBoxModel<Payee> payeeModel;

	// set
	private JCheckBox chkSet;
	private JComboBox<String> cmbObjectSet;
	private JButton butSetSelect;
	private SetSelectDialog setSelectFrame = null;
	private JCheckBox chkInverseSet;

	private JSeparator jsepHoriz1;
	private JSeparator jsepHoriz2;

	private Set<IAccountingObject> setCriteria = new HashSet<IAccountingObject>();

	/*
	 * Ctor
	 */

	public CriteriaPane() {
		initComponents();
		initModels();
		initLayout();
	}

	/*
	 * Internal methods
	 */

	/**
	 * Initializes the components.
	 */
	private void initComponents() {

		jsepHoriz1 = new JSeparator();
		jsepHoriz2 = new JSeparator();

		// date interval

		chkDate = new JCheckBox(I18n.getMessage("criteriaPane.chkPeriod"));
		chkDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onCheckDateCriteria();
			}
		});

		lblStartDate = new JLabel(I18n.getMessage("criteriaPane.lblStartDate"));
		dpStartDate = new DatePicker();
		dpStartDate.setDateFormat(Core.getInstance().getDateFormat());

		lblEndDate = new JLabel(I18n.getMessage("criteriaPane.lblEndDate"));
		dpEndDate = new DatePicker();
		dpEndDate.setDateFormat(Core.getInstance().getDateFormat());

		chkInvertDate = new JCheckBox(I18n.getMessage("criteriaPane.chkInverse"));

		lblDatePreset = new JLabel(I18n.getMessage("criteriaPane.lblDatePreset"));
		cmbDatePreset = new JComboBox<String>(DATE_PRESETS);
		cmbDatePreset.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onDatePreset();
				}
			}
		});

		jsepHoriz1.setBorder(BorderFactory.createEtchedBorder());

		// name

		chkName = new JCheckBox(I18n.getMessage("criteriaPane.chkName"));
		chkName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onCheckNameCriteria();
			}
		});

		lblNameObject = new JLabel(I18n.getMessage("criteriaPane.lblNameObject"));

		cmbNameObject = new JComboBox<>();
		DefaultComboBoxModel<String> nameObjectModel = new DefaultComboBoxModel<>();
		nameObjectModel.addElement(Account.class.getName());
		nameObjectModel.addElement(Category.class.getName());
		nameObjectModel.addElement(Payee.class.getName());
		cmbNameObject.setRenderer(new MapListRenderer<String>(Ui.getMapAccountObjectNames()));
		cmbNameObject.setModel(nameObjectModel);
		cmbNameObject.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onChangeNameObject();
				}
			}
		});

		lblName = new JLabel(I18n.getMessage("criteriaPane.lblName"));
		cmbName = new JComboBox<>();
		cmbName.setEditable(true);

		chkInverseName = new JCheckBox(I18n.getMessage("criteriaPane.chkInverse"));

		jsepHoriz2.setBorder(BorderFactory.createEtchedBorder());

		// set

		chkSet = new JCheckBox(I18n.getMessage("criteriaPane.chkSet"));
		chkSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onCheckSetCriteria();
			}
		});

		butSetSelect = new JButton(I18n.getMessage("criteriaPane.butSetSelect"));		

		cmbObjectSet = new JComboBox<>();
		chkInverseSet = new JCheckBox(I18n.getMessage("criteriaPane.chkInverse"));

		DefaultComboBoxModel<String> setObjectModel =
				new DefaultComboBoxModel<>();
				setObjectModel.addElement(Account.class.getName());
				setObjectModel.addElement(Category.class.getName());
				setObjectModel.addElement(Payee.class.getName());

				cmbObjectSet.setRenderer(new MapListRenderer<String>(Ui.getMapAccountObjectNames()));
				cmbObjectSet.setModel(setObjectModel);
				cmbObjectSet.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO check for a real change
						setSelectFrame = null;
					}
				});

				butSetSelect.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						selectSet();
					}
				});

				onCheckDateCriteria();
				onCheckNameCriteria();
				onCheckSetCriteria();
	}

	private void initModels() {

		accountsModel = new ListComboModel<Account>(Core.getInstance().getAccounts());

		categoryModel = new ListComboModel<Category>(Core.getInstance().getCategories());

		payeeModel = new ListComboModel<Payee>(Core.getInstance().getPayees());

	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		// layout

		setBorder(javax.swing.BorderFactory.createTitledBorder(I18n.getMessage("criteriaPane.title")));
		setLayout(new GridBagLayout());

		// period

		JPanel pnlDate = new JPanel();
		pnlDate.setLayout(new GridBagLayout());

		pnlDate.add(chkDate, new GBC(0, 0).setAnchor(GBC.WEST));

		pnlDate.add(lblDatePreset, new GBC(1, 0).setAnchor(GBC.EAST));
		pnlDate.add(cmbDatePreset, new GBC(2, 0).setAnchor(GBC.EAST));

		pnlDate.add(lblStartDate, new GBC(1, 1).setAnchor(GBC.WEST));
		pnlDate.add(dpStartDate, new GBC(1, 2, GBC.HORIZONTAL));
		pnlDate.add(lblEndDate, new GBC(2, 1).setAnchor(GBC.WEST));
		pnlDate.add(dpEndDate, new GBC(2, 2, GBC.HORIZONTAL));

		pnlDate.add(chkInvertDate, new GBC(3, 2, GBC.WEST));

		// name

		JPanel pnlName = new JPanel();
		pnlName.setLayout(new GridBagLayout());

		pnlName.add(chkName, new GBC(0, 0).setAnchor(GBC.WEST));

		pnlName.add(lblNameObject, new GBC(1, 0).setAnchor(GBC.WEST));
		pnlName.add(cmbNameObject, new GBC(1, 1, GBC.HORIZONTAL));

		pnlName.add(lblName, new GBC(2, 0).setAnchor(GBC.WEST));
		pnlName.add(cmbName, new GBC(2, 1, GBC.HORIZONTAL));

		pnlName.add(chkInverseName, new GBC(3, 1).setAnchor(GBC.WEST));

		// set

		JPanel pnlSet = new JPanel();
		pnlSet.setLayout(new GridBagLayout());

		pnlSet.add(chkSet, new GBC(0, 0).setAnchor(GBC.WEST));

		pnlSet.add(cmbObjectSet, new GBC(1, 0, GBC.HORIZONTAL));
		pnlSet.add(butSetSelect, new GBC(2, 0).setAnchor(GBC.WEST));

		pnlSet.add(chkInverseSet, new GBC(3, 0).setAnchor(GBC.WEST));

		// separator

		add(pnlDate, new GBC(0, 0, GBC.HORIZONTAL));
		add(jsepHoriz1, new GBC(0, 1, GBC.HORIZONTAL).setGridWidth(GBC.REMAINDER));

		add(pnlName, new GBC(0, 2, GBC.HORIZONTAL));
		add(jsepHoriz2, new GBC(0, 3, GBC.HORIZONTAL).setGridWidth(GBC.REMAINDER));

		add(pnlSet, new GBC(0, 4, GBC.HORIZONTAL));
		add(Box.createGlue(), new GBC(0, 5, GBC.VERTICAL));

		// default selection

		onCheckDateCriteria();
		onCheckNameCriteria();
		onCheckSetCriteria();
	}

	/*
	 * Events
	 */

	private void onCheckNameCriteria() {

		boolean enabled = chkName.isSelected();

		lblName.setEnabled(enabled);
		cmbName.setEnabled(enabled);
		lblNameObject.setEnabled(enabled);
		cmbNameObject.setEnabled(enabled);
		chkInverseName.setEnabled(enabled);
	}

	private void onCheckDateCriteria() {

		boolean enabled = chkDate.isSelected();

		lblStartDate.setEnabled(enabled);
		dpStartDate.setEnabled(enabled);
		lblEndDate.setEnabled(enabled);
		dpEndDate.setEnabled(enabled);
		chkInvertDate.setEnabled(enabled);
	}

	private void onCheckSetCriteria() {

		boolean enabled = chkSet.isSelected();

		cmbObjectSet.setEnabled(enabled);
		butSetSelect.setEnabled(enabled);
		chkInverseSet.setEnabled(enabled);
	}

	/**
	 * When user select a new date preset.
	 */
	private void onDatePreset() {

		Date today = new Date();
		Date startDate = new Date(), endDate = new Date();

		int datePreset = cmbDatePreset.getSelectedIndex();

		switch (datePreset) {
		case CURRENT_MONTH:
			startDate = Utils.getNMonthDate(today, 0);
			endDate = today;
			break;
		case LAST_MONTH:
			startDate = Utils.getNMonthDate(today, -1);
			endDate = Utils.getNMonthDate(today, 0);
			break;
		case CURRENT_TRIMESTER:
			startDate = Utils.getNMonthDate(today, -3);
			endDate = today;
			break;
		case LAST_TRIMESTER:
			startDate = Utils.getNMonthDate(today, -6);
			endDate = Utils.getNMonthDate(today, -3);
			break;
		case CURRENT_SEMESTER:
			startDate = Utils.getNMonthDate(today, -6);
			endDate = today;
			break;
		case LAST_SEMESTER:
			startDate = Utils.getNMonthDate(today, -12);
			endDate = Utils.getNMonthDate(today, -6);
			break;
		case CURRENT_YEAR:
			startDate = Utils.getNMonthDate(today, -12);
			endDate = today;
			break;
		case LAST_YEAR:
			startDate = Utils.getNMonthDate(today, -24);
			endDate = Utils.getNMonthDate(today, -12);
			break;
		}

		try {
			dpStartDate.setDate(startDate);
			dpEndDate.setDate(endDate);
		} catch (PropertyVetoException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void onChangeNameObject() {

		String obj = (String) cmbNameObject.getSelectedItem();
/*
		if (Account.class.getName().equals(obj)) {
			cmbName.setModel(accountsModel);
		}
		else if (Category.class.getName().equals(obj)) {
			cmbName.setModel((ComboBoxModel<? extends IAccountingObject>) categoryModel);
		}
		else if (Payee.class.equals(obj)) {
			cmbName.setModel(payeeModel);
		}*/
	}

	private void selectSet() {

		Object o = cmbObjectSet.getModel().getSelectedItem();

		if (o != null && o instanceof Class) {

			if (setSelectFrame == null) {
				setSelectFrame = new SetSelectDialog(
						(JFrame) this.getTopLevelAncestor(),
						new ArrayList<IAccountingObject>(setCriteria), 
						(Class<? extends IAccountingObject>) o);
				setSelectFrame.setModalityType(ModalityType.APPLICATION_MODAL);
			}

			setSelectFrame.showCentered();

			setCriteria.clear();
			setCriteria.addAll(setSelectFrame.getSelectedList());
		}
		else {
			log.error("selectSet gave a non class object");
		}
	}

	/**
	 * Init the pane from a AndListCriteria
	 * @param criteria the root criteria, which has to be of type AndListCriteria
	 */
	public void init(ICriteria criteria) {

		if (criteria == null) {
			return;
		}

		// ensure that the criteria is a AndListCriteria
		if (criteria instanceof AndListCriteria) {

			// for each criteria
			for (ICriteria c : ((AndListCriteria) criteria).getCriterias()) {

				log.debug("Init criteria: " + c.getClass().getSimpleName());

				if (c instanceof PeriodCriteria) {

					PeriodCriteria periodCriteria = (PeriodCriteria)c;
					try {
						dpStartDate.setDate(periodCriteria.getStartDate());
						dpEndDate.setDate(periodCriteria.getEndDate());
					} catch (PropertyVetoException e) {
						log.error(e.getMessage(), e);
					}

					onCheckDateCriteria();

					log.debug("Put periodCriteria with: " 
							+ periodCriteria.getStartDate()
							+ "and "
							+ periodCriteria.getEndDate());
				}
				else if (c instanceof NameCriteria) {

					NameCriteria nameCriteria = (NameCriteria) c;

					cmbNameObject.setSelectedItem(nameCriteria.getAccountingObjectClass());
					cmbName.setSelectedItem(nameCriteria.getName());

					onCheckNameCriteria();
				}
				else if (c instanceof InSetCriteria) {

					InSetCriteria inSetCriteria = (InSetCriteria) c;
					setCriteria = inSetCriteria.getSet();

					onCheckSetCriteria();

					log.debug("Put InSetCriteria");
				}
			}
		}
		else {
			log.error("Cannot initialize: not a AndListCriteria : " + criteria.getClass().getSimpleName());
		}
	}

	/**
	 * Build the criteria from the preferences set by the user.
	 * @return
	 */
	public ICriteria buildCriteria() {

		AndListCriteria criterias = new AndListCriteria();

		// period criteria

		if (chkDate.isSelected()) {

			ICriteria criteria = null;

			criteria = new PeriodCriteria(
					dpStartDate.getDate(),
					dpEndDate.getDate());

			// inverse criteria ?

			if (chkInvertDate.isSelected()) {
				criteria = new NegativeCriteria(criteria);
			}

			// adds the criteria to the list

			criterias.addCriteria(criteria);
		}

		// name criteria

		if (chkName.isSelected()) {

			ICriteria criteria = null;

			String obj = (String) cmbNameObject.getSelectedItem();

			if (obj != null) {

				Class<? extends IAccountingObject> clazz;
				try {
					clazz = (Class<? extends IAccountingObject>) Class.forName(obj);
					criteria = new NameCriteria(clazz, cmbName.getSelectedItem().toString());
				} catch (ClassNotFoundException e) {
					log.error(e.getMessage(), e);
				}
			}
			else {
				log.error("cmbObject gave a non class object");
			}

			// inverse criteria ?

			if (chkInverseName.isSelected()) {
				criteria = new NegativeCriteria(criteria);
			}

			// adds the criteria to the list

			criterias.addCriteria(criteria);
		}

		// set criteria

		if (chkSet.isSelected()) {

			ICriteria criteria = null;

			log.debug("cmbObject for name is ObjectLabel");

			Object o = cmbObjectSet.getModel().getSelectedItem();

			if (o != null && o instanceof Class<?>) {

				criteria = new InSetCriteria(
						(Class<? extends IAccountingObject>) o,
						setCriteria);
			}

			// inverse criteria

			if (chkInverseSet.isSelected()) {
				criteria = new NegativeCriteria(criteria);
			}

			criterias.addCriteria(criteria);

			log.debug("criteria: " + criteria.toString());
		}

		// no criteria selected : return a null criteria (accepts everything)

		if (criterias.getCriterias().isEmpty()) {
			return new NullCriteria();
		}

		return criterias;
	}
}
