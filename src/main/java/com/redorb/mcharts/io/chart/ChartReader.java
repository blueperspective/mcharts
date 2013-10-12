package com.redorb.mcharts.io.chart;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.redorb.mcharts.ui.charts.ChartFactory;
import com.redorb.mcharts.ui.charts.IChartCreator;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.core.charts.Chart;
import com.redorb.mcharts.data.aggregation.aggregator.AggregatorFactory;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.criteria.filter.CriteriaFactory;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.restriction.IRestriction;
import com.redorb.mcharts.data.restriction.RestrictionFactory;
import com.redorb.mcharts.io.IReader;
import com.redorb.mcharts.io.grisbi.XmlConstants;

/**
 * Abstract reader for charts.
 */
public class ChartReader implements IReader {

	/*
	 * Attributes
	 */

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(ChartReader.class);


	/** File to read. */
	private File file = null;

	private IChart chart = null;

	/*
	 * Ctors
	 */

	/**
	 * Creates an ChartDefinitionReader
	 * @param file the file path of the Xml file to read
	 */
	public ChartReader(String file) {
		this.file = new File(file);
	}

	/**
	 * Creates an ChartDefinitionReader
	 * @param file the file path of the Xml file to read
	 */
	public ChartReader(File file) {
		this.file = file;
	}

	/*
	 * Operations
	 */

	/**
	 * Reads the Xml file to create the categories, charts and store it in Ui
	 * for display.
	 */
	public void read() {

		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList categories = doc.getElementsByTagName(XmlConstants.XML_ELT_CHART_DEFINITION);

			if (categories.getLength() > 0) {
				Element nChartDefinition = (Element) categories.item(0);
				chart = createChart(nChartDefinition);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Creates a chart from an Xml element
	 * @param nChartDefinition the element
	 * @return InstanciatedChart or null
	 */
	protected IChart createChart(Element nChartDefinition) {

		log.debug("nChartDefinition : " + nChartDefinition.getNodeName() + ", type " + nChartDefinition.getNodeType() + ";" + Node.TEXT_NODE);
		log.debug("nChartDefinition attributes : " + nChartDefinition.hasAttributes());

		// chart variables

		String chartTitle = nChartDefinition.getAttributes().getNamedItem(XmlConstants.XML_ATT_NAME).getNodeValue();
		String chartDescription = null;
		ICriteria criteria = null;
		IAggregator aggregator = null;
		IRestriction restriction = null;
		IChartCreator chartCreator = null;

		// child nodes

		NodeList elementsDefinitions = nChartDefinition.getChildNodes();

		log.debug(chartTitle + " ;" + elementsDefinitions.getLength());

		for (int ed = 0; ed < elementsDefinitions.getLength(); ed++) {

			Node nElement = elementsDefinitions.item(ed);

			if (Node.ELEMENT_NODE == nElement.getNodeType()) {

				String name = nElement.getNodeName();

				if (XmlConstants.XML_ELT_DESCRIPTION.equals(name)) {
					chartDescription = nElement.getTextContent();
				}
				else if (XmlConstants.XML_ELT_CRITERIA.equals(name)) {
					criteria = createCriteria((Element) nElement);
				}
				else if (XmlConstants.XML_ELT_AGGREGATOR.equals(name)) {
					aggregator = createAggregator((Element) nElement);
				}
				else if (XmlConstants.XML_ELT_RESTRICTION.equals(name)) {
					restriction = createRestriction((Element) nElement);
				}
				else if (XmlConstants.XML_ELT_CHART_CREATOR.equals(name)) {
					chartCreator = createChartCreator((Element) nElement);
				}
				else {
					log.warn("Unknown node: " + name);
				}
			}
		}

		// creates the chart

		Chart instanciatedChart = new Chart(
				chartTitle,
				chartDescription,
				criteria,
				aggregator,
				restriction,
				chartCreator);

		return instanciatedChart;
	}

	/**
	 * Creates the criteria from the Xml element
	 * @param nElement the element
	 * @return ICriteria or null
	 */
	protected ICriteria createCriteria(Element nElement) {

		ICriteria criteria = null;

		// gets node type

		String type = nElement.getAttribute(XmlConstants.XML_ATT_TYPE);

		log.debug("criteria of type " + type);

		// gets children

		NodeList children = nElement.getChildNodes();

		// list of sub criterias

		List<ICriteria> subCriterias = null;

		// list of parameters

		List<String> parametersNames = null;
		List<String> parametersValues = null;

		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);

			if (Node.ELEMENT_NODE == child.getNodeType()) {

				if (XmlConstants.XML_ELT_CRITERIA.equals(child.getNodeName())) {

					log.debug("contains sub criterias");

					if (subCriterias == null) {
						subCriterias = new ArrayList<ICriteria>();
					}
					// adds the sub criteria to the list
					subCriterias.add(createCriteria((Element)child));
				}
				else if (XmlConstants.XML_ELT_PARAMETER.equals(child.getNodeName())) {

					log.debug("final criteria : parameters and values");

					if (parametersNames == null) {
						parametersNames = new ArrayList<String>();
						parametersValues = new ArrayList<String>();
					}
					// adds parameter names and value to the lists
					parametersNames.add(child.getAttributes().getNamedItem(XmlConstants.XML_ATT_NAME).getNodeValue());
					parametersValues.add(child.getAttributes().getNamedItem(XmlConstants.XML_ATT_VALUE).getNodeValue());
				}
				else {
					log.error(MessageFormat.format("unknow child element of criteria : {0}",
							new Object[] {child.getNodeName()}));
				}
			}
		}

		if (subCriterias != null) {

			ICriteria[] array = new ICriteria[subCriterias.size()];
			for (int i = 0; i < array.length; i++) {
				array[i] = subCriterias.get(i);
			}

			criteria = CriteriaFactory.newInstance(type, array);
		}
		else {

			String[] arrayParams = null;
			String[] arrayValues = null;
			
			if (parametersNames != null && parametersValues != null) {
				arrayParams = new String[parametersNames.size()];
				arrayValues = new String[parametersValues.size()];
				
				for (int i = 0; i < arrayParams.length; i++) {
					arrayParams[i] = parametersNames.get(i);
					arrayValues[i] = parametersValues.get(i);
				}
			}

			criteria = CriteriaFactory.newInstance(type, arrayParams, arrayValues);
		}

		return criteria;
	}

