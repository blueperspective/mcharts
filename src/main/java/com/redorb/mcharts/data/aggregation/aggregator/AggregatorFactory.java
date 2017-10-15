package com.redorb.mcharts.data.aggregation.aggregator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.data.aggregation.Dimension;

/**
 * Creates an aggregator instance.
 */
public final class AggregatorFactory {

	/*
	 * Attributes
	 */
	
	private static final Logger log = LoggerFactory.getLogger(AggregatorFactory.class);
	
	private static final String PACKAGE_NAME = "com.redorb.mcharts.data.aggregation.aggregator.";

	/*
	 * Ctors
	 */
	
	private AggregatorFactory() {}

	/*
	 * Operations
	 */
	
	public static IAggregator newInstance(String type, String[] strDimensions) {
		
		IAggregator aggregator = null;
		
		try {

			// gets the restriction class

			List<Object> dimensions = new ArrayList<Object>();

			// converts each parameter

			for (int p = 0; p < strDimensions.length; p++) {

				String value = strDimensions[p];

				// treats the parameter (converts)
					
				Object dimension = null;
				
				if (value.equals(Account.class.getSimpleName())) {
					dimension = Account.class;
				}
				else if (value.equals(Payee.class.getSimpleName())) {
					dimension = Payee.class;
				}
				else if (value.equals(Category.class.getSimpleName())) {
					dimension = Category.class;
				}
				else if (value.equalsIgnoreCase(Dimension.WEEK.name())) {
					dimension = Dimension.WEEK;
				}
				else if (value.equalsIgnoreCase(Dimension.MONTH.name())) {
					dimension = Dimension.MONTH;
				}
				else if (value.equalsIgnoreCase(Dimension.TRIMESTER.name())) {
					dimension = Dimension.TRIMESTER;
				}
				else if (value.equalsIgnoreCase(Dimension.SEMESTER.name())) {
					dimension = Dimension.SEMESTER;
				}
				else if (value.equalsIgnoreCase(Dimension.YEAR.name())) {
					dimension = Dimension.YEAR;
				}
				else {
					log.error("Unknown dimension: " + value);
				}
				
				if (dimension != null) {
					dimensions.add(dimension);
				}
			}
			
			Class<? extends IAggregator> aggClass = Class.forName(PACKAGE_NAME + type).asSubclass(IAggregator.class);
			Constructor<? extends IAggregator> cons = aggClass.getConstructor(List.class);
			
			aggregator = cons.newInstance(dimensions);
			
			log.debug("Aggregation created : " + aggregator.getClass().getName());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return aggregator;
	}
}
