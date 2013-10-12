package com.redorb.mcharts.ui;

import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.charts.ChartFileInfo;
import com.redorb.mcharts.core.charts.ChartsCategory;
import com.redorb.mcharts.io.layout.LayoutReader;
import com.redorb.mcharts.ui.models.ChartCategoryTreeModel;
import com.redorb.mcharts.ui.renderers.ChartCategoryRenderer;

/**
 * Panel showing charts categories and charts, for template and user's charts.
 */
@SuppressWarnings("serial")
public class ChartsCategoryPane extends JPanel {

	/*
	 * Attributes
	 */

	public static final String CMD_TPL_CHART = "templateChart";
	public static final String CMD_USER_CHART= "userChart";

	private final Logger log = LoggerFactory.getLogger(ChartsCategoryPane.class);

	private final List<ActionListener> listeners = new LinkedList<ActionListener>();

	private JLabel lblTemplate;
	private JTree treeCharts;
	private JScrollPane scrolTreeCharts;

	private JLabel lblUserCharts;
	private JTree userTreeCharts;
	private ChartCategoryTreeModel userChartsModel;
	private JScrollPane scrolUserTreeCharts;

	private JButton butAdd;
	private JButton butRemove;

	private JPopupMenu popupMenu;
	private Point popupMenuLocation;

	/*
	 * Ctors
	 */

	public ChartsCategoryPane() {
		super();
		initComponents();
		initLayout();
	}

