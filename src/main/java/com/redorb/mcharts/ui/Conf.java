package com.redorb.mcharts.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.CommonProperties;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.io.IWriter;
import com.redorb.mcharts.io.config.ListReader;
import com.redorb.mcharts.io.config.ListWriter;
import com.redorb.mcharts.utils.Utils;

/**
 * Saves and loads MCharts configuration.
 */
public class Conf {

	/*
	 * Final attributes
	 */

	private final Logger log = LoggerFactory.getLogger(Conf.class);

	public static final String NEWLINE = System.getProperty("line.separator");

	public static final String LAYOUT_RESOURCE = "/layout.xml";	

	public static final String MCHARTS_DIR = 
			System.getProperty("user.home") + File.separator + ".mcharts";

	public static final String TEMPLATE_DIR = 
			MCHARTS_DIR + File.separator + "templates";

	/** Configuration file. */
	private static final String CONFIG_FILE =
			MCHARTS_DIR + File.separator + "mcharts.properties";

	private static final String SAVES_DIR = 
			MCHARTS_DIR + File.separator + "saves";

	public static final String USER_LAYOUT_FILE = 
			SAVES_DIR + File.separator + "layout.xml";

	/** text indicating true boolean value */
	private static final String VALUE_TRUE = "true";

	/** text indicating false boolean value */
	private static final String VALUE_FALSE = "false";

	/*
	 * recent
	 */

	/** File path of recent file list. */
	public static final String PROP_RECENT_FILE = "recentFileList";

	/*
	 * Ignore list
	 */

	/** File path of ignore list file. */
	public static final String PROP_IGNORE_LIST = "ignoreList";

	public static final String PROP_USE_IGNORE_LIST = "ignoreList.use";

	public static final String PROP_SAVE_DIR = "saveDir";

	public static final String PROP_TEMPLATE_FILE= "templateFile";

	public static final String PROP_LAYOUT_FILE = "layoutFile";

	public static final String PROP_SHOW_TRANSACTIONS = "showTransactionPane";

	public static final String PROP_SHOW_TREES = "showTreesPane";

	public static final String PROP_LAST_OPEN_DIR = "lastOpenDir";

	public static final String PROP_LAST_SAVE_DIR = "lastSaveDir";

	/*
	 * i18n
	 */

	/** country for i18n */
	public static final String PROP_COUNTRY = "i18n.country";

	/** language for i18n */
	public static final String PROP_LANGUAGE = "i18n.language";

	public static final String LANGUAGE_FR = "fr";
	public static final String COUNTRY_FR = "FR";

	public static final String LANGUAGE_EN = "en";
	public static final String COUNTRY_EN = "GB";	

	/*
	 * Attributes
	 */

	private final static Object LOCK = new Object();

	private static Conf instance = null;

	public static Conf getInstance() {

		boolean doInit = false;

		synchronized (LOCK) {
			if (instance == null) {
				instance = new Conf();
				doInit = true;
			}
		}

		if (doInit) {
			instance.firstRun();
			CommonProperties.setProps(instance.props);
		}

		return instance;
	}

	public static PropertiesConfiguration getProps() {
		return getInstance().props;
	}

	/** properties */
	private PropertiesConfiguration props = new PropertiesConfiguration();

	/*
	 * Ignore list
	 */

	/** use ignore list */
	private boolean useIgnoreList = false;

	/** list of ignored objects */
	private List<String> ignoreList = null;

	/*
	 * Ctors
	 */

	protected Conf() {}

	/*
	 * Operations
	 */

	public void firstRun() {

		File tplDir = new File(TEMPLATE_DIR);

		if (! tplDir.exists()) {

			tplDir.mkdirs();

			File[] files = tplDir.listFiles();

			if (files == null || files.length == 0) 
			{
				// copy template file
				Utils.extractFromJar("/charts/lineAmount.xml", TEMPLATE_DIR + File.separator + "lineAmount.xml");
				Utils.extractFromJar("/charts/pieCategory.xml", TEMPLATE_DIR + File.separator + "pieCategory.xml");
				Utils.extractFromJar("/charts/piePayee.xml", TEMPLATE_DIR + File.separator + "piePayee.xml");
				Utils.extractFromJar("/charts/stackCategories.xml", TEMPLATE_DIR + File.separator + "stackCategories.xml");
				Utils.extractFromJar("/charts/stackPayees.xml", TEMPLATE_DIR + File.separator + "stackPayees.xml");
			}

			I18n.setLanguage(LANGUAGE_EN, COUNTRY_EN);

			try {
				store();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Loads all properties.
	 * @throws IOException
	 */
	public void load() throws IOException {

		FileInputStream in = new FileInputStream(CONFIG_FILE);

		try {
			props.load(in);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (in != null) { in.close(); }
		}

		// i18n

		String language = props.getString(PROP_LANGUAGE);

		if (language == null) {
			language = LANGUAGE_FR;
		}

		String country = props.getString(PROP_COUNTRY);

		if (country == null) {
			country = COUNTRY_FR;
		}

		I18n.setLanguage(language, country);

		// recent files

		if (props.containsKey(PROP_RECENT_FILE)) {

			File recentFile = new File(props.getString(PROP_RECENT_FILE));

			if (recentFile.exists()) {

				ListReader recentReader = 
						new ListReader(
								new FileReader(recentFile));
				recentReader.read();

				for (String file : recentReader.getList()) {
					Ui.getRecentFiles().add(new File(file));
				}
			}
		}

		// ignore list

		if (props.containsKey(PROP_IGNORE_LIST)) {

			File ignoreFile = new File(props.getString(PROP_IGNORE_LIST));

			if (ignoreFile.exists()) {

				ListReader ignoreListReader = 
						new ListReader(
								new FileReader(ignoreFile));
				ignoreListReader.read();
				ignoreList = ignoreListReader.getList();
			}
		}

		CommonProperties.setProps(props);
	}

	/**
	 * Stores all properties
	 * @throws IOException
	 */
	public void store() throws Exception {

		// recent files
		// ------------

		if (! props.containsKey(PROP_RECENT_FILE)) {
			props.addProperty(PROP_RECENT_FILE, MCHARTS_DIR + File.separator + "recentFiles.txt");
		}

		List<String> recentFiles = new ArrayList<>();
		for (File f : Ui.getRecentFiles().getObjects()) {
			recentFiles.add(f.getAbsolutePath());
		}

		IWriter recentWriter = 
				new ListWriter(props.getString(PROP_RECENT_FILE), recentFiles);

		recentWriter.write();

		// ignore list
		// -----------

		if (props.containsKey(PROP_IGNORE_LIST)) {
			props.addProperty(PROP_IGNORE_LIST, MCHARTS_DIR + File.separator + "ignoreList.txt");
		}

		props.setProperty(PROP_USE_IGNORE_LIST, useIgnoreList ? VALUE_TRUE : VALUE_FALSE);

		if (ignoreList != null) {
			IWriter ignoreListWriter = 
					new ListWriter(props.getString(PROP_IGNORE_LIST), ignoreList);

			ignoreListWriter.write();
		}

		// stores properties

		FileOutputStream out = new FileOutputStream(CONFIG_FILE);
		props.save(out);
		out.close();
	}

	/*
	 * Getters/Setters
	 */

	public List<String> getIgnoreList() {
		return ignoreList;
	}

	public String getSavedChartsDir() {
		return SAVES_DIR;
	}
}
