package com.redorb.mcharts.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.core.charts.Chart;
import com.redorb.mcharts.data.aggregation.aggregator.GlobalAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.perf.Perf;
import com.redorb.mcharts.ui.Conf;
import com.redorb.mcharts.ui.charts.ChartPanelCreator;
import com.redorb.mcharts.ui.control.ControlPane;
import com.redorb.mcharts.ui.explorer.AccountingTreePane;
import com.redorb.mcharts.ui.explorer.TransactionsPane;

@SuppressWarnings("serial")
public class MainPane extends JPanel {

	/*
	 * Attributes
	 */

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(MainPane.class);

	private JLabel lblName;
	private JTextField txtName;
	private JButton butProperties;
	
	private JSplitPane splitPane;

	/** control pane: criteria, restriction, etc. */
	private ControlPane controlPane = null;

	/** tab pane for: chart, data grid */
	private JTabbedPane tabPane = null;

	/** pane containing the chart */
	private JScrollPane pnlChart = null;

	private IChart chart = null;

	/*
	 * Ctor
	 */
	
	public MainPane() {
		super();
		initComponents();
		initLayout();		
		//controlPane.init(chart);
	}

	public MainPane(IChart chart) {
		super();
		this.chart = chart;
		initComponents();
		initLayout();		
		controlPane.init(chart);
	}

	/*
	 * Internal methods
	 */

	/**
	 * Creates the components and the layout.
	 */
	private void initComponents() {

		controlPane = new ControlPane();

		butProperties = Utils.createButton(
				"/images/22x22/properties.png",
				"", 
				"", 
				new ActionListener() {					
			@Override
			public void actionPerformed(ActionEvent e) {
				onProperties();
			}
		}, 
		false);
		
		lblName = new JLabel(I18n.getMessage("mainPane.lblName"));

		//txtName = new JTextField(chart.getName());

		tabPane = new JTabbedPane();
		pnlChart = new JScrollPane();

		controlPane.getChartPane().getComputeButton().addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				computeChart();
			}
		});
		
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setOneTouchExpandable(true);
	}

	private void initLayout() {

		setLayout(new BorderLayout());

		JPanel pnlHeader = new JPanel();
		pnlHeader.setLayout(new GridBagLayout());
		pnlHeader.add(lblName, new GBC(0, 0));
		//pnlHeader.add(txtName, new GBC(1, 0, GBC.HORIZONTAL));
		pnlHeader.add(butProperties, new GBC(2, 0));
		
		JPanel pnlGraph = new JPanel();
		pnlGraph.setLayout(new BorderLayout());
		pnlGraph.add(pnlHeader, BorderLayout.PAGE_START);
		pnlGraph.add(tabPane, BorderLayout.CENTER);
		
		splitPane.setTopComponent(controlPane);
		splitPane.setBottomComponent(pnlGraph);
		
		add(splitPane);
	}

	/*
	 * Operations
	 */

	/**
	 * Called by control pane to inform that computation is done.
	 * Hides the control pane, for better chart visualization.
	 */
	public void computeChart() {

		controlPane.check();

		try {

			tabPane.removeAll();
			tabPane.addTab(I18n.getMessage("mainPane.jpnlChart"), pnlChart);

			chart = new Chart("", "",
					controlPane.getCriteriaPane().buildCriteria(),
					controlPane.getAggregatorPane().buildAggregator(),
					controlPane.getAggregatorPane().buildRestriction(),
					controlPane.getChartPane().buildChart());

			ChartPanelCreator creator = new ChartPanelCreator(chart);

			// compute the chart
			
			creator.compute();
			
			// show the transactions (optional)

			if (Conf.getProps().getBoolean(Conf.PROP_SHOW_TRANSACTIONS, false)) {

				// set the transactions in the transactionsPane
				TransactionsPane transactionsPane = new TransactionsPane();
				transactionsPane.setTransactions(creator.getFilteredTransactions());

				tabPane.addTab(I18n.getMessage("mainPane.transactionsPane"), transactionsPane);
			}
			
			// show the trees (optional)

			if (Conf.getProps().getBoolean(Conf.PROP_SHOW_TREES, false)) {

				IAggregator aggregator = chart.getAggregator();

				if (aggregator instanceof KindAggregator) {

					KindAggregator agg = (KindAggregator) aggregator;

					// set the accounting tree
					AccountingTreePane incomeTreePane = new AccountingTreePane();
					incomeTreePane.setTree(agg.getIncomeTree());
					AccountingTreePane outcomeTreePane= new AccountingTreePane();
					outcomeTreePane.setTree(agg.getOutcomeTree());

					tabPane.addTab(I18n.getMessage("mainPane.incomeTreePane"), 
							incomeTreePane);
					tabPane.addTab(I18n.getMessage("mainPane.outcomeTreePane"), 
							outcomeTreePane);
				}
				else if (aggregator instanceof GlobalAggregator) {

					GlobalAggregator agg = (GlobalAggregator) aggregator;

					// set the accounting tree
					AccountingTreePane treePane = new AccountingTreePane();
					treePane.setTree(agg.getTree());

					tabPane.addTab(I18n.getMessage("mainPane.incomeTreePane"), 
							treePane);
				}
			}

			// display the chart
			displayChart(creator);
			
			splitPane.setDividerLocation(0.0d);
			
			log.info(Perf.getInstance().toString());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Displays a given chart.
	 * @param container the chart's container
	 */
	public void displayChart(Container container) {

		pnlChart.setViewportView(container);
		container.setBackground(Color.WHITE);
	}

	/*
	 * Events
	 */

	public void onProperties() {

		splitPane.setDividerLocation(1.0);
	}

	/*
	 * Getters/Setters
	 */

	/**
	 * @return the chart
	 */
	public IChart getChart() {
		return chart;
	}

	public String getChartName() {
		return txtName.getText();
	}
}