	/**
	 * Initialize the components.
	 */
	private void initComponents() {

		// popup menu

		popupMenu = new JPopupMenu();
		JMenuItem mnuEdit = new JMenuItem(I18n.getMessage("action.rename"));
		mnuEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreePath treepath = userTreeCharts.getClosestPathForLocation(
						popupMenuLocation.x, popupMenuLocation.y);


				if (treepath != null) {
					treepath.getLastPathComponent();
					userTreeCharts.startEditingAtPath(treepath);
				}
			}
		});

		popupMenu.add(mnuEdit);

		// template tree

		lblTemplate = new JLabel(I18n.getMessage("chartsCategoryPane.lblTemplate"));

		treeCharts = new JTree();
		treeCharts.setEnabled(false);
		treeCharts.setRootVisible(false);
		treeCharts.setShowsRootHandles(true);

		treeCharts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() > 1) {
					onTemplateTreeSelect();
				}
			}
		});

		scrolTreeCharts = new JScrollPane();
		scrolTreeCharts.setViewportView(treeCharts);

		// user tree

		lblUserCharts = new JLabel(I18n.getMessage("chartsCategoryPane.lblUserCharts"));

		userTreeCharts = new JTree();
		userTreeCharts.setEnabled(false);
		userTreeCharts.setRootVisible(false);
		userTreeCharts.setShowsRootHandles(true);
		userTreeCharts.setEditable(true);

		userTreeCharts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() > 1) {
					onUserTreeSelect();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupMenu.show((JComponent) e.getSource(), e.getX(), e.getY());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupMenuLocation = e.getPoint();
					popupMenu.show((JComponent) e.getSource(), popupMenuLocation.x, popupMenuLocation.y);
					System.out.println("y:" + popupMenu.getY());
				}
			}
		});

		scrolUserTreeCharts = new JScrollPane();
		scrolUserTreeCharts.setViewportView(userTreeCharts);

		// buttons

		butAdd = Utils.createButton(
				"/images/22x22/add.png", 
				"", "", 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						onAddCategory();
					}
				}, false);

		butRemove = Utils.createButton(
				"/images/22x22/remove.png", 
				"", "", 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						onRemove();
					}
				}, 
				false);
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		setLayout(new GridBagLayout());

		// buttons

		JPanel pnlButtons = new JPanel();

		pnlButtons.add(butAdd);
		pnlButtons.add(butRemove);

		add(lblTemplate, new GBC(0, 0).setAnchor(GBC.WEST));
		add(scrolTreeCharts, new GBC(0, 1, GBC.BOTH));

		add(lblUserCharts, new GBC(0, 2).setAnchor(GBC.WEST));
		add(scrolUserTreeCharts, new GBC(0, 3, GBC.BOTH).setWeighty(3d));

		add(pnlButtons, new GBC(0, 4).setAnchor(GBC.WEST));
	}

	/**
	 * Initializes the tree by adding charts.
	 */
	public void initTree() {

		// read the template layout

		LayoutReader layoutReader = new LayoutReader(ChartsCategoryPane.class.getResourceAsStream(Conf.LAYOUT_RESOURCE));
		layoutReader.setBasePath(Conf.TEMPLATE_DIR);
		layoutReader.read();

		Ui.getInstance().setTemplateCategories(layoutReader.getCategories());

		treeCharts.setModel(new ChartCategoryTreeModel(treeCharts, layoutReader.getCategories()));
		treeCharts.setCellRenderer(new ChartCategoryRenderer(false));

		for (int row = treeCharts.getRowCount() - 1; row >= 0; row--) {
			treeCharts.expandRow(row);
		}

		// read the user layout

		try {

			List<ChartsCategory> categories = null;

			File userLayoutFile = new File(Conf.USER_LAYOUT_FILE);

			if (userLayoutFile.exists()) {

				LayoutReader userLayoutReader = new LayoutReader(new FileInputStream(userLayoutFile));
				userLayoutReader.setBasePath(userLayoutFile.getParent());
				userLayoutReader.read();

				Ui.getInstance().setUserCategories(userLayoutReader.getCategories());
				categories = userLayoutReader.getCategories();
			}
			else {
				categories = new ArrayList<ChartsCategory>();
			}

			userChartsModel = new ChartCategoryTreeModel(userTreeCharts, categories);
			userTreeCharts.setModel(userChartsModel);
			userTreeCharts.setCellRenderer(new ChartCategoryRenderer(true));

			for (int row = userTreeCharts.getRowCount() - 1; row >= 0; row--) {
				userTreeCharts.expandRow(row);
			}

		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Enables the main frame (when a file is opened : otherwise it is disabled)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		treeCharts.setEnabled(enabled);
		userTreeCharts.setEnabled(enabled);
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	/*
	 * Operations
	 */

	/**
	 * Check if a chart can be added.
	 * @param chartName
	 * @return
	 */
	public boolean checkAddChartOk() {

		// get tree path

		TreePath treepath = userTreeCharts.getSelectionPath();

		if (treepath == null) {
			JOptionPane.showMessageDialog(this, 
					I18n.getMessage("msg.selectCategory"), 
					I18n.getMessage("common.error"), 
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/**
	 * Look in current category if a chart with same name exists.
	 * @param chartName
	 * @return
	 */
	public ChartFileInfo existChart(String chartName) {

		ChartFileInfo chartFileInfo = null;

		TreePath treepath = userTreeCharts.getSelectionPath();

		if (treepath != null) {

			// get category

			ChartsCategory category = (ChartsCategory) treepath.getPathComponent(1);

			// search for a chart with same name

			for (int i = 0; i < category.getCount() && chartFileInfo == null; i++) {
				ChartFileInfo curChartFileInfo = category.getChart(i);

				if (curChartFileInfo.getName().equals(chartName)) {
					chartFileInfo = curChartFileInfo;
				}
			}
		}

		return chartFileInfo;
	}

	public void addChart(ChartFileInfo chartFileInfo) {

		// get tree path

		TreePath treepath = userTreeCharts.getSelectionPath();

		if (treepath == null) {
			JOptionPane.showMessageDialog(this, 
					I18n.getMessage("msg.selectCategory"), 
					I18n.getMessage("common.error"), 
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		ChartsCategory category = (ChartsCategory) treepath.getPathComponent(1);

		userChartsModel.addChart(category, chartFileInfo);
	}

	/*
	 * Events
	 */

	/**
	 * Click in the tree.
	 */
	private void onTemplateTreeSelect() {

		TreePath selectedPath = treeCharts.getSelectionPath();
		onTreeSelect(selectedPath, CMD_TPL_CHART);
	}

	/**
	 * Click in the tree.
	 */
	private void onUserTreeSelect() {

		TreePath selectedPath = userTreeCharts.getSelectionPath();
		onTreeSelect(selectedPath, CMD_USER_CHART);
	}

	/**
	 * Double click on a chart. Raise an action event on all listeners.
	 * @param selectedPath the tree path
	 * @param command the action event command
	 */
	private void onTreeSelect(TreePath selectedPath, String command) {

		if (selectedPath != null) {

			Object leaf = selectedPath.getLastPathComponent();

			// gets the leaf

			if (leaf instanceof ChartFileInfo) {

				ChartFileInfo chartFile = (ChartFileInfo) leaf;

				if (chartFile.getFile().exists()) {

					ActionEvent actionEvent = new ActionEvent(chartFile, 0, command);

					for (ActionListener listener : listeners) {
						listener.actionPerformed(actionEvent);
					}
				}
				else {
					log.error("Could not find chart file " + chartFile.getFile().getAbsolutePath());
				}
			}
		}
	}

	/**
	 * Adds a new category.
	 */
	private void onAddCategory() {

		String catName = JOptionPane.showInputDialog(this, "", "newCategory");

		if (catName != null) {

			List<ChartsCategory> categories = Ui.getInstance().getUserCategories();

			// check if the category already exists

			for (int i = 0; i < categories.size(); i++) {
				if (categories.get(i).getName().equals(catName)) {
					JOptionPane.showMessageDialog(this, 
							I18n.getMessage("error.catAlreadyExists", catName),
							I18n.getMessage("common.error"), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// create the category

			ChartsCategory category = new ChartsCategory(catName);

			userChartsModel.addCategory(category);
		}
	}

	/**
	 * Removes a graph or an entire category.
	 */
	private void onRemove() {

		TreePath selectedPath = userTreeCharts.getSelectionPath();
		int row = userTreeCharts.getSelectionRows()[0];

		if (selectedPath != null) {

			Object selectedObject = selectedPath.getLastPathComponent();

			// gets the leaf

			if (selectedObject instanceof ChartsCategory) {

				userChartsModel.removeCategory((ChartsCategory) selectedObject);
			}
			else if (selectedObject instanceof ChartFileInfo) {

				ChartsCategory category = (ChartsCategory)  selectedPath.getPathComponent(1);
				userChartsModel.removeChart(category, (ChartFileInfo) selectedObject);
			}

			// change selection

			if (row == userTreeCharts.getRowCount() && row > 0) {
				row--;
			}

			userTreeCharts.setSelectionRow(row);
		}
	}
}
