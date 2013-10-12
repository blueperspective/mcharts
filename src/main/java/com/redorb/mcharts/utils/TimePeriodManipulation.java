package com.redorb.mcharts.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Get dates of week start, month start, etc. from a given date.
 */
public class TimePeriodManipulation {
	
	/*
	 * Attributes
	 */

	private static final Calendar calendar = Calendar.getInstance();
	
	private static final Map<String, TimePeriodType> TIME_PERIOD_TYPES =
		new HashMap<String, TimePeriodType>();
	
	/**
	 * Get the week start date.
	 * @param date
	 * @return week start of the date
	 */
	public static synchronized Date getWeek(Date date) {
				
		calendar.setTime(date);		
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		return calendar.getTime();
	}

	/**
	 * Get the week start date.
	 * @param date
	 * @return week start of the date
	 */
	public static synchronized Date getMonth(Date date) {
		
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		return calendar.getTime();
	}

	/**
	 * Get the trimester start date.
	 * @param date
	 * @return trimester start of the date
	 */
	public static synchronized Date getTrimester(Date date) {
				
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		
		if (month < 4) {
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
		}
		else if (month < 7) {
			calendar.set(Calendar.MONTH, Calendar.APRIL);
		}
		else if (month < 10) {
			calendar.set(Calendar.MONTH, Calendar.JULY);
		}
		else {
			calendar.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		return calendar.getTime();
	}

	/**
	 * Get the semester start date.
	 * @param date
	 * @return semester start of the date
	 */
	public static synchronized Date getSemester(Date date) {
		
		
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH);
		
		if (month < 6) {
			calendar.set(Calendar.MONTH, Calendar.JANUARY);
		}
		else {
			calendar.set(Calendar.MONTH, Calendar.JULY);
		}
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		return calendar.getTime();
	}

	/**
	 * Get the year start date.
	 * @param date
	 * @return year start of the date
	 */
	public static synchronized Date getYear(Date date) {

		calendar.setTime(date);
		
		calendar.set(Calendar.MONTH,  Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		return calendar.getTime();
	}

	/**
	 * Create a TimePeriodType from a string.
	 * @return TimePeriodType
	 */
	public static TimePeriodType fromString(String strTimePeriodType) {

		// if the map timePeriodTypes is empty, fills it (=> done only once)
		
		if (TIME_PERIOD_TYPES.isEmpty()) {

			TIME_PERIOD_TYPES.put("WEEK", TimePeriodType.WEEK);
			TIME_PERIOD_TYPES.put("MONTH", TimePeriodType.MONTH);
			TIME_PERIOD_TYPES.put("TRIMESTER", TimePeriodType.TRIMESTER);
			TIME_PERIOD_TYPES.put("SEMESTER", TimePeriodType.SEMESTER);
			TIME_PERIOD_TYPES.put("YEAR", TimePeriodType.YEAR);
		}

		// returns the TimePeriodType of NO_PERIOD if no correspondence is found
		
		TimePeriodType timePeriodType = TimePeriodType.NO_PERIOD;

		if (TIME_PERIOD_TYPES.containsKey(strTimePeriodType)) {
			timePeriodType = TIME_PERIOD_TYPES.get(strTimePeriodType);
		}

		return timePeriodType;
	}
}
