package com.redorb.mcharts.ui.components;

import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.redorb.commons.ui.GBC;

@SuppressWarnings("serial")
public class SidebarPanel extends JPanel {

	private GridBagLayout layout;
	private JSeparator vertSeparator;

	private int buttonsCount;

	public SidebarPanel() {
		initComponents();
		initLayout();
	}

	private void initComponents() {

		vertSeparator = new JSeparator(JSeparator.VERTICAL);
	}

	private void initLayout() {

		layout = new GridBagLayout();
		setLayout(layout);

		add(vertSeparator, new GBC(1, 0, GBC.VERTICAL).setGridHeight(GBC.REMAINDER));
	}

	public void addButton(JComponent button) {

		add(button, new GBC(0, buttonsCount).setInsets(20));
		buttonsCount++;
	}

	public void endLayout() {
		add(Box.createVerticalGlue(), new GBC(0, buttonsCount, GBC.VERTICAL));
	}
}
