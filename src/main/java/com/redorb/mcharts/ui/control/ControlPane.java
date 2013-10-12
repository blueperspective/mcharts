package com.redorb.mcharts.ui.control;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import net.infonode.tabbedpanel.TabAdapter;
import net.infonode.tabbedpanel.TabStateChangedEvent;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.titledtab.TitledTab;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.charts.IChart;

/**
 * This pane contains 4 sub panes : criteria, aggregator, restriction and chart
 * pane.
 * This is where you launch the char computation.
 */
@SuppressWarnings("serial")
public class ControlPane extends JPanel {

	/*
	 * Attributes
	 */

	private TabbedPanel contentPane;
	private CriteriaPane criteriaPane = null;
	private AggregatorPane aggregatorPane = null;
	private ChartPane chartPane = null;

	/*
	 * Ctor
	 */

	public ControlPane() {
		initComponents();
		initLayout();
	}

	/*
	 * Internal methods
	 */

	/**
	 * Initializes the components.
	 */
	private void initComponents() {

		criteriaPane = new CriteriaPane();
		aggregatorPane = new AggregatorPane();
		chartPane = new ChartPane();
		
		contentPane = new TabbedPanel();

		contentPane.addTab(new TitledTab(
				I18n.getMessage("criteriaPane.title"), 
				null, 
				criteriaPane, 
				null));

		contentPane.addTab(new TitledTab(
				I18n.getMessage("aggregatorPane.title"), 
				null, 
				aggregatorPane, 
				null));

		contentPane.addTab(new TitledTab(
				I18n.getMessage("chartPane.title"), 
				null, 
				chartPane, 
				null));
		
		contentPane.addTabListener(new TabAdapter() {
			
			@Override
			public void tabSelected(TabStateChangedEvent evt) {
				if (chartPane.equals(evt.getCurrentTab().getContentComponent())) {
					onSelectChartPane();
				}
			}
		});
	}
	
	private void initLayout() {
		
		setLayout(new BorderLayout());
		
		add(contentPane, BorderLayout.CENTER);
	}

	/*
	 * Operations
	 */

	/**
	 * Initializes the panes using a template chart.
	 * @param chart
	 */
	public void init(IChart chart) {

		criteriaPane.init(chart.getCriteria());
		aggregatorPane.init(chart.getAggregator());
		chartPane.init(chart.getChartCreator());
	}

	/**
	 * Check all panes in the control pane.
	 */
	public void check() {

		// criteriaPane.check();
		aggregatorPane.check();
		chartPane.check();
	}
	
	/*
	 * Events
	 */
	
	public void onSelectChartPane() {
		
		chartPane.setDimensionCount(aggregatorPane.getDimensionCount());
	}

	/*
	 * Getters/Setters
	 */

	/**
	 * @return the criteriaPane
	 */
	public CriteriaPane getCriteriaPane() {
		return criteriaPane;
	}

	/**
	 * @return the aggregatorPane
	 */
	public AggregatorPane getAggregatorPane() {
		return aggregatorPane;
	}

	/**
	 * @return the chartPane
	 */
	public ChartPane getChartPane() {
		return chartPane;
	}
}
