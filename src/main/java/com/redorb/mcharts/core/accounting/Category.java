package com.redorb.mcharts.core.accounting;

import java.util.ArrayList;
import java.util.List;

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
	private List<Category> subCategories = null;

	private Kind kind = null;

	/*
	 * Ctor
	 */

	/**
	 * Builds a category with a given number and name
	 */
	public Category(String name) {
		super(name);
	}

	public Category(String name, Kind kind) {
		super(name);
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
	public void addSubCategory(Category category) {

		if (subCategories == null) {
			subCategories = new ArrayList<>();
		}

		category.kind = kind;

		subCategories.add(category);
	}

	/*
	 * Getters/Setters
	 */

	/**
	 * @return the sub categories
	 */
	public List<Category> getSubCategories() {
		return subCategories;
	}

	/**
	 * @return the kind
	 */
	public Kind getKind() {
		return kind;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null) { return false; }

		if (! (o instanceof Category)) { return false; }

		Category c = (Category) o;

		boolean res = name.equals(c.name);

		if (res && subCategories != null) {

			res = subCategories.size() == c.subCategories.size();

			for (int i = 0; i < subCategories.size() && !res; i++) {
				res &= subCategories.get(i).equals(c.subCategories.get(i));
			}
		}

		return res;
	}

	@Override
	public int hashCode() {

		int hashCode = name.hashCode();

		if (subCategories != null) {
			for (int i = 0; i < subCategories.size(); i++) {
				hashCode += subCategories.get(i).hashCode();
			}
		}

		return hashCode;
	}
}
