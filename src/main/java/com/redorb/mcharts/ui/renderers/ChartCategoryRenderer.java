package com.redorb.mcharts.ui.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.charts.ChartFileInfo;
import com.redorb.mcharts.core.charts.ChartsCategory;

/**
 * Renderer showing a tool tip and and icon for the charts categories.
 */
@SuppressWarnings("serial")
public class ChartCategoryRenderer extends JLabel implements TreeCellRenderer {

	/*
	 * Static attributes
	 */
	
	private final static Icon chartIcon = Utils.getIcon("/images/22x22/chart.png", "chart");
	private final static Icon chartCategoryIcon = Utils.getIcon("/images/22x22/chartsCategory.png", "chartsCategory");
	private final static Icon userCategoryIcon = Utils.getIcon("/images/22x22/chartsCategory.png", "userCategory");

	/*
	 * Attributes
	 */
	
	private boolean user;
	
	/*
	 * Ctors
	 */
	
	public ChartCategoryRenderer(boolean user) {
		this.user = user;
		setOpaque(true);
	}

	@Override
	public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
		
		if (value instanceof DefaultMutableTreeNode) {
			value = ((DefaultMutableTreeNode) value).getUserObject();
		}
		
		setEnabled(tree.isEnabled());
		
		setBackground(sel ? Color.blue : tree.getBackground());
		
		setText(value.toString());

		if (value instanceof ChartsCategory) {
			setIcon(user ? chartCategoryIcon : userCategoryIcon);
		}
		else if (value instanceof ChartFileInfo) {
			setIcon(chartIcon);
		}
		
		return this;
	}
}
