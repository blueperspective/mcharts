package com.redorb.mcharts.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ListComboModel<E> implements ComboBoxModel<E> {

	/*
	 * Attributes
	 */
	
	private List<E> list;

	private E selected = null;

	private List<ListDataListener> listeners = new ArrayList<ListDataListener>();

	/*
	 * Ctor
	 */
	
	public ListComboModel(List<E> list) {
		this.list = list;
	}
	
	/*
	 * Getters/Setters
	 */

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public E getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selected = (E) anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}
}
