package com.redorb.mcharts.ui.panels;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.criteria.filter.CriteriaFilter;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.KindCriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;
import com.redorb.mcharts.ui.components.RingDateSelector;
import com.redorb.mcharts.ui.explorer.TransactionsPane;

@SuppressWarnings("serial")
public class MonthTransactionsPanel extends JPanel {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(MonthTransactionsPanel.class);

	private boolean isRendered = false;
	
	private RingDateSelector dateSelector;

	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	private TransactionsPane incomeTransactionsPane;
	private TransactionsPane outcomeTransactionsPane;

	/*
	 * Ctors
	 */

	public MonthTransactionsPanel() {
		initComponents();
		initLayout();
	}

	/**
	 * Initializes the components.
	 */
	protected void initComponents() {

		dateSelector = new RingDateSelector();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		dateSelector.setYear(cal.get(Calendar.YEAR));
		dateSelector.setMonth(cal.get(Calendar.MONTH));

		incomeTransactionsPane = new TransactionsPane();
		outcomeTransactionsPane = new TransactionsPane();
		
		ActionListener changeChartListener = new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				render();
			}
		};
		
		dateSelector.addActionListener(changeChartListener);
	}

	/**
	 * Initializes the layout.
	 */
	protected void initLayout() {

		setLayout(new GridBagLayout());

		add(dateSelector, new GBC(0, 0, GBC.HORIZONTAL));


		add(incomeTransactionsPane, new GBC(0, 1, GBC.BOTH));
		add(outcomeTransactionsPane, new GBC(0, 2, GBC.BOTH));
	}

	/*
	 * Operations
	 */
	
	public boolean isRendered() {
		return isRendered;
	}
	
	/**
	 * @return the criteria
	 */
	public ICriteria getCriteria() {

		ICriteria periodCriteria = null;

		try {

			Date startDate = dateFormat.parse("01/" + dateSelector.getMonth() + "/" + dateSelector.getYear());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			Date endDate = calendar.getTime();

			periodCriteria = new PeriodCriteria(startDate, endDate);

		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return periodCriteria;
	}
	
	public void render() {

		List<Transaction> periodTransactions = CriteriaFilter.filter(Core.getInstance().getTransactions(), getCriteria());
		
		List<Transaction> incomeTransactions = CriteriaFilter.filter(periodTransactions, new KindCriteria(Transaction.TRANSACTION_TYPE_INCOME));
		List<Transaction> outcomeTransactions = CriteriaFilter.filter(periodTransactions, new KindCriteria(Transaction.TRANSACTION_TYPE_OUTCOME));
		
		incomeTransactionsPane.setTransactions(incomeTransactions);
		outcomeTransactionsPane.setTransactions(outcomeTransactions);
	}
}
