package com.redorb.mcharts.ui.renderers;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.redorb.mcharts.data.aggregation.structure.AccountingLeaf;
import com.redorb.mcharts.data.aggregation.structure.AccountingNode;

@SuppressWarnings("serial")
public class AccountingTreeRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		if (value instanceof AccountingNode) {
			AccountingNode o = (AccountingNode) value;
			setText(o.getContent().toString());
		}
		else if (value instanceof AccountingLeaf) {
			AccountingLeaf o = (AccountingLeaf) value;
			setText(o.getContent().toString() + ": " + o.getValue().toString());
		}

		return this;
	}
}
