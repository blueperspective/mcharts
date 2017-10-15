package com.redorb.mcharts.data.restriction;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public class ValuedObjectComparator implements Comparator<ValuedObject>, Serializable {

	/*
	 * Attribute
	 */
		
	private boolean ascending = true;
	
	/*
	 * Ctor
	 */
	
	public ValuedObjectComparator() {
		this(true);
	}
	
	public ValuedObjectComparator(boolean ascending) {
		this.ascending = ascending;
	}
	
	/*
	 * Operations
	 */
	
	@Override
	public int compare(ValuedObject v1, ValuedObject v2) {
		
		int result = 0;
		
		result = v1.compareTo(v2);
		
		if (! ascending) {
			result = - result;
		}
		
		return result;
	}
}
