package com.redorb.mcharts.io.chart;

import java.io.File;

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

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.core.charts.IChart;
import com.redorb.mcharts.data.aggregation.aggregator.IAggregator;
import com.redorb.mcharts.data.criteria.filter.CriteriaFactory;
import com.redorb.mcharts.data.criteria.structure.ICriteria;
import com.redorb.mcharts.data.criteria.structure.ICriteriaDecorator;
import com.redorb.mcharts.data.criteria.structure.InSetCriteria;
import com.redorb.mcharts.data.criteria.structure.InSetNameCriteria;
import com.redorb.mcharts.data.criteria.structure.NameCriteria;
import com.redorb.mcharts.data.criteria.structure.PeriodCriteria;
import com.redorb.mcharts.data.restriction.IRestriction;
import com.redorb.mcharts.io.IWriter;
import com.redorb.mcharts.io.grisbi.XmlConstants;
import com.redorb.mcharts.ui.charts.IChartCreator;

public class ChartWriter implements IWriter {

	/*
	 * Attributes
	 */

	private File file = null;

	private IChart chart = null;

	private Document doc = null;

	/*
	 * Ctors
	 */

	public ChartWriter(String filepath, IChart chart) {
		this.file = new File(filepath);
		this.chart= chart;
	}

	/*
	 * Operations
	 */

	public void write() throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();

		doc = impl.createDocument(null,null,null);

		Element eltRoot = doc.createElement(XmlConstants.XML_ELT_CHARTS);

		Element eltChartDef = doc.createElement(XmlConstants.XML_ELT_CHART_DEFINITION);
		eltChartDef.setAttribute(XmlConstants.XML_ATT_NAME, chart.getName());

		eltRoot.appendChild(eltChartDef);

		Element eltDescription = doc.createElement(XmlConstants.XML_ELT_DESCRIPTION);
		eltDescription.appendChild(doc.createTextNode(chart.getDescription()));

		eltChartDef.appendChild(eltDescription);

		if (chart.getCriteria() != null) {
			createCriteria(chart.getCriteria(), eltChartDef);
		}

		if (chart.getAggregator() != null) {
			createAggregator(chart.getAggregator(), eltChartDef);
		}

		if (chart.getRestriction() != null) {
			createRestriction(chart.getRestriction(), eltChartDef);
		}

		createChartCreator(chart.getChartCreator(), eltChartDef);

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

	private void createCriteria(ICriteria criteria, Element eltParent) {


		Element eltCriteria = doc.createElement(XmlConstants.XML_ELT_CRITERIA);
		eltCriteria.setAttribute(
				XmlConstants.XML_ATT_TYPE,
				criteria.getClass().getSimpleName());

		if (criteria instanceof ICriteriaDecorator) {

			ICriteriaDecorator decorator = (ICriteriaDecorator) criteria;

			for (int i = 0; i < decorator.getCriterias().size(); i++) {
				createCriteria(decorator.getCriterias().get(i), eltCriteria);
			}
		}
		else if (criteria instanceof NameCriteria) {

			NameCriteria nameCriteria = (NameCriteria) criteria;

			Element eltParam = doc.createElement(XmlConstants.XML_ELT_PARAMETER);

			if (nameCriteria.getName() != null) {
				eltParam.setAttribute(
						CriteriaFactory.PARAM_NAME,
						nameCriteria.getName());
			}
			else if (nameCriteria.getPatternName() != null) {
				eltParam.setAttribute(
						CriteriaFactory.PARAM_REG_EXP, 
						nameCriteria.getPatternName().toString());
			}

			eltCriteria.appendChild(eltParam);
		}
		else if (criteria instanceof PeriodCriteria) {

			PeriodCriteria periodCriteria = (PeriodCriteria) criteria;

			Element eltParam = doc.createElement(XmlConstants.XML_ELT_PARAMETER);
			eltParam.setAttribute(
					CriteriaFactory.PARAM_START_DATE,
					Core.getInstance().getDateFormat().format(periodCriteria.getStartDate()));
			eltParam.setAttribute(
					CriteriaFactory.PARAM_END_DATE,
					Core.getInstance().getDateFormat().format(periodCriteria.getEndDate()));

			eltCriteria.appendChild(eltParam);
		}
		else if (criteria instanceof InSetNameCriteria) {

			InSetNameCriteria inSetNameCriteria = (InSetNameCriteria) criteria;

			for (String name : inSetNameCriteria.getSet()) {
				Element eltParam = doc.createElement(XmlConstants.XML_ELT_PARAMETER);
				eltParam.setAttribute(
						CriteriaFactory.PARAM_SET_OBJECT,
						name);
				eltCriteria.appendChild(eltParam);
			}			
		}
		else if (criteria instanceof InSetCriteria) {

			InSetCriteria inSetCriteria = (InSetCriteria) criteria;

			for (IAccountingObject objet : inSetCriteria.getSet()) {
				Element eltParam = doc.createElement(XmlConstants.XML_ELT_PARAMETER);
				eltParam.setAttribute(
						CriteriaFactory.PARAM_SET_OBJECT,
						objet.getName());
				eltCriteria.appendChild(eltParam);
			}			
		}

		eltParent.appendChild(eltCriteria);
	}

	private void createAggregator(IAggregator aggregator, Element eltParent) {

		Element eltAggregator = doc.createElement(XmlConstants.XML_ELT_AGGREGATOR);
		eltAggregator.setAttribute(
				XmlConstants.XML_ATT_TYPE,
				aggregator.getClass().getSimpleName());

		// write dimensions

		for (int i = 0; i < aggregator.getDimensions().size(); i++) {

			Element eltParam = doc.createElement(XmlConstants.XML_ELT_PARAMETER);

			eltParam.setAttribute(XmlConstants.XML_ATT_NAME, "dimension");
			eltParam.setAttribute(XmlConstants.XML_ATT_VALUE, 
					aggregator.getDimensions().get(i).name());

			eltAggregator.appendChild(eltParam);
		}

		eltParent.appendChild(eltAggregator);
	}

	private void createRestriction(IRestriction restriction, Element eltParent) {

		Element eltRestriction = doc.createElement(XmlConstants.XML_ELT_RESTRICTION);
		eltRestriction.setAttribute(
				XmlConstants.XML_ATT_TYPE,
				restriction.getClass().getSimpleName());

		Element eltParam = doc.createElement(XmlConstants.XML_ELT_PARAMETER);
		eltParam.setAttribute(XmlConstants.XML_ATT_NAME, 
				restriction.getValue());

		eltRestriction.appendChild(eltParam);

		eltParent.appendChild(eltRestriction);
	}

	private void createChartCreator(IChartCreator creator, Element eltParent) {

		Element eltRestriction = doc.createElement(XmlConstants.XML_ELT_CHART_CREATOR);
		eltRestriction.setAttribute(
				XmlConstants.XML_ATT_TYPE,
				creator.getClass().getSimpleName());

		eltParent.appendChild(eltRestriction);
	}
}
