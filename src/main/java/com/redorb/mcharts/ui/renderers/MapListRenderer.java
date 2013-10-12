package com.redorb.mcharts.ui.renderers;

import java.awt.Component;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")
public class MapListRenderer<T> extends JLabel implements ListCellRenderer<T> {

	/*
	 * Attributes
	 */
	
	private Map<T, String> mapText = null;

	private Map<T, Icon> mapIcons = null;
	
	/*
	 * Ctors
	 */
	
	public MapListRenderer(Map<T, String> map) {
		this.mapText = map;
		setOpaque(true);
	}
	
	public MapListRenderer(Map<T, String> mapText, Map<T, Icon> mapIcons) {
		this.mapText = mapText;
		this.mapIcons = mapIcons;
		setOpaque(true);
	}
	
	/*
	 * Operations
	 */

	@Override
	public Component getListCellRendererComponent(JList<? extends T> list, T value,
			int index, boolean isSelected, boolean cellHasFocus) {

		String text = mapText.get(value);
		if (text == null) {
			text = "not found";
		}
		
		if (mapIcons != null) {
			Icon icon = mapIcons.get(value);
			if (icon != null) {
				setIcon(icon);
			}
		}

		setText(text);
				
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

		return this;
	}
}
