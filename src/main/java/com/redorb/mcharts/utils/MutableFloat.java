package com.redorb.mcharts.utils;

import java.math.BigDecimal;

public class MutableFloat implements Comparable<MutableFloat> {

	private BigDecimal value = new BigDecimal(.0f);
	
	public MutableFloat() {}

	public MutableFloat(final float f) {
		value = new BigDecimal(f);
	}
	
	public MutableFloat(final BigDecimal f) {
		value = f;
	}

	public void add(final BigDecimal f) {
		value = value.add(f);
	}

	public void add(final MutableFloat f) {
		add(f.value);
	}

	public void substract(final BigDecimal f) {
		value = value.subtract(f);
	}

	public void substract(final MutableFloat f) {
		substract(f.value);
	}

	public void multiply(final BigDecimal f) {
		value = value.multiply(f);
	}

	public void mutliply(final MutableFloat f) {
		multiply(f.value);
	}

	public void divide(final BigDecimal f) {
		value = value.divide(f);
	}

	public void divide(final MutableFloat f) {
		divide(f.value);
	}

	public BigDecimal get() {
		return value;
	}

	public void set(final BigDecimal f) {
		value = f;
	}

	public String toString() {
		return value.toString();
	}

	@Override
	public int compareTo(final MutableFloat o) {
		return value.compareTo(o.value);
	}
	
	@Override
	public boolean equals(Object o) {
		
		boolean res = false;
		
		if (o != null && o instanceof MutableFloat) {
			
			MutableFloat mfloat = (MutableFloat) o;
			
			res = (value.equals(mfloat.value));
		}
		
		return res;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
