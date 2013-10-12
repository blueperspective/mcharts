package com.redorb.mcharts.ui.models;

import java.util.List;

import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class StringListModel extends AbstractListModel<String> {

	/*
	 * Attributes
	 */
	
	private List<String> strings = null;
	
	/*
	 * Ctors
	 */
	
	public StringListModel(List<String> strings) {
		this.strings = strings;
	}
	
	/*
	 * Getters/Setters
	 */
	
	@Override
	public String getElementAt(int index) {
		return strings.get(index);
	}

	@Override
	public int getSize() {
		return strings.size();
	}
}
