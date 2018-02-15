package com.redorb.mcharts.ui.panels;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.AccountingCore;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.aggregator.GlobalAggregator;
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
	private JLabel lblTotalIncome;
	private JLabel lblTotalIncomeValue;
	
	private TransactionsPane outcomeTransactionsPane;
	private JLabel lblTotalOutcome;
	private JLabel lblTotalOutcomeValue;
	
	private JLabel lblTotal;
	private JLabel lblTotalValue;

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
		
		Font font = getFont();
		
		incomeTransactionsPane = new TransactionsPane();
		lblTotalIncome = new JLabel(I18n.getMessage("MonthTransactionsPanel.lblTotalIncome"));
		lblTotalIncomeValue = new JLabel();		
		lblTotalIncome.setFont(font.deriveFont(18.0f));
		lblTotalIncomeValue.setFont(font.deriveFont(18.0f));
		
		outcomeTransactionsPane = new TransactionsPane();		
		lblTotalOutcome = new JLabel(I18n.getMessage("MonthTransactionsPanel.lblTotalOutcome"));
		lblTotalOutcomeValue = new JLabel();
		lblTotalOutcome.setFont(font.deriveFont(18.0f));
		lblTotalOutcomeValue.setFont(font.deriveFont(18.0f));
		
		lblTotal = new JLabel(I18n.getMessage("MonthTransactionsPanel.lblTotal"));
		lblTotalValue = new JLabel();
		lblTotal.setFont(font.deriveFont(22.0f));
		lblTotalValue.setFont(font.deriveFont(22.0f));
		
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

		add(incomeTransactionsPane, new GBC(0, 1, GBC.BOTH).setGridWidth(GBC.REMAINDER));
		add(lblTotalIncome, new GBC(0, 2).setAnchor(GBC.EAST));
		add(lblTotalIncomeValue, new GBC(1, 2));
		
		add(outcomeTransactionsPane, new GBC(0, 3, GBC.BOTH).setGridWidth(GBC.REMAINDER));
		add(lblTotalOutcome, new GBC(0, 4).setAnchor(GBC.EAST));
		add(lblTotalOutcomeValue, new GBC(1, 4));
		
		add(lblTotal, new GBC(0, 5).setAnchor(GBC.EAST));
		add(lblTotalValue, new GBC(1, 5));
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

		List<Transaction> periodTransactions = CriteriaFilter.filter(AccountingCore.getInstance().getTransactions(), getCriteria());
		
		List<Transaction> incomeTransactions = CriteriaFilter.filter(periodTransactions, new KindCriteria(Transaction.TRANSACTION_TYPE_INCOME));
		List<Transaction> outcomeTransactions = CriteriaFilter.filter(periodTransactions, new KindCriteria(Transaction.TRANSACTION_TYPE_OUTCOME));
		
		incomeTransactionsPane.setTransactions(incomeTransactions);
		outcomeTransactionsPane.setTransactions(outcomeTransactions);
		
		GlobalAggregator incomeAgg = new GlobalAggregator((Dimension[])null);
		incomeAgg.aggregate(incomeTransactions);
		BigDecimal totalIncome = incomeAgg.getTree().getLeaf(null).getValue();		
		lblTotalIncomeValue.setText(AccountingCore.getInstance().getDecimalFormat().format(totalIncome));
		
		GlobalAggregator outcomeAgg = new GlobalAggregator((Dimension[])null);
		outcomeAgg.aggregate(outcomeTransactions);
		BigDecimal totalOutcome = outcomeAgg.getTree().getLeaf(null).getValue();		
		lblTotalOutcomeValue.setText(AccountingCore.getInstance().getDecimalFormat().format(totalOutcome));
		
		lblTotalValue.setText(AccountingCore.getInstance().getDecimalFormat().format(totalIncome.add(totalOutcome)));
	}
}
