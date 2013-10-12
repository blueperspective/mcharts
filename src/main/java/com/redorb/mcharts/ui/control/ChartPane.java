package com.redorb.mcharts.ui.control;

import java.awt.GridBagLayout;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.ui.charts.Bar3DChart;
import com.redorb.mcharts.ui.charts.BarChart;
import com.redorb.mcharts.ui.charts.IChartCreator;
import com.redorb.mcharts.ui.charts.LinesChart;
import com.redorb.mcharts.ui.charts.MultiplePieCharts;
import com.redorb.mcharts.ui.charts.Pie3DChart;
import com.redorb.mcharts.ui.charts.PieChart;
import com.redorb.mcharts.ui.charts.StackedAreasChart;

/**
 * Pane to select the chart type, launch the computing and displaying progress
 * bar.
 */
@SuppressWarnings("serial")
public class ChartPane extends JPanel {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(ChartPane.class);

	private JLabel lblChartType;
	private JProgressBar pbarCompute;

	private ButtonGroup chartGroup;
	private JLabel lblLines;
	private JToggleButton btnLines;
	private JLabel lblPie;
	private JToggleButton btnPie;
	private JLabel lblPie3d;
	private JToggleButton btnPie3d;
	private JLabel lblMultipie;
	private JToggleButton btnMultipie;
	private JLabel lblBar;
	private JToggleButton btnBar;
	private JLabel lblBar3d;
	private JToggleButton btnBar3d;
	private JLabel lblStack;
	private JToggleButton btnStack;

	private JButton butCompute;

	private Map<Class<? extends IChartCreator>, JToggleButton> mapButtonsChart = 
			new HashMap<Class<? extends IChartCreator>, JToggleButton>();

	private JToggleButton[][] chartButtons;

	/*
	 * Ctors
	 */

	public ChartPane() {
		initComponents();
		initLayout();
	}

	/*
	 * Operations
	 */

