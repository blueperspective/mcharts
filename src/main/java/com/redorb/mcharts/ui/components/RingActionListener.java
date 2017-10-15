package com.redorb.mcharts.ui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RingActionListener<T> extends MouseAdapter {

	private RingSelectionPanel<T> panel;
	private int inc;
	
	public RingActionListener(RingSelectionPanel<T> panel, int inc) {
		this.panel = panel;
		this.inc = inc;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		panel.move(inc);
	}
}
