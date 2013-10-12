package com.redorb.mcharts.ui.renderers;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer to display a table with an alternate color every even row.
 */
@SuppressWarnings("serial")
public class AlternateRowsTableRenderer extends JLabel implements
		TableCellRenderer {
	/*
	 * Attributes
	 */
	
	private final Color lightGray = new Color(210,210,210);
	
	private final Color lightBlue = new Color(184,207,209);
	
	private final Color incomeColor = Color.BLUE;
	
	private final Color outcomeColor = Color.RED;
	
	/*
	 * Ctors
	 */
	
	public AlternateRowsTableRenderer() {
		setOpaque(true);
	}

	/*
	 * Operations
	 */

	@Override
	public Component getTableCellRendererComponent(
			JTable table, Object value,
            boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
				
		// foreground
		
		if (value instanceof BigDecimal) {
			BigDecimal b = (BigDecimal) value;
			if (b.signum() < 0) {
				setForeground(outcomeColor);
			}
			else {
				setForeground(incomeColor);
			}
			
			setText(value.toString() + " €");
		}
		else {
			setText(value.toString());
		}
		
		// background

		if (rowIndex % 2 == 0) {
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
