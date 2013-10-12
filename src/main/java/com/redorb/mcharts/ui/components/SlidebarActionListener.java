package com.redorb.mcharts.ui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SlidebarActionListener<T> extends MouseAdapter {

	private SlidebarPanel<T> panel;
	private int inc;
	
	public SlidebarActionListener(SlidebarPanel<T> panel, int inc) {
		this.panel = panel;
		this.inc = inc;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		panel.move(inc);
	}
}
