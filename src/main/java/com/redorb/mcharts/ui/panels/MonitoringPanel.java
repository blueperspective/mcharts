package com.redorb.mcharts.ui.panels;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.criteria.structure.AndBinaryCriteria;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.NotATransferBetweenCriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;
import com.redorb.mcharts.ui.Conf;
import com.redorb.mcharts.ui.components.RingDateSelector;

@SuppressWarnings("serial")
public class MonitoringPanel extends JPanel {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(MonitoringPanel.class);
	
	private RingDateSelector dateSelector;

	private JToggleButton butCategories;
	private JToggleButton butPayees;

	private JCheckBox chkSubcategories;

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	/*
	 * Ctors
	 */

	public MonitoringPanel() {
		initComponents();
		initLayout();
	}

	/**
	 * Initializes the components.
	 */
	protected void initComponents() {

		dateSelector = new RingDateSelector();

		butCategories = new JToggleButton(I18n.getMessage("common.categories"), Utils.getIcon("/images/16x16/category.png"));
		butPayees = new JToggleButton(I18n.getMessage("common.payees"), Utils.getIcon("/images/16x16/payee.png"));

		ButtonGroup group = new ButtonGroup();
		group.add(butCategories);
		group.add(butPayees);
		butCategories.setSelected(true);

		chkSubcategories = new JCheckBox(I18n.getMessage("MonitoringPanel.chkSubcategories"));
	}

	/**
	 * Initializes the layout.
	 */
	protected void initLayout() {

		setLayout(new GridBagLayout());

		add(dateSelector, new GBC(0, 0, GBC.HORIZONTAL));

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new GridBagLayout());
		pnlButtons.add(Box.createHorizontalGlue(), new GBC(0, 0, GBC.HORIZONTAL));
		pnlButtons.add(butCategories, new GBC(1, 0, GBC.HORIZONTAL));
		pnlButtons.add(butPayees, new GBC(2, 0, GBC.HORIZONTAL));
		pnlButtons.add(Box.createHorizontalGlue(), new GBC(3, 0, GBC.HORIZONTAL));

		add(pnlButtons, new GBC(0, 1, GBC.HORIZONTAL));

		add(chkSubcategories, new GBC(0, 2));
	}

	/*
	 * Operations
	 */

	public void addChangeListener(ActionListener changeChartListener) {

		dateSelector.addActionListener(changeChartListener);
		butCategories.addActionListener(changeChartListener);
		butPayees.addActionListener(changeChartListener);
	}

	/**
	 * @return the criteria
	 */
	public ICriteria getCriteria() {

		ICriteria criteria = null;
				
		try {
			
			// month criteria
			
			Date startDate = dateFormat.parse("01/" + dateSelector.getMonth() + "/" + dateSelector.getYear());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			Date endDate = calendar.getTime();

			ICriteria periodCriteria = new PeriodCriteria(startDate, endDate);
			
			// no transfert criteria
			
			ICriteria transfertCriteria = new NotATransferBetweenCriteria(
					 Core.getInstance().getAccountsByName(
							 Conf.getInstance().getProps().getList(Conf.PROP_HIDE_TRANSFERTS_LIST)));
			
			// combine
			
			criteria = new AndBinaryCriteria(periodCriteria, transfertCriteria);

		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return criteria;
	}

	/**
	 * @return an aggregator on one dimension
	 */
	public IAggregator getAggregator() {

		return new KindAggregator(butCategories.isSelected() ? Dimension.CATEGORY : Dimension.PAYEE);
	}
}