	/**
	 * Creates an aggregator from the Xml element
	 * @param nElement the element
	 * @return IAggregator or null
	 */
	protected IAggregator createAggregator(Element nElement) {

		// aggregator returned

		IAggregator aggregator = null;

		String type = nElement.getAttribute(XmlConstants.XML_ATT_TYPE);

		// treats each parameter

		NodeList parameters = nElement.getElementsByTagName(XmlConstants.XML_ELT_PARAMETER);
		String[] dimensions = new String[parameters.getLength()];

		for (int p = 0; p < parameters.getLength(); p++) {

			Node nParameter = parameters.item(p);
			dimensions[p] = nParameter.getAttributes().getNamedItem(XmlConstants.XML_ATT_VALUE).getNodeValue();

			log.debug("aggregator with dimension " + p + ": " + dimensions[p]);
		}

		// calls the AggregatorFactory

		aggregator = AggregatorFactory.newInstance(type, dimensions);

		return aggregator;
	}

	/**
	 * Creates a restriction from an Xml element.
	 * @param nElement
	 * @return IRestriction or null
	 */
	protected IRestriction createRestriction(Element nElement) {

		// restriction returned

		IRestriction restriction = null;

		// gets the type of the restriction

		String type = nElement.getAttribute(XmlConstants.XML_ATT_TYPE);

		// treats each parameter

		NodeList parameters = nElement.getElementsByTagName(XmlConstants.XML_ELT_PARAMETER);

		String[] parameterNames = new String[parameters.getLength()];
		String[] parameterValues = new String[parameters.getLength()];

		for (int p = 0; p < parameters.getLength(); p++) {

			Node nParameter = parameters.item(p);

			// gets param name & value

			parameterNames[p] = nParameter.getAttributes().getNamedItem(XmlConstants.XML_ATT_NAME).getNodeValue();
			parameterValues[p] = nParameter.getAttributes().getNamedItem(XmlConstants.XML_ATT_VALUE).getNodeValue();

			log.debug("Creating restiction with param " + parameterNames[p] + " = " + parameterValues[p]);
		}

		// calls the RestrictionFactory

		restriction = RestrictionFactory.newInstance(type, parameterNames, parameterValues);

		return restriction;
	}

	/**
	 * Creates a chart from an Xml element.
	 * @param nElement the element
	 * @return IChartCreator or null
	 */
	protected IChartCreator createChartCreator(Node nElement) {

		// gets the type of the chart

		String type = nElement.getAttributes().getNamedItem(XmlConstants.XML_ATT_TYPE).getNodeValue();

		log.debug("Create chart of type " + type);

		IChartCreator chart = ChartFactory.newInstance(type);

		return chart;
	}

	/*
	 * Getters/Setters
	 */

	/**
	 * @return the chart
	 */
	public IChart getChart() {
		return chart;
	}
}
