package com.redorb.mcharts.ui;

import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.IUseObjectListener;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.io.grisbi.GsbReader;
import com.redorb.mcharts.perf.Perf;
import com.redorb.mcharts.ui.components.SidebarPanel;
import com.redorb.mcharts.ui.config.ConfigPanel;
import com.redorb.mcharts.ui.dialogs.MainPane;
import com.redorb.mcharts.ui.dialogs.MessageTankDialog;
import com.redorb.mcharts.ui.explorer.ExplorerDialog;
import com.redorb.mcharts.ui.misc.ExtensionFilter;
import com.redorb.mcharts.ui.monitoring.ChartMonitoringPanel;
import com.redorb.mcharts.ui.monitoring.TableMonitoringPanel;
import com.redorb.mcharts.ui.panels.DashboardPanel;

@SuppressWarnings("serial")
public class MchartFrame extends JFrame {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(MchartFrame.class);

	private JButton butOpen;
	private JPopupMenu mRecentFilesPopup;
	
	private JToggleButton butHome;
	private JToggleButton butExplorer;
	private JToggleButton butCharts;
	private JToggleButton butTableFollower;
	private JToggleButton butChartFollower;
	private JToggleButton butConfigure;

	private SidebarPanel sidebarPanel;

	private JPanel currentPanel;
	private DashboardPanel homePanel;
	private ExplorerDialog explorerPanel;
	private MainPane customChartPanel;
	private ChartMonitoringPanel chartMonitoringPanel;
	private TableMonitoringPanel tableMonitoringPanel;
	private ConfigPanel configPanel;

	private enum HomeView {
		HOME,
		EXPLORER,
		CHARTS,
		CHARTS_MONITORING,
		TABLE_MONITORING,
		CONFIG
	}

	/*
	 * Ctors
	 */

	public MchartFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(I18n.getMessage("mainFrame.title"));
		setIconImage(Utils.getImageIcon("/images/mcharts16x16.png", "").getImage());
		initComponents();
		initLayout();
		
		enableFrame(false);
		setSize(1200, 800);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

	/*
	 * Operations
	 */

	private void initComponents() {

		sidebarPanel = new SidebarPanel();
		
		Ui.getRecentFiles().addListener(new IUseObjectListener<File>() {
			@Override
			public void useObject(File file) {
				open(file);
			}
		});
		mRecentFilesPopup = Ui.getRecentFiles().buildPopupMenu();

		butOpen = new JButton(I18n.getMessage("menu.open"), Utils.getIcon("/images/22x22/open.png"));
		sidebarPanel.addButton(butOpen);
		
		butOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				if (MouseEvent.BUTTON1 == e.getButton()) {
					onOpenFile();
				}
				else {
					showRecentFiles();
				}
			}
		});

		ButtonGroup buttonGroup = new ButtonGroup();

		butHome = new JToggleButton(Utils.getIcon("/images/64x64/home.png"));
		butHome.setBorderPainted(false);
		butHome.setFocusPainted(false);
		buttonGroup.add(butHome);
		sidebarPanel.addButton(butHome);

		butHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onChangeView(HomeView.HOME);
			}
		});

		butExplorer = new JToggleButton(Utils.getIcon("/images/64x64/explorer.png"));
		butExplorer.setBorderPainted(false);
		butExplorer.setFocusPainted(false);
		buttonGroup.add(butExplorer);
		sidebarPanel.addButton(butExplorer);

		butExplorer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onChangeView(HomeView.EXPLORER);
			}
		});

		butCharts = new JToggleButton(Utils.getIcon("/images/64x64/charts.png"));
		butCharts.setBorderPainted(false);
		butCharts.setFocusPainted(false);
		buttonGroup.add(butCharts);
		sidebarPanel.addButton(butCharts);
		
		butCharts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onChangeView(HomeView.CHARTS);
			}
		});

		butTableFollower = new JToggleButton(Utils.getIcon("/images/64x64/tableFollower.png"));
		butTableFollower.setBorderPainted(false);
		butTableFollower.setFocusPainted(false);
		buttonGroup.add(butTableFollower);
		sidebarPanel.addButton(butTableFollower);

		butTableFollower.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onChangeView(HomeView.TABLE_MONITORING);
			}
		});

		butChartFollower = new JToggleButton(Utils.getIcon("/images/64x64/chartFollower.png"));		
		butChartFollower.setBorderPainted(false);
		butChartFollower.setFocusPainted(false);
		buttonGroup.add(butChartFollower);
		sidebarPanel.addButton(butChartFollower);

		butChartFollower.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onChangeView(HomeView.CHARTS_MONITORING);
			}
		});

		butConfigure = new JToggleButton(Utils.getIcon("/images/64x64/configure.png"));
		butConfigure.setBorderPainted(false);
		butConfigure.setFocusPainted(false);
		buttonGroup.add(butConfigure);
		sidebarPanel.addButton(butConfigure);
		
		butConfigure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				onChangeView(HomeView.CONFIG);
			}
		});

		sidebarPanel.endLayout();

		homePanel = new DashboardPanel();
		customChartPanel = new MainPane();
		chartMonitoringPanel = new ChartMonitoringPanel();
		tableMonitoringPanel = new TableMonitoringPanel();
		configPanel = new ConfigPanel(false);
	}

	private void initLayout() {

		setLayout(new GridBagLayout());

		add(sidebarPanel, new GBC(0, 0, GBC.VERTICAL));
		add(Box.createHorizontalGlue(), new GBC(1, 2, GBC.HORIZONTAL));
	}

	private void onChangeView(HomeView view) {

		if (currentPanel != null) { remove(currentPanel); }
		invalidate();

		switch (view) {
		case HOME:
			if (! homePanel.isRendered()) { homePanel.render(); }
			currentPanel = homePanel;
			break;
		case EXPLORER:
			currentPanel = explorerPanel;
			break;
		case CHARTS:
			currentPanel = customChartPanel;
			break;
		case TABLE_MONITORING:
			if (! tableMonitoringPanel.isRendered()) { tableMonitoringPanel.render(); }
			currentPanel = tableMonitoringPanel;
			break;
		case CHARTS_MONITORING:
			if (! chartMonitoringPanel.isRendered()) { chartMonitoringPanel.render(); }
			currentPanel = chartMonitoringPanel;
			break;
		case CONFIG:
			currentPanel = configPanel;
			break;
		}

		add(currentPanel, new GBC(1, 0, GBC.BOTH));
		validate();
		repaint();
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
	 * Pops up the recent file menu.
	 */
	public void showRecentFiles() {

		Point location = getMousePosition();
		mRecentFilesPopup.show(this, location.x, location.y);
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
						butHome.setSelected(true);
						onChangeView(HomeView.HOME);
						explorerPanel = new ExplorerDialog();
						enableFrame(true);
					}
					else {
						MessageTankDialog.getInstance().showMessages();
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					MessageTank.getInstance().error(e.getMessage());
				}
			}			
		}.execute();
	}

	public void enableFrame(boolean enabled) {

		butHome.setEnabled(enabled);
		butExplorer.setEnabled(enabled);
		butCharts.setEnabled(enabled);
		butTableFollower.setEnabled(enabled);
		butChartFollower.setEnabled(enabled);
	}
}
