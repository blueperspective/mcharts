package com.redorb.mcharts.io.layout;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.redorb.mcharts.core.charts.ChartsCategory;
import com.redorb.mcharts.io.IWriter;
import com.redorb.mcharts.io.grisbi.XmlConstants;

/**
 * Create a layout file.
 */
public class LayoutWriter implements IWriter {

	/*
	 * Attributes
	 */

	/** output file */
	private File file;
	
	/** categories */
	private List<ChartsCategory> categories;
	
	/** xml document */
	private Document doc;

	/*
	 * Ctors
	 */
	
	public LayoutWriter(final File file, final List<ChartsCategory> categories) {
		this.file = file;
		this.categories = categories;
	}

	/*
	 * Operations
	 */

	public void write() throws Exception {
		
		if (categories == null || categories.isEmpty()) {
			return;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();

		doc = impl.createDocument(null, null, null);

		Element eltRoot = doc.createElement(XmlConstants.XML_ELT_LAYOUT);

		addCategories(categories, eltRoot);

		doc.appendChild(eltRoot);

		// write XML

		// Prepare the DOM document for writing
		Source source = new DOMSource(doc);

		// Prepare the output file
		Result result = new StreamResult(file);

		// Write the DOM document to the file
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);
	}

	private void addCategories(final List<ChartsCategory> categories, final Element parent) {

		for (ChartsCategory category : categories) {

			Element eltCategory = doc.createElement(XmlConstants.XML_ELT_CHART_CATEGORY);
			eltCategory.setAttribute(XmlConstants.XML_ATT_NAME, category.getName());

			// for each chart
			for (int j = 0; j < category.getCount(); j++) {

				Element eltChart = doc.createElement(XmlConstants.XML_ELT_CHART);
				eltChart.setAttribute(XmlConstants.XML_ATT_NAME, category.getChart(j).getName());
				eltChart.setAttribute(XmlConstants.XML_ATT_FILE, category.getChart(j).getFile().getName());
				
				eltCategory.appendChild(eltChart);
			}

			parent.appendChild(eltCategory);
		}
	}
}
