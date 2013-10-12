package com.redorb.mcharts.ui;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.RecentObjectsMenu;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.charts.ChartFileInfo;
import com.redorb.mcharts.core.charts.ChartsCategory;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.restriction.GlobalNFirstRestriction;
import com.redorb.mcharts.data.restriction.LocalNFirstRestriction;
import com.redorb.mcharts.ui.charts.Bar3DChart;
import com.redorb.mcharts.ui.charts.BarChart;
import com.redorb.mcharts.ui.charts.LinesChart;
import com.redorb.mcharts.ui.charts.Pie3DChart;
import com.redorb.mcharts.ui.charts.PieChart;
import com.redorb.mcharts.ui.charts.StackedAreasChart;
import com.redorb.mcharts.ui.charts.MultiplePieCharts;
import com.redorb.mcharts.utils.TimePeriodType;

public class Ui {

	/*
	 * Singleton design pattern
	 */

	private static Ui instance = null;

	public static Ui getInstance() {
		if (instance == null) {
			instance = new Ui();
		}

		return instance;
	}

	/*
	 * static methods
	 */

	private final static Map<Class, String> mapRestrictionName = 
			new HashMap<>();

	private final static Map<Dimension, String> mapDimensionsName =
			new HashMap<>();
	
	private final static Map<Class, String> mapAccountingObjectName =
			new HashMap<>();
	
	private final static Map<Object, Icon> mapAccountingObjectIcon =
			new HashMap<>();

	private final static Map<Object, String> mapTimePeriodName = 
			new HashMap<>();
	
	private final static Map<Dimension, Icon> mapDimensionIcon =
			new HashMap<>();
	
	private final static Map<Object, Icon> mapTimePeriodIcon = 
			new HashMap<>();

	private final static Map<Object, String> mapChartName = 
			new HashMap<>();

	public static Map<Object, String> getMapChartNames() {

		if (mapChartName.isEmpty()) {
			mapChartName.put(
					LinesChart.class, 
					I18n.getMessage("common.linesChart"));
			mapChartName.put(
					PieChart.class, 
					I18n.getMessage("common.pieChart"));
			mapChartName.put(
					MultiplePieCharts.class, 
					I18n.getMessage("common.multiplePieCharts"));
			mapChartName.put(
					BarChart.class, 
					I18n.getMessage("common.barChart"));
			mapChartName.put(
					Bar3DChart.class,
					I18n.getMessage("common.bar3DChart"));
			mapChartName.put(
					StackedAreasChart.class, 
					I18n.getMessage("common.stackedAreasChart"));
			mapChartName.put(
					Pie3DChart.class, 
					I18n.getMessage("common.pie3DChart"));
		}

		return mapChartName;
	}

	public static Map<Class, String> getMapRestrictionNames() {

		if (mapRestrictionName.isEmpty()) {
			mapRestrictionName.put(
					LocalNFirstRestriction.class,
					I18n.getMessage("common.localNFirstRestriction"));
			mapRestrictionName.put(
					GlobalNFirstRestriction.class, 
					I18n.getMessage("common.globalNFirstRestriction"));
		}

		return mapRestrictionName;
	}
	
	public static Map<Dimension, String> getMapDimensionsNames() {

		if (mapDimensionsName.isEmpty()) {
			mapDimensionsName.put(
					Dimension.ACCOUNT, 
					I18n.getMessage("common.account"));
			mapDimensionsName.put(
					Dimension.CATEGORY, 
					I18n.getMessage("common.category"));
			mapDimensionsName.put(
					Dimension.SUB_CATEGORY, 
					I18n.getMessage("common.subcategory"));
			mapDimensionsName.put(
					Dimension.PAYEE, 
					I18n.getMessage("common.payee"));
			mapDimensionsName.put(
					Dimension.WEEK, 
					I18n.getMessage("common.week"));
			mapDimensionsName.put(
					Dimension.MONTH, 
					I18n.getMessage("common.month"));
			mapDimensionsName.put(
					Dimension.TRIMESTER, 
					I18n.getMessage("common.trimester"));
			mapDimensionsName.put(
					Dimension.SEMESTER, 
					I18n.getMessage("common.semester"));
			mapDimensionsName.put(
					Dimension.YEAR, 
					I18n.getMessage("common.year"));
		}

		return mapDimensionsName;
	}

	public static Map<Class, String> getMapAccountObjectNames() {

		if (mapAccountingObjectName.isEmpty()) {
			mapAccountingObjectName.put(
					Account.class, 
					I18n.getMessage("common.account"));
			mapAccountingObjectName.put(
					Category.class, 
					I18n.getMessage("common.category"));
			mapAccountingObjectName.put(
					Payee.class, 
					I18n.getMessage("common.payee"));
			
		}

		return mapAccountingObjectName;
	}
	
