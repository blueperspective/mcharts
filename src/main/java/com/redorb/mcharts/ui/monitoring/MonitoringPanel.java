package com.redorb.mcharts.ui.monitoring;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.aggregation.aggregator.KindAggregator;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;
import com.redorb.mcharts.ui.components.SlidebarPanel;

@SuppressWarnings("serial")
public class MonitoringPanel extends JPanel {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(MonitoringPanel.class);

	private SlidebarPanel<String> yearControl;
	private SlidebarPanel<String> monthControl;

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

	
	protected void initComponents() {

		yearControl = new SlidebarPanel<>(5, 
				Arrays.asList("2007,2008,2009,2010,2011,2012,2013,2014,2015,2016".split(",")));
							
		yearControl.setSelectedElement(new SimpleDateFormat("yyyy").format(new Date()));
		
		monthControl = new SlidebarPanel<>(7, 
				Arrays.asList("Janvier,Fevrier,Mars,Avril,Mai,Juin,Juillet,Aout,Septembre,Octobre,Novembre,Decembre".split(",")));

		monthControl.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("MM").format(new Date())) - 1);
		
		butCategories = new JToggleButton(I18n.getMessage("common.categories"), Utils.getIcon("/images/16x16/category.png"));

		butPayees = new JToggleButton(I18n.getMessage("common.payees"), Utils.getIcon("/images/16x16/payee.png"));

		ButtonGroup group = new ButtonGroup();
		group.add(butCategories);
		group.add(butPayees);
		butCategories.setSelected(true);

		chkSubcategories = new JCheckBox(I18n.getMessage("MonitoringPanel.chkSubcategories"));
	}

	protected void initLayout() {

		setLayout(new GridBagLayout());

		add(yearControl, new GBC(0, 0, GBC.HORIZONTAL));
		add(monthControl, new GBC(0, 1, GBC.HORIZONTAL));

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new GridBagLayout());
		pnlButtons.add(Box.createHorizontalGlue(), new GBC(0, 0, GBC.HORIZONTAL));
		pnlButtons.add(butCategories, new GBC(1, 0, GBC.HORIZONTAL));
		pnlButtons.add(butPayees, new GBC(2, 0, GBC.HORIZONTAL));
		pnlButtons.add(Box.createHorizontalGlue(), new GBC(3, 0, GBC.HORIZONTAL));
		
		add(pnlButtons, new GBC(0, 2, GBC.HORIZONTAL));
		
		add(chkSubcategories, new GBC(0, 3));
	}

	/*
	 * Operations
	 */

	public void addChangeListener(ActionListener changeChartListener) {

		yearControl.addActionListener(changeChartListener);
		monthControl.addActionListener(changeChartListener);
	}

	public ICriteria getCriteria() {

		ICriteria dateCriteria = null;

		try {
			
			Date startDate = dateFormat.parse("01/" + monthControl.getSelectedIndex() + "/" + yearControl.getSelectedElement());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			Date endDate = calendar.getTime();

			dateCriteria = new PeriodCriteria(startDate, endDate);
			
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return dateCriteria;
	}

	public IAggregator getAggregator() {

		List<Dimension> dimensions = new ArrayList<>();
		dimensions.add(Dimension.CATEGORY);

		return new KindAggregator(dimensions);
	}
}
