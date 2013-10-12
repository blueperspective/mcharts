package com.redorb.mcharts.ui.charts;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimePeriodValues;
import org.jfree.data.time.TimePeriodValuesCollection;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.aggregation.structure.INode;
import com.redorb.mcharts.utils.MutableFloat;
import com.redorb.mcharts.utils.TimePeriodType;

public class DatasetComputer {

	/*
	 * Attributes
	 */
	
	private static final Logger log = LoggerFactory.getLogger(DatasetComputer.class);

	/*
	 * Ctors
	 */

	/*
	 * Operations
	 */
	
	public static void computeDataset(AccountingTree tree, DefaultPieDataset dataset) {

		// fills dataset

		INode<MutableFloat> rootNode = tree.getRoot();
		int count1 = rootNode.getChildren().size();

		for (int d1 = 0; d1 < count1; d1++) {

			INode<MutableFloat> n1 = rootNode.getChild(d1);

			IAccountingObject accountingObject = (IAccountingObject) n1.getContent();
			BigDecimal amount = n1.getValue().get().abs();
			
			dataset.setValue(accountingObject.getName(), amount);
		}
	}
	
	public static void computeDataset(AccountingTree tree, DefaultCategoryDataset dataset) {

		// fills dataset

		INode<MutableFloat> rootNode = tree.getRoot();

		int count1 = rootNode.getChildren().size();

		for (int d1 = 0; d1 < count1; d1++) {

			INode<MutableFloat> n1 = rootNode.getChild(d1);

			IAccountingObject accountingObject = (IAccountingObject) n1.getContent();
			BigDecimal amount = n1.getValue().get().abs();

			dataset.addValue(
					amount, 
					I18n.getMessage("chart.legend.amount"), 
					accountingObject.getName());
		}
	}
	
	public static void computeDatasetSeparate(AccountingTree tree, DefaultCategoryDataset dataset) {

		// fills dataset

		INode<MutableFloat> rootNode = tree.getRoot();

		int count1 = rootNode.getChildren().size();

		for (int d1 = 0; d1 < count1; d1++) {

			INode<MutableFloat> n1 = rootNode.getChild(d1);

			IAccountingObject accountingObject = (IAccountingObject) n1.getContent();
			BigDecimal amount = n1.getValue().get().abs();

			dataset.addValue(
					amount, 
					accountingObject.getName(), 
					accountingObject.getName());
		}
	}
	
	public static void compute2DDataset(AccountingTree tree, DefaultCategoryDataset dataset) {

		// date format

		DateFormat dateFormat = null;

		Object dim1 = tree.getDimensions().get(0);

		if (! (dim1 instanceof TimePeriodType)) {
			log.error("1st dimension should be of type TimePeriodType");
			return;
		}

		TimePeriodType timePeriodType = (TimePeriodType) dim1;

		switch (timePeriodType) {			
		case WEEK:
			dateFormat = new SimpleDateFormat("w yy");
			break;
		case MONTH:
			dateFormat = new SimpleDateFormat("MMM yy");
			break;
		case TRIMESTER:
			dateFormat = new SimpleDateFormat("MMM yy");
			break;
		case SEMESTER:
			dateFormat = new SimpleDateFormat("MMM yy");
			break;
		case YEAR:
			dateFormat = new SimpleDateFormat("yyyy");
			break;
		default:
			break;
		}

		// graph series

		INode<MutableFloat> rootNode = tree.getRoot();
		int count1 = rootNode.getChildren().size();

		for (int d1 = 0; d1 < count1; d1++) {

			INode<MutableFloat> n1 = rootNode.getChild(d1);
			Date period = (Date) n1.getContent();

			int count2 = n1.getChildrenCount();

			for (int d2 = 0; d2 < count2; d2++) {

				INode<MutableFloat> n2 = n1.getChild(d2);

				IAccountingObject accountingObject = (IAccountingObject) n2.getContent();				
				BigDecimal amount = n2.getValue().get().abs();

				dataset.addValue(
						amount, 
						accountingObject.getName(), 
						dateFormat.format(period));
			}
		}
	}
	
	public static void compute2DDataset(AccountingTree tree, TimePeriodValuesCollection dataset) {

		if (tree.getDimensions().size() != 2) {
			throw new RuntimeException("Missing dimensions");
		}
		
		// credit series
		Map<IAccountingObject, TimePeriodValues> timeSeries =
				new HashMap<IAccountingObject, TimePeriodValues>();

		// fills the series with the aggregator

		Dimension dim1 = tree.getDimensions().get(0);

		if (Dimension.WEEK != dim1
				&& Dimension.MONTH != dim1
				&& Dimension.TRIMESTER != dim1
				&& Dimension.SEMESTER != dim1
				&& Dimension.YEAR != dim1) {
			throw new RuntimeException("first dimension should be of type TimePeriodType");
		}

		// browse the tree

		INode<MutableFloat> rootNode = tree.getRoot();

		int count1 = rootNode.getChildren().size();

		for (int d1 = 0; d1 < count1; d1++) {

			INode<MutableFloat> n1 = rootNode.getChild(d1);

			Date period = (Date) n1.getContent();

			// chart timePeriod
			TimePeriod timePeriod = null;

			switch (dim1) {
			case WEEK:
				timePeriod = new Week(period);
				break;
			case MONTH:
				timePeriod = new Month(period);
				break;
			case TRIMESTER:
				timePeriod = new Month(period);
				break;
			case SEMESTER:
				timePeriod = new Month(period);
				break;
			case YEAR:
				timePeriod = new Year(period);
				break;
			default:
				break;
			}

			int count2 = n1.getChildren().size();

			for (int d2 = 0; d2 < count2; d2++) {

				INode<MutableFloat> n2 = n1.getChild(d2);

				IAccountingObject accountingObject = (IAccountingObject) n2.getContent();
				BigDecimal amount = n2.getValue().get().abs();

				// current time serie
				TimePeriodValues currentSerie = null;

				currentSerie = timeSeries.get(accountingObject);

				if (currentSerie == null) {
					currentSerie = new TimePeriodValues(accountingObject.getName());
					timeSeries.put(accountingObject, currentSerie);
				}

				currentSerie.add(timePeriod, amount);
				
				log.debug("accountingObject " + accountingObject + " " + timePeriod);
			}
		}

		// adds the series to the datasets

		for (TimePeriodValues serie : timeSeries.values()) {
			dataset.addSeries(serie);
		}
	}
}
