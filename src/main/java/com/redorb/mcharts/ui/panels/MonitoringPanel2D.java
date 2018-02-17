package com.redorb.mcharts.ui.panels;

import java.awt.GridBagLayout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.charts.Chart;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.criteria.structure.AndBinaryCriteria;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.NotATransferBetweenCriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;
import com.redorb.mcharts.ui.Conf;
import com.redorb.mcharts.ui.charts.ChartPanelCreator;
import com.redorb.mcharts.ui.charts.StackedAreasChart;

@SuppressWarnings("serial")
public class MonitoringPanel2D extends JPanel {

	/*
	 * Attributes
	 */
	
	private final Logger log = LoggerFactory.getLogger(MonitoringPanel2D.class);
	
	private boolean isRendered = false;

	private JScrollPane pnlChart;
	
	/*
	 * Ctors
	 */
	
	public MonitoringPanel2D() {
		initComponents();
		initLayout();
	}

	/**
	 * Initializes the components.
	 */
	protected void initComponents() {

		pnlChart = new JScrollPane();
	}

	/**
	 * Initializes the layout.
	 */
	protected void initLayout() {
		
		setLayout(new GridBagLayout());
		
		add(pnlChart, new GBC(0, 0, GBC.BOTH));
	}

	/*
	 * Operations
	 */
	
	public ICriteria getCriteria() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -6);
		
		Date startDate = calendar.getTime();			
		Date endDate = new Date();

		ICriteria periodCriteria = new PeriodCriteria(startDate, endDate);
		return periodCriteria;
	}
	
	public boolean isRendered() {
		return isRendered;
	}
		
	public void render() {

		try {

			IAggregator aggregator = new KindAggregator(Dimension.MONTH, Dimension.CATEGORY);
			
			IChart chart = new Chart("", "",
					getCriteria(),
					aggregator,
					null,
					new StackedAreasChart());

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
