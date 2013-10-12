package com.redorb.mcharts.core.accounting;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a category (or sub category).
 */
public class Category extends AbstractAccountingObject {

	/*
	 * Attributes
	 */
	
	public enum Kind {
		INCOME,
		OUTCOME
	}
	
	/** sub categories */
	private Map<Long, Category> subCategories = null;
	
	private Kind kind = null;
	
	/*
	 * Ctor
	 */
	
	/**
	 * Builds a category with a given number and name
	 */
	public Category(Long number, String name) {
		super(number, name);
	}
	
	public Category(Long number, String name, Kind kind) {
		super(number, name);
		this.kind = kind;
	}
	
	/*
	 * Operations
	 */
	
	/**
	 * Adds the sub category.
	 * Constructs the subCategories object the first time used, in order
	 * not to have unused HashMap for all the sub categories.
	 * @param number the sub category number
	 * @param category the sub category to add
	 */
	public void addSubCategory(Long number, Category category) {
		
		if (subCategories == null) {
			subCategories = new HashMap<Long, Category>();
		}
		
		category.kind = kind;
		
		subCategories.put(number, category);
	}
	
	/*
	 * Getters/Setters
	 */

	/**
	 * @return the sub categories
	 */
	public Map<Long, Category> getSubCategories() {
		return subCategories;
	}
	
	/**
	 * Return a given sub category
	 * @param number identifier of the category
	 * @return the category or null
	 */
	public Category getSubCategory(Long number) {
		
		Category subCategory = null;
		
		if (subCategories != null) {
			subCategory =  subCategories.get(number);
		}
		
		return subCategory;
	}
	
	/**
	 * @return the kind
	 */
	public Kind getKind() {
		return kind;
	}

	@Override
	public boolean equals(Object o) {
		
		boolean res = false;
		
		if (o != null && o instanceof Category) {
			Category c = (Category) o;
			res = (number.equals(c.number)) && (name.equals(c.name));
		}
		
		return res;
	}
	
	@Override
	public int hashCode() {
		return number.intValue() + name.hashCode();
	}
}
