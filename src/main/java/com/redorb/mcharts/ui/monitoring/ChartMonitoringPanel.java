package com.redorb.mcharts.ui.monitoring;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.mcharts.core.charts.Chart;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.ui.charts.ChartPanelCreator;
import com.redorb.mcharts.ui.charts.Pie3DChart;

@SuppressWarnings("serial")
public class ChartMonitoringPanel extends JPanel {

	/*
	 * Attributes
	 */
	
	private final Logger log = LoggerFactory.getLogger(ChartMonitoringPanel.class);

	private boolean isRendered = false;
	
	private MonitoringPanel monitoringPanel;
	
	private JScrollPane pnlChart;

	/*
	 * Ctors
	 */
	
	public ChartMonitoringPanel() {
		initComponents();
		initLayout();
	}

	protected void initComponents() {

		monitoringPanel = new MonitoringPanel();
		pnlChart = new JScrollPane();
		
		monitoringPanel.addChangeListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				render();
			}
		});
	}

	protected void initLayout() {
		
		setLayout(new GridBagLayout());
		
		add(monitoringPanel, new GBC(0, 0, GBC.HORIZONTAL));
		add(pnlChart, new GBC(0, 1, GBC.BOTH));
	}

	/*
	 * Operations
	 */
	
	public boolean isRendered() {
		return isRendered;
	}
	
	public void render() {

		try {
			
			IChart chart = new Chart("", "",
					monitoringPanel.getCriteria(),
					monitoringPanel.getAggregator(),
					null,
					new Pie3DChart());

			ChartPanelCreator creator = new ChartPanelCreator(chart);

			// compute the chart

			creator.compute();

			// display the chart
						
			pnlChart.setViewportView(creator);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
