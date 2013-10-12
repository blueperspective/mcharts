package com.redorb.mcharts.io.layout;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.redorb.mcharts.core.charts.ChartFileInfo;
import com.redorb.mcharts.core.charts.ChartsCategory;
import com.redorb.mcharts.io.IReader;
import com.redorb.mcharts.io.grisbi.XmlConstants;

public class LayoutReader implements IReader {

	/*
	 * Attributes
	 */

	/** log. */
	private final Logger log = LoggerFactory.getLogger(LayoutReader.class);

	private InputStream is = null;

	private final List<ChartsCategory> categories = new ArrayList<ChartsCategory>();
	
	/** optional base path, used for template layout */
	private String basePath = null;

	/*
	 * Ctors
	 */

	public LayoutReader(InputStream is) {
		this.is = is;
	}

	/*
	 * Operations
	 */

	@Override
	public void read() {

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			// categories

			NodeList categoriesList = doc.getElementsByTagName(XmlConstants.XML_ELT_CHART_CATEGORY);

			for (int c = 0; c < categoriesList.getLength(); c++) {
				ChartsCategory category = readCategory((Element) categoriesList.item(c));	
				categories.add(category);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Creates the category.
	 */
	protected ChartsCategory readCategory(Element nCategory) {

		ChartsCategory category = new ChartsCategory(nCategory.getAttribute(XmlConstants.XML_ATT_NAME));

		NodeList charts = nCategory.getElementsByTagName(XmlConstants.XML_ELT_CHART);

		for (int c = 0; c < charts.getLength(); c++) {

			Element eltChart = (Element) charts.item(c);
			String chartName = eltChart.getAttribute(XmlConstants.XML_ATT_NAME);
			String chartFilepath = eltChart.getAttribute(XmlConstants.XML_ATT_FILE);
			
			// prepend base path if any
			if (basePath != null) {
				chartFilepath = basePath + File.separator + chartFilepath;
			}

			category.add(new ChartFileInfo(chartName, chartFilepath));
		}

		return category;
	}

	/*
	 * Getters/Setters
	 */

	/**
	 * @return the categories
	 */
	public List<ChartsCategory> getCategories() {
		return categories;
	}

	/**
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * @param basePath the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}