	/**
	 * Initializes the components.
	 */
	private void initComponents() {

		lblChartType = new JLabel(I18n.getMessage("chartPane.chartType"));
		pbarCompute = new JProgressBar();
		butCompute = new JButton(I18n.getMessage("chartPane.jbutCompute"));

		lblLines = new JLabel(I18n.getMessage("common.linesChart"));
		btnLines = new JToggleButton(Utils.getIcon("/images/charts/line.png"));

		lblPie = new JLabel(I18n.getMessage("common.pieChart"));
		btnPie = new JToggleButton(Utils.getIcon("/images/charts/pie.png"));

		lblPie3d = new JLabel(I18n.getMessage("common.pie3dChart"));
		btnPie3d = new JToggleButton(Utils.getIcon("/images/charts/pie3d.png"));

		lblMultipie = new JLabel(I18n.getMessage("common.multiplePieCharts"));
		btnMultipie = new JToggleButton(Utils.getIcon("/images/charts/multipie.png"));

		lblBar = new JLabel(I18n.getMessage("common.barChart"));
		btnBar = new JToggleButton(Utils.getIcon("/images/charts/bar.png"));

		lblBar3d = new JLabel(I18n.getMessage("common.bar3dChart"));
		btnBar3d = new JToggleButton(Utils.getIcon("/images/charts/bar3d.png"));

		lblStack = new JLabel(I18n.getMessage("common.stackedAreasChart"));
		btnStack = new JToggleButton(Utils.getIcon("/images/charts/stackarea.png"));

		chartGroup = new ButtonGroup();
		chartGroup.add(btnLines);
		chartGroup.add(btnPie);
		chartGroup.add(btnPie3d);
		chartGroup.add(btnMultipie);
		chartGroup.add(btnBar);
		chartGroup.add(btnBar3d);
		chartGroup.add(btnStack);

		mapButtonsChart.put(LinesChart.class, btnLines);
		mapButtonsChart.put(PieChart.class, btnPie);
		mapButtonsChart.put(Pie3DChart.class, btnPie3d);
		mapButtonsChart.put(MultiplePieCharts.class, btnMultipie);
		mapButtonsChart.put(BarChart.class, btnBar);
		mapButtonsChart.put(Bar3DChart.class, btnBar3d);
		mapButtonsChart.put(StackedAreasChart.class, btnStack);

		chartButtons = new JToggleButton[][] {
				{ btnPie, btnPie3d, btnBar, btnBar3d },
				{ btnLines, btnMultipie, btnStack }
		};
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		JPanel pnlCharts = new JPanel();
		pnlCharts.setLayout(new GridBagLayout());

		// 1st line

		pnlCharts.add(btnPie, new GBC(0, 1));
		pnlCharts.add(lblPie, new GBC(0, 2));

		pnlCharts.add(btnPie3d, new GBC(1, 1));
		pnlCharts.add(lblPie3d, new GBC(1, 2));

		pnlCharts.add(btnBar, new GBC(2, 1));
		pnlCharts.add(lblBar, new GBC(2, 2));

		pnlCharts.add(btnBar3d, new GBC(3, 1));
		pnlCharts.add(lblBar3d, new GBC(3, 2));
		
		// 2nd line
		
		pnlCharts.add(btnLines, new GBC(0, 3));
		pnlCharts.add(lblLines, new GBC(0, 4));
		
		pnlCharts.add(btnMultipie, new GBC(1, 3));
		pnlCharts.add(lblMultipie, new GBC(1, 4));

		pnlCharts.add(btnStack, new GBC(2, 3));
		pnlCharts.add(lblStack, new GBC(2, 4));

		// global

		setBorder(BorderFactory.createTitledBorder(I18n.getMessage("chartPane.title")));
		setLayout(new java.awt.GridBagLayout());

		add(lblChartType, new GBC(0, 0).setAnchor(GBC.WEST));
		add(pnlCharts, new GBC(0, 1, GBC.BOTH));
		add(pbarCompute, new GBC(0, 2, GBC.HORIZONTAL).setGridWidth(GBC.REMAINDER));
		add(butCompute, new GBC(0, 3).setAnchor(GBC.WEST));
		add(Box.createGlue(), new GBC(0, 4, GBC.VERTICAL));
	}

	/*
	 * Operations
	 */

	/**
	 * Initialize the dialog settings from the chart.
	 * @param accountingChart
	 */
	public void init(IChartCreator accountingChart) {

		if (accountingChart != null) {

			log.debug("init with " + accountingChart.getClass());

			chartGroup.clearSelection();

			JToggleButton button = mapButtonsChart.get(accountingChart.getClass());

			if (button != null) {
				button.setSelected(true);
			}
			else {
				log.error("No chart could be identified for class " + accountingChart.getClass().getName());
			}
		}
	}

	public IChartCreator buildChart() {

		IChartCreator chartCreator = null;

		try {

			boolean found = false;

			Iterator<Entry<Class<? extends IChartCreator>, JToggleButton>> it = 
					mapButtonsChart.entrySet().iterator();

			Class<? extends IChartCreator> chartClass = null;

			while (it.hasNext() && ! found) {
				Entry<Class<? extends IChartCreator>, JToggleButton> e = it.next();
				if (e.getValue().isSelected()) {
					found = true;
					chartClass = e.getKey();
				}
			}

			Constructor<? extends IChartCreator> c = chartClass.getConstructor();
			chartCreator = c.newInstance();
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return chartCreator;
	}

	/**
	 * Check the validity of the form
	 */
	public void check() {

	}

	/*
	 * Getters/Setters
	 */

	public JButton getComputeButton() {
		return butCompute;
	}

	public void setDimensionCount(int dimensionCount) {

		if (dimensionCount <= chartButtons.length) {

			for (int i = 0; i < chartButtons.length; i++) {
				
				JToggleButton[] buttons = chartButtons[i];

				boolean enabled = (i == (dimensionCount - 1));
				
				for (JToggleButton button : buttons) {
					button.setEnabled(enabled);
				}
			}
		}
	}
}
