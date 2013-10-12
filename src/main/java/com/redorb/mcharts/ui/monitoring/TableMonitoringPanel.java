package com.redorb.mcharts.ui.monitoring;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import com.redorb.commons.ui.GBC;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.aggregator.GlobalAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.criteria.filter.CriteriaFilter;
import com.redorb.mcharts.ui.explorer.AccountingTreePane;

@SuppressWarnings("serial")
public class TableMonitoringPanel extends JPanel {

	/*
	 * Attributes
	 */
	
	private boolean isRendered = false;

	private MonitoringPanel monitoringPanel;

	private AccountingTreePane incomeTreePane;
	private AccountingTreePane outcomeTreePane;

	/*
	 * Ctors
	 */

	public TableMonitoringPanel() {
		initComponent();
		initLayout();
	}

	private void initComponent() {

		monitoringPanel = new MonitoringPanel();
		incomeTreePane = new AccountingTreePane();
		outcomeTreePane = new AccountingTreePane();

		monitoringPanel.addChangeListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				render();
			}
		});
	}

	private void initLayout() {

		setLayout(new GridBagLayout());

		add(monitoringPanel, new GBC(0, 0, GBC.HORIZONTAL));
		add(incomeTreePane, new GBC(0, 1, GBC.BOTH));
		add(outcomeTreePane, new GBC(0, 2, GBC.BOTH));
	}

	/*
	 * Operations
	 */
	
	public boolean isRendered() {
		return isRendered;
	}

	public void render() {

		List<Transaction> filteredTransactions = CriteriaFilter.filter(
				Core.getInstance().getTransactions(), monitoringPanel.getCriteria());
		
		IAggregator aggregator = monitoringPanel.getAggregator();		
		aggregator.aggregate(filteredTransactions);
		
		if (aggregator instanceof GlobalAggregator) {

			GlobalAggregator agg = (GlobalAggregator) aggregator;

			AccountingTree tree = agg.getTree();
			
			incomeTreePane.setTree(tree);
			outcomeTreePane.setVisible(false);
		}
		else if (aggregator instanceof KindAggregator) {

			KindAggregator agg = (KindAggregator) aggregator;
			
			AccountingTree incomeTree = agg.getIncomeTree();
			AccountingTree outcomeTree = agg.getOutcomeTree();
			
			incomeTreePane.setTree(incomeTree);
			outcomeTreePane.setTree(outcomeTree);
		}
	}
}
