package com.redorb.mcharts.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private final static Logger log = LoggerFactory.getLogger(Utils.class);
	
	/*
	 * Operations
	 */

	public static void extractFromJar(String jarpath, String outFile) {

		try {
			
			BufferedInputStream in = new BufferedInputStream(
					Utils.class.getResourceAsStream(jarpath));

			OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));

			byte[] buffer = new byte[2048];
			for (;;)  {
				int nBytes = in.read(buffer);
				if (nBytes <= 0) break;
				out.write(buffer, 0, nBytes);
			}
			
			out.flush();
			out.close();
			in.close();
			
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * From an array of objects, returns the corresponding array of classes
	 */
	public static Class<?>[] toClassArray(Object[] objects) {

		Class<?>[] classes = new Class[objects.length];

		for (int i = 0; i < objects.length; i++) {
			classes[i] = objects[i].getClass();
		}

		return classes;
	}

	/**
	 * Gets the date of the first day in month of a date N month in future or
	 * past of a given date
	 * @param date the starting date
	 * @param amount the number of month in future or past
	 * @return the date (1st day of month)
	 */
	public static Date getNMonthDate(Date date, int amount) {

		Date res = null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, amount);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		res = calendar.getTime();

		return res;
	}
}
