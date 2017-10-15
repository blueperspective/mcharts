package com.redorb.mcharts.ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.criteria.filter.CriteriaFilter;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;
import com.redorb.mcharts.data.restriction.GlobalNFirstRestriction;
import com.redorb.mcharts.data.restriction.IRestriction;
import com.redorb.mcharts.ui.Conf;
import com.redorb.mcharts.ui.charts.BalanceChart;
import com.redorb.mcharts.ui.charts.IChartCreator;
import com.redorb.mcharts.ui.charts.Pie3DChart;
import com.redorb.mcharts.ui.components.SlidebarPanel;
import com.redorb.mcharts.ui.explorer.TransactionsPane;

@SuppressWarnings("serial")
public class DashboardPanel extends JPanel {

	/*
	 * Attributes
	 */

	private boolean isRendered = false;

	private SlidebarPanel<Account> accountSelector;

	private JScrollPane pnlAmountChart;

	private JLabel lblTransactions;
	private TransactionsPane pnlTransactions;

	private JButton butPrevious;
	private JPanel pnlCategoryChart;
	private JPanel pnlPayeeChart;
	private JButton butNext;

	private DateFormat dateFormat = new SimpleDateFormat("MMMMMM yyyy");

	private int monthsOffset = -1;

	private Date startDate;

	private List<Transaction> filteredTransactions;

	/*
	 * Ctors
	 */

	public DashboardPanel() {
		initComponents();
		initLayout();
	}

	public void initComponents() {

		accountSelector = new SlidebarPanel<>(5);
		accountSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Conf.getProps().setProperty(Conf.LAST_ACCOUNT, accountSelector.getSelectedElement().getName());
				render(false);
			}
		});

		pnlAmountChart = new JScrollPane();
		pnlAmountChart.setBackground(Color.WHITE);

		lblTransactions = new JLabel();
		Font f = getFont();
		lblTransactions.setFont(f.deriveFont(18f));
		pnlTransactions = new TransactionsPane();

		butPrevious = new JButton("<");
		butPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onPrevious();
			}
		});

		butNext = new JButton(">");
		butNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				onNext();
			}
		});
	}

	public void initLayout() {

		setLayout(new GridBagLayout());

		add(accountSelector, new GBC(0, 0, GBC.HORIZONTAL).setGridWidth(GBC.REMAINDER));

		add(pnlAmountChart, new GBC(0, 1, GBC.BOTH).setGridWidth(2).setGridHeight(2).setWeighty(3));
		add(lblTransactions, new GBC(2, 1).setAnchor(GBC.WEST));
		add(pnlTransactions, new GBC(2, 2, GBC.BOTH).setGridWidth(GBC.REMAINDER));

		add(butPrevious, new GBC(0, 3));
		add(butNext, new GBC(3, 3));
	}

	/*
	 * Operations
	 */

	public boolean isRendered() {
		return isRendered;
	}

	public void render(boolean firstTime) {

		isRendered = true;
		
		accountSelector.setObjets(Core.getInstance().getAccounts());

		if (firstTime) {

			String accountName = Conf.getProps().getString(Conf.LAST_ACCOUNT);
			Account account = null;

			if (accountName != null) {
				for (Account a : Core.getInstance().getAccounts()) {
					if (accountName.equals(a.getName())) {
						account = a;
						break;
					}
				}
			}

			if (account != null) {
				accountSelector.setSelectedElement(account);
			}
		}

		renderBalance(accountSelector.getSelectedElement());

		filterTransaction();
		renderLastMonthOperations();
		renderCategoryPayee();
	}

	public void onPrevious() {
		filterTransaction();
		monthsOffset--;
		renderLastMonthOperations();
		renderCategoryPayee();
	}

	public void onNext() {
		monthsOffset++;
		filterTransaction();
		renderLastMonthOperations();
		renderCategoryPayee();
	}

	private void filterTransaction() {

		// criteria

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		calendar.add(Calendar.MONTH, monthsOffset);
		startDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date endDate = calendar.getTime();

		ICriteria dateCriteria = new PeriodCriteria(startDate, endDate);

		filteredTransactions = CriteriaFilter.filter(
				accountSelector.getSelectedElement().getTransactions(), dateCriteria);
	}

	private void renderLastMonthOperations() {

		String balance = filteredTransactions.isEmpty() ? "" : filteredTransactions.get(filteredTransactions.size() - 1).getBalance().toString();

		lblTransactions.setText(
				I18n.getMessage("DashboardPanel.transactions", 
						dateFormat.format(startDate),
						balance));

		pnlTransactions.setTransactions(filteredTransactions);
	}

	private void renderBalance(Account account) {

		// criteria

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, - 6);
		Date startDate = calendar.getTime();
		Date endDate = new Date();

		BalanceChart chart = new BalanceChart();
		JFreeChart c = chart.createChart(I18n.getMessage("DashboardPanel.balanceTitle"), account, startDate, endDate);

		ChartPanel chartPanel = new ChartPanel(c);

		pnlAmountChart.setViewportView(chartPanel);

		validate();
		repaint();
	}

	private void renderCategoryPayee() {

		if (pnlCategoryChart != null) { remove(pnlCategoryChart); }
		if (pnlPayeeChart != null) { remove(pnlPayeeChart); }

		// aggregator
		KindAggregator categoryAggregator = new KindAggregator(Dimension.CATEGORY);

		KindAggregator payeeAggregator = new KindAggregator(Dimension.PAYEE);

		// chart

		pnlCategoryChart = renderSubchart(
				I18n.getMessage("HomePanel.pnlCategoryChart", dateFormat.format(startDate)),
				filteredTransactions, 
				categoryAggregator, 
				new Pie3DChart());

		pnlPayeeChart = renderSubchart(
				I18n.getMessage("HomePanel.pnlPayeeChart", dateFormat.format(startDate)),
				filteredTransactions,
				payeeAggregator,
				new Pie3DChart());

		add(pnlCategoryChart, new GBC(1, 3, GBC.BOTH));
		add(pnlPayeeChart, new GBC(2, 3, GBC.BOTH));

		validate();
		repaint();
	}

	private JPanel renderSubchart(
			String title, 
			List<Transaction> transactions,
			KindAggregator aggregator,
			IChartCreator chartCreator) {

		aggregator.aggregate(transactions);

		IRestriction restriction = new GlobalNFirstRestriction(8);

		AccountingTree outcomeTree = restriction.computeRestriction(aggregator.getOutcomeTree());

		JFreeChart outcomeChart = chartCreator.createChart(outcomeTree, title);

		JPanel pnlChart = new ChartPanel(outcomeChart);
		pnlChart.setBackground(Color.GRAY);

		return pnlChart;
	}
}
