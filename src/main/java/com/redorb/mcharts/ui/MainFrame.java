package com.redorb.mcharts.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.titledtab.TitledTab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.redorb.commons.ui.CenteredDialog;
import com.redorb.commons.ui.CommonProperties;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.IUseObjectListener;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.charts.ChartFileInfo;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.io.IWriter;
import com.redorb.mcharts.io.chart.ChartWriter;
import com.redorb.mcharts.io.grisbi.GsbReader;
import com.redorb.mcharts.io.layout.LayoutWriter;
import com.redorb.mcharts.perf.Perf;
import com.redorb.mcharts.ui.dialogs.MainPane;
import com.redorb.mcharts.ui.dialogs.MessageTankDialog;
import com.redorb.mcharts.ui.misc.ExtensionFilter;

/**
 * Application main class.
 * Creates the application main frame.
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	/*
	 * Attributes 
	 */

	private final Logger log = LoggerFactory.getLogger(MainFrame.class);

	private TabbedPanel tabPane;
	private ChartsCategoryPane chartsCatPane;

	private JMenuBar mbMainMenu;
	private JMenu mRecentFiles;
	private JPopupMenu mRecentFilesPopup;
	private JToolBar tbToolbar;
	private CenteredDialog explorerDialog;

	/*
	 * Ctors
	 */

	/**
	 * Builds the main frame.
	 */
	public MainFrame() {

		initMenuToolbar();
		initComponents();
		initLayout();
		chartsCatPane.initTree();

		setVisible(true);
	}

	/**
	 * Initializes the main frame components.
	 */
	private void initComponents() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(I18n.getMessage("mainFrame.title"));
		setIconImage(Utils.getImageIcon("/images/mcharts16x16.png", "").getImage());

		// charts category

		chartsCatPane = new ChartsCategoryPane();
		chartsCatPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onSelectChart(evt);
			}
		});

		// tab pane for graphs

		tabPane = new TabbedPanel();

		CommonProperties.restoreFrameProperties(this);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				onQuit();
			}
		});
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		setLayout(new BorderLayout());

		JSplitPane treeMainSplit = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				chartsCatPane,
				tabPane);
		treeMainSplit.setOneTouchExpandable(true);
		treeMainSplit.setDividerLocation(200);

		getContentPane().add(tbToolbar, BorderLayout.PAGE_START);
		getContentPane().add(treeMainSplit, BorderLayout.CENTER);
	}

	/*
	 * Operations
	 */
	
	/**
	 * Creates the menu bar.
	 */
	private void initMenuToolbar() {

		// action listeners

		ActionListener openAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onOpenFile();
			}
		};

		ActionListener recentAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onShowRecentFiles();
			}
		};

		ActionListener saveAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onSave();
			}
		};

		ActionListener explorerAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onShowFileExplorer();
			}
		};

		ActionListener configAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onShowConfiguration();
			}
		};

		ActionListener quitAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onQuit();
			}
		};
		
		ActionListener aboutAL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onAbout();
			}
		};

		// builds the menu

		JMenu mFile = new JMenu(I18n.getMessage("menu.file"));
		JMenuItem mOpen = new JMenuItem(I18n.getMessage("menu.open"));		
		JMenuItem mSaveCurrentChart = new JMenuItem(I18n.getMessage("menu.saveCurrentChart"));
		JMenuItem mFileQuit = new JMenuItem(I18n.getMessage("menu.quit"));

		JMenu mTools = new JMenu(I18n.getMessage("menu.tools"));
		JMenuItem mExplorer = new JMenuItem(I18n.getMessage("menu.explorer"));
		JMenuItem mConfigure = new JMenuItem(I18n.getMessage("menu.configure"));

		JMenu mHelp = new JMenu(I18n.getMessage("menu.help"));
		JMenuItem mHelpContent = new JMenuItem(I18n.getMessage("menu.helpContent"));
		JMenuItem mAbout = new JMenuItem(I18n.getMessage("menu.about"));
		
		// recent files

		Ui.getRecentFiles().addListener(new IUseObjectListener<File>() {
			@Override
			public void useObject(File file) {
				open(file);
			}
		});

		mRecentFiles = Ui.getRecentFiles().buildMenu();
		mRecentFiles.setText(I18n.getMessage("menu.recentFiles"));
		mRecentFilesPopup = Ui.getRecentFiles().buildPopupMenu();

		// menu layout

		mFile.add(mOpen);
		mFile.add(mRecentFiles);
		mFile.addSeparator();
		mFile.add(mSaveCurrentChart);
		mFile.addSeparator();
		mFile.add(mFileQuit);
		
		mTools.add(mExplorer);
		mTools.addSeparator();
		mTools.add(mConfigure);
		
		mHelp.add(mHelpContent);
		mHelp.addSeparator();
		mHelp.add(mAbout);

		// main menu

		mbMainMenu = new JMenuBar();
		mbMainMenu.add(mFile);
		mbMainMenu.add(mTools);
		mbMainMenu.add(mHelp);

		// adds action listeners

		mOpen.addActionListener(openAL);
		mExplorer.addActionListener(explorerAL);
		mSaveCurrentChart.addActionListener(saveAL);
		mConfigure.addActionListener(configAL);
		mFileQuit.addActionListener(quitAL);
		mAbout.addActionListener(aboutAL);

		setJMenuBar(mbMainMenu);

		// toolbar

		tbToolbar = new JToolBar();

		tbToolbar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
		tbToolbar.setFloatable(false);
		tbToolbar.setRollover(true);

		tbToolbar.add(Utils.createButton(
				"/images/32x32/open.png", 
				I18n.getMessage("menu.open.tooltip"),
				null, 
				openAL, 
				true));

		tbToolbar.add(Utils.createButton(
				"/images/32x32/recent.png", 
				I18n.getMessage("menu.recentFiles.tooltip"),
				null,
				recentAL,
				true));

		tbToolbar.addSeparator();

		tbToolbar.add(Utils.createButton(
				"/images/32x32/save.png", 
				I18n.getMessage("menu.saveCurrentChart.tooltip"),
				null, 
				saveAL,
				true));

		tbToolbar.addSeparator();

		tbToolbar.add(Utils.createButton(
				"/images/32x32/explorer.png", 
				I18n.getMessage("menu.explorer.tooltip"),
				null, 
				explorerAL,
				true));

		tbToolbar.add(Utils.createButton(
				"/images/32x32/configure.png", 
				I18n.getMessage("menu.configure.tooltip"),
				null, 
				configAL,
				true));
	}

	/**
	 * Adds a new main pane.
	 * @param chart the template chart from which to create a main pane
	 */
	private MainPane addMainPane(IChart chart) {

		//log.info("Create new main pane with template chart " + chart.getName());

		JPanel pnlTab = new JPanel();
		pnlTab.setLayout(new BoxLayout(pnlTab, HEIGHT));

		MainPane mainPane = new MainPane(chart);

		final TitledTab tab = new TitledTab(
				chart.getName(), 
				null, 
				mainPane, 
				null);

		JButton closeButton = Utils.createButtonNoBorder(
				"/images/22x22/close.png", "", "", 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tab.getTabbedPanel().removeTab(tab);
					}
				});

		tab.setHighlightedStateTitleComponent(closeButton);

		tabPane.addTab(tab);
		tabPane.setSelectedTab(tab);

		return mainPane;
	}

	/**
	 * Enables the main frame (when a file is opened : otherwise it is disabled)
	 */
	public void enableFrame() {
		chartsCatPane.setEnabled(true);
	}

	/**
	 * Open a file (in a worker thread)
	 * @param file
	 */
	private void open(final File file) {

		if (! file.exists()) {
			JOptionPane.showMessageDialog(this, I18n.getMessage("error.fileNotFound"));
		}

		new SwingWorker<Boolean, Void>() {

			@Override
			protected Boolean doInBackground() throws Exception {

				boolean res = true;

				try {
					Perf.getInstance().takeMeasure("start reading gsb file");
					GsbReader gsbReader = new GsbReader();
					gsbReader.read(file.getAbsolutePath());
					Ui.getRecentFiles().add(file);
					Perf.getInstance().takeMeasure("end reading gsb file");
				} catch (Exception e) {
					res = false;
					log.error(e.getMessage(), e);
					MessageTank.getInstance().error(e.getMessage());
				}

				return res;
			}

			@Override
			protected void done() {
				try {
					if (get()) {
						enableFrame();
					}
					else {
						MessageTankDialog.getInstance().showMessages();
					}
				} catch (Exception e) {
					MessageTank.getInstance().error(e.getMessage());
				}
			}			
		}.execute();
	}

	/*
	 * Events
	 */

	/**
	 * Pops up the recent file menu.
	 */
	public void onShowRecentFiles() {

		Point location = getMousePosition();
		mRecentFilesPopup.show(this, location.x, location.y);
	}

	/**
	 * Opens a new gsb file. Shows a open dialog.
	 * @param evt
	 */
	public void onOpenFile() {

		JFileChooser chooser = new JFileChooser();		
		
		File openDir = null;
		if (Conf.getProps().containsKey(Conf.PROP_LAST_OPEN_DIR)) {
			openDir = new File(Conf.getProps().getString(Conf.PROP_LAST_OPEN_DIR));
		}

		chooser.setCurrentDirectory(openDir);
		
		// file filters		
		FileFilter fileFilter = new ExtensionFilter(
				I18n.getMessage("common.gsbFile"), ".gsb");
		
		// extension filter
		chooser.addChoosableFileFilter(fileFilter);

		// default filter
		chooser.setFileFilter(fileFilter);
		
		int res = chooser.showOpenDialog(this);

		if (res == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			Conf.getProps().addProperty(Conf.PROP_LAST_OPEN_DIR, file.getParent());
			open(file);
		}
	}

	/**
	 * Opens the explorer dialog.
	 */
	public void onShowFileExplorer() {

		/*if (explorerDialog == null) {
			explorerDialog = new ExplorerDialog(this);
			explorerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			explorerDialog.setSize(900, 600);
		}

		explorerDialog.showCentered();
		*/
	}

	/**
	 * Saves the current chart.
	 */
	public void onSave() {

		MainPane mainPane = (MainPane) tabPane.getSelectedTab().getContentComponent();

		try {

			// chart exists ?

			if (mainPane.getChart() == null) {
				return;
			}

			// category ok ?

			if (! chartsCatPane.checkAddChartOk()) {
				return;
			}

			// get chart name

			String chartName = mainPane.getChartName();

			if (chartName.isEmpty()) {
				JOptionPane.showMessageDialog(this,
						I18n.getMessage("error.noChartName"), 
						I18n.getMessage("common.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			mainPane.getChart().setName(chartName);
			((TitledTab) tabPane.getSelectedTab()).setText(chartName);

			// existing charts ?

			ChartFileInfo chartFileInfo = chartsCatPane.existChart(chartName);

			// get chart file if existing, or create it if not

			String chartFile = null;

			if (chartFileInfo == null) {
				chartFile = 
						Conf.getProps().getString(Conf.PROP_SAVE_DIR) + File.separator +
						Long.toString(System.currentTimeMillis()) +
						".xml";
			}
			else {
				chartFile = chartFileInfo.getFile().getAbsolutePath();
			}

			// write file

			ChartWriter writer = new ChartWriter(
					chartFile,
					mainPane.getChart());
			writer.write();

			// add the chart if not existing

			if (chartFileInfo == null) {
				chartsCatPane.addChart(new ChartFileInfo(chartName, chartFile));
			}
			
			// inform everything went ok

			JOptionPane.showMessageDialog(this,
					I18n.getMessage("msg.saveOk", chartName), 
					I18n.getMessage("common.info"), JOptionPane.INFORMATION_MESSAGE);


		} catch (Exception e) {
			log.error(e.getMessage(), e);
			JOptionPane.showMessageDialog(this, 
					e.getClass().getName() + "/" + e.getMessage(), 
					I18n.getMessage("common.error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Show the configuration dialog.
	 * @param evt
	 */
	public void onShowConfiguration() {
/*
		CenteredDialog configDialog = new ConfigPanel(this, false);

		configDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		configDialog.setSize(900, 600);
		configDialog.showCentered();*/
	}

	/**
	 * Raised when a template or user chart has been double clicked.
	 * Creates a new main pane with the chart properties.
	 * @param evt
	 */
	public void onSelectChart(ActionEvent evt) {

		if (evt.getSource() instanceof ChartFileInfo) {
			ChartFileInfo chartFileInfo = (ChartFileInfo) evt.getSource();
			MainPane mainPane = addMainPane(chartFileInfo.getChart());

			if (ChartsCategoryPane.CMD_USER_CHART.equals(evt.getActionCommand())) {
				// the selected chart was in user's chart list, compute
				// and show the chart
				mainPane.computeChart();
			}
		}
	}

	/**
	 * Quits the application.
	 */
	public void onQuit() {

		if (explorerDialog != null) {
			explorerDialog.dispose();
			explorerDialog = null;
		}

		// write layout

		try {
			File layoutFile = new File(Conf.USER_LAYOUT_FILE);
			IWriter writer = new LayoutWriter(layoutFile, Ui.getInstance().getUserCategories());		
			writer.write();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		// sets properties

		CommonProperties.saveFrameProperties(this);

		// save the configuration

		try {
			Conf.getInstance().store();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		// dispose of the main frame

		this.dispose();
	}
	
	/**
	 * 
	 */
	public void onAbout() {
		
	}
}