	public static Map<Dimension, Icon> getMapDimensionsIcons() {

		if (mapDimensionIcon.isEmpty()) {
			mapDimensionIcon.put(
					Dimension.ACCOUNT, 
					Utils.getIcon("/images/16x16/account.png"));
			mapDimensionIcon.put(
					Dimension.CATEGORY, 
					Utils.getIcon("/images/16x16/category.png"));
			mapDimensionIcon.put(
					Dimension.SUB_CATEGORY, 
					Utils.getIcon("/images/16x16/category.png"));
			mapDimensionIcon.put(
					Dimension.PAYEE,
					Utils.getIcon("/images/16x16/payee.png"));
			mapDimensionIcon.put(
					Dimension.WEEK, 
					Utils.getIcon("/images/16x16/timeperiodw.png"));
			mapDimensionIcon.put(
					Dimension.MONTH, 
					Utils.getIcon("/images/16x16/timeperiodm.png"));
			mapDimensionIcon.put(
					Dimension.TRIMESTER, 
					Utils.getIcon("/images/16x16/timeperiodt.png"));
			mapDimensionIcon.put(
					Dimension.SEMESTER, 
					Utils.getIcon("/images/16x16/timeperiods.png"));
			mapDimensionIcon.put(
					Dimension.YEAR, 
					Utils.getIcon("/images/16x16/timeperiody.png"));
		}

		return mapDimensionIcon;
	}
	
	public static Map<Object, Icon> getMapAccountObjectIcons() {

		if (mapAccountingObjectIcon.isEmpty()) {
			mapAccountingObjectIcon.put(
					Account.class, 
					Utils.getIcon("/images/16x16/account.png"));
			mapAccountingObjectIcon.put(
					Category.class, 
					Utils.getIcon("/images/16x16/category.png"));
			mapAccountingObjectIcon.put(
					Payee.class, 
					Utils.getIcon("/images/16x16/payee.png"));
		}

		return mapAccountingObjectIcon;
	}

	public static Map<Object, String> getMapTimePeriodNames() {

		if (mapTimePeriodName.isEmpty()) {
			mapTimePeriodName.put(
					TimePeriodType.WEEK, 
					I18n.getMessage("common.week"));
			mapTimePeriodName.put(
					TimePeriodType.MONTH, 
					I18n.getMessage("common.month"));
			mapTimePeriodName.put(
					TimePeriodType.TRIMESTER, 
					I18n.getMessage("common.trimester"));
			mapTimePeriodName.put(
					TimePeriodType.SEMESTER, 
					I18n.getMessage("common.semester"));
			mapTimePeriodName.put(
					TimePeriodType.YEAR, 
					I18n.getMessage("common.year"));
		}

		return mapTimePeriodName;
	}
	
	public static Map<Object, Icon> getMapTimePeriodIcons() {

		if (mapTimePeriodIcon.isEmpty()) {
			mapTimePeriodIcon.put(
					TimePeriodType.WEEK, 
					Utils.getIcon("/images/16x16/timeperiodw.png"));
			mapTimePeriodIcon.put(
					TimePeriodType.MONTH, 
					Utils.getIcon("/images/16x16/timeperiodm.png"));
			mapTimePeriodIcon.put(
					TimePeriodType.TRIMESTER, 
					Utils.getIcon("/images/16x16/timeperiodt.png"));
			mapTimePeriodIcon.put(
					TimePeriodType.SEMESTER, 
					Utils.getIcon("/images/16x16/timeperiods.png"));
			mapTimePeriodIcon.put(
					TimePeriodType.YEAR, 
					Utils.getIcon("/images/16x16/timeperiody.png"));
		}

		return mapTimePeriodIcon;
	}

	private static RecentObjectsMenu<File> recentFiles =
			new RecentObjectsMenu<File>(4);

	public static RecentObjectsMenu<File> getRecentFiles() {
		return recentFiles;
	}
	
	/*
	 * Attributes
	 */

	private List<ChartsCategory> templateCategories;
	
	private List<ChartsCategory> userCategories;

	/*
	 * Ctors
	 */

	protected Ui() {}

	/*
	 * Getters/Setters
	 */
	
	public ChartsCategory getTemplateChartsCategory(int index) {
		return templateCategories.get(index);
	}

	public int getTemplateChartsCategoryCount() {
		return templateCategories.size();
	}

	public ChartsCategory getUserChartsCategory(int index) {
		return userCategories.get(index);
	}

	public int getUserChartsCategoryCount() {
		return userCategories.size();
	}

	/**
	 * @return the templateCategories
	 */
	public List<ChartsCategory> getTemplateCategories() {
		return templateCategories;
	}

	/**
	 * @param templateCategories the templateCategories to set
	 */
	public void setTemplateCategories(List<ChartsCategory> templateCategories) {
		this.templateCategories = templateCategories;
	}

	/**
	 * @return the userCategories
	 */
	public List<ChartsCategory> getUserCategories() {
		return userCategories;
	}

	/**
	 * @param userCategories the userCategories to set
	 */
	public void setUserCategories(List<ChartsCategory> userCategories) {
		this.userCategories = userCategories;
	}
	
	/**
	 * Create a node hierarchy for categories.
	 * @param categories
	 * @return
	 */
	public static DefaultMutableTreeNode createCategoryTree(List<ChartsCategory> categories) {
		
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
		
		for (ChartsCategory category : categories) {
			
			DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(category);
			
			for (ChartFileInfo chart : category.getCharts()) {
				DefaultMutableTreeNode chartNode = new DefaultMutableTreeNode(chart);
				catNode.add(chartNode);
			}
			
			rootNode.add(catNode);
		}
		
		return rootNode;
	}
}
