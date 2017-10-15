package com.redorb.mcharts.data.aggregation.aggregator;

import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.utils.TimePeriodManipulation;

/**
 * Used to get a specific dimension of a transaction
 */
public final class DimensionGetter {

	private DimensionGetter() {}

	/*
	 * Operations
	 */

	public static Object getDimension(final Dimension dimension, final Transaction transaction) {

		Object dimensionValue = null;

		switch (dimension) {

		case ACCOUNT:
			dimensionValue = transaction.getAccount();				
			break;
		case CATEGORY:
			dimensionValue = transaction.getCategory();
			break;
		case SUB_CATEGORY:
			dimensionValue = transaction.getSubCategory();
			break;
		case PAYEE:
			dimensionValue = transaction.getPayee();
			break;
		case WEEK:
			dimensionValue = TimePeriodManipulation.getWeek(transaction.getDate());
			break;
		case MONTH:
			dimensionValue = TimePeriodManipulation.getMonth(transaction.getDate());
			break;
		case TRIMESTER:
			dimensionValue = TimePeriodManipulation.getTrimester(transaction.getDate());
			break;
		case SEMESTER:
			dimensionValue = TimePeriodManipulation.getSemester(transaction.getDate());
			break;
		case YEAR:
			dimensionValue = TimePeriodManipulation.getYear(transaction.getDate());
			break;
		default:
			break;
		}

		return dimensionValue;
	}
}
