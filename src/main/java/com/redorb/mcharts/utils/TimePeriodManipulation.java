package com.redorb.mcharts.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Get dates of week start, month start, etc. from a given date.
 */
public class TimePeriodManipulation {
	
	/*
	 * Attributes
	 */

	private static final Calendar calendar = Calendar.getInstance();
		
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
}
