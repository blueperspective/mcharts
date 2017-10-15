package com.redorb.mcharts.ui.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Renderer to displays list with an alternate color every even row.
 */
@SuppressWarnings("serial")
public class AlternateRowsListRenderer<E> extends JLabel implements ListCellRenderer<E> {

	/*
	 * Attributes
	 */
	
	private final Color lightGray = new Color(210,210,210);
	
	private final Color lightBlue = new Color(184,207,209);

	/*
	 * Ctor
	 */

	public AlternateRowsListRenderer() {
		setOpaque(true);
	}

	/*
	 * Operations
	 */

	@Override
	public Component getListCellRendererComponent(
			JList<? extends E> list,
			E value, // value to display
			int index,    // cell index
			boolean isSelected,  // is selected
			boolean hasFocus)  // cell has focus?
	{
		setText(value.toString());

		if (index % 2 == 0) {
			setBackground(Color.white);
		}
		else {
			setBackground(lightGray);
		}

		if (isSelected) {
			setBackground(lightBlue);
		}

		return this;
	}
}	
