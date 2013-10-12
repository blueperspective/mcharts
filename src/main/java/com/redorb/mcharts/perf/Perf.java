package com.redorb.mcharts.perf;

import java.util.ArrayList;
import java.util.List;

public class Perf {

	/*
	 * Attributes
	 */
	
	private List<Long> times = new ArrayList<Long>(50);
	private List<String> measurePoints = new ArrayList<String>(50);

	/*
	 * Ctors
	 */
	
	private static Perf instance = null;
	
	public synchronized static Perf getInstance() {
		
		if (instance == null) {
			instance = new Perf();
			instance.takeMeasure("start");
		}
		
		return instance;
	}

	/*
	 * Operations
	 */
	
	public void takeMeasure(String name) {

		times.add(System.nanoTime());
		measurePoints.add(name);
	}
	
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 1; i < times.size() && i < measurePoints.size(); i++) {
			
			builder.append(measurePoints.get(i - 1));
			builder.append(" - ");
			builder.append(measurePoints.get(i));
			builder.append(": ");
			builder.append((times.get(i) - times.get(i - 1)) / 1000000);
			builder.append(" ms\n");
		}
		
		return builder.toString();
	}
}
