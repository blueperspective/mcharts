package com.redorb.mcharts.data.restriction;

public class ValuedObject<O, V> implements Comparable<ValuedObject<O, V>> {

	/*
	 * Attributes
	 */

	public O object = null;
	
	private Comparable<V> value = null; 
	
	/*
	 * Ctors
	 */
	
	public ValuedObject(O object, Comparable<V> value) {
		this.object = object;
		this.value = value;
	}

	/*
	 * Operations
	 */
	
	@Override
	public int hashCode() {		
		return object.hashCode() + value.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		
		boolean res = false;
				
		if (o != null && o instanceof ValuedObject<?, ?>) {
			
			ValuedObject<O, V> valuedObject = (ValuedObject<O, V>) o;
			res = valuedObject.getObject().equals(valuedObject.getObject())
				&& valuedObject.getValue().equals(value);
		}
		else {
			res = super.equals(o);
		}
		
		return res;
	}

	@Override
	public int compareTo(ValuedObject<O, V> o) {
				
		return value.compareTo((V) o.getValue());
	}

	/*
	 * Getters/Setters
	 */
	
	/**
	 * @return the object
	 */
	public O getObject() {
		return object;
	}

	/**
	 * @return the value
	 */
	public Comparable<V> getValue() {
		return value;
	}
}
