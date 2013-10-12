package com.redorb.mcharts.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.redorb.mcharts.utils.TimePeriodManipulation;

public class TimePeriodManipulationTest {
	
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	@Test
	public void testWeek() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.MAY, 29);
		
		Date origDate = cal.getTime();
		
		cal.set(2010, Calendar.MAY, 24);
		Date goodDate = cal.getTime();
		
		Date result = TimePeriodManipulation.getWeek(origDate);
		
		boolean test =  
			result.getTime() == goodDate.getTime();
		
		if (! test) {
			System.out.println(
					"WEEK: " + df.format(result) + " <> " + df.format(goodDate));
		}
		
		Assert.assertTrue(test);		
	}
	
	@Test
	public void testMonth() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.MAY, 29);
		
		Date origDate = cal.getTime();
		
		cal.set(2010, Calendar.MAY, 1);
		Date goodDate = cal.getTime();
		
		Date result = TimePeriodManipulation.getMonth(origDate);
		
		boolean test =  
			result.getTime() == goodDate.getTime();
		
		if (! test) {
			System.out.println(
					"MONTH: " + df.format(result) + " <> " + df.format(goodDate));
		}
		
		Assert.assertTrue(test);		
	}
	
	@Test
	public void testTrimester1() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.MAY, 29);
		
		Date origDate = cal.getTime();
		
		cal.set(2010, Calendar.APRIL, 1);
		Date goodDate = cal.getTime();
		
		Date result = TimePeriodManipulation.getTrimester(origDate);
		
		boolean test =  
			result.getTime() == goodDate.getTime();
		
		if (! test) {
			System.out.println(
					"TRIM1: " + df.format(result) + " <> " + df.format(goodDate));
		}
		
		Assert.assertTrue(test);
	}
	
	@Test
	public void testTrimester2() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.SEPTEMBER, 29);
		
		Date origDate = cal.getTime();
		
		cal.set(2010, Calendar.JULY, 1);
		Date goodDate = cal.getTime();
		
		Date result = TimePeriodManipulation.getTrimester(origDate);
		
		boolean test =  
			result.getTime() == goodDate.getTime();
		
		if (! test) {
			System.out.println(
					"TRIM2: " + df.format(result) + " <> " + df.format(goodDate));
		}
		
		Assert.assertTrue(test);
	}
	
	@Test
	public void testSemester() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.SEPTEMBER, 29);
		
		Date origDate = cal.getTime();
		
		cal.set(2010, Calendar.JULY, 1);
		Date goodDate = cal.getTime();
		
		Date result = TimePeriodManipulation.getSemester(origDate);
		
		boolean test =  
			result.getTime() == goodDate.getTime();
		
		if (! test) {
			System.out.println(
					"SEM:" + df.format(result) + " <> " + df.format(goodDate));
		}
		
		Assert.assertTrue(test);
	}
	
	@Test
	public void testYear() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.SEPTEMBER, 29);
		
		Date origDate = cal.getTime();
		
		cal.set(2010, Calendar.JANUARY, 1);
		Date goodDate = cal.getTime();
		
		Date result = TimePeriodManipulation.getYear(origDate);
		
		boolean test =  
			result.getTime() == goodDate.getTime();
		
		if (! test) {
			System.out.println(
					"YEAR:" + df.format(result) + " <> " + df.format(goodDate));
		}
		
		Assert.assertTrue(test);
	}
}
