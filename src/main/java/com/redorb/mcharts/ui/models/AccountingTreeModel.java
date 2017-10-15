package com.redorb.mcharts.ui.models;

import java.math.BigDecimal;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.redorb.mcharts.data.aggregation.structure.AccountingNode;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.aggregation.structure.INode;

/**
 * Tree model for showing an accounting tree.
 */
public class AccountingTreeModel implements TreeModel {

	/*
	 * Attribute
	 */
	
	/** Accounting tree to display. */
	private AccountingTree tree = null;

	/*
	 * Ctor
	 */
	
	public AccountingTreeModel(final AccountingTree tree) {
		this.tree = tree;
	}
	
	/*
	 * Operations
	 */

	@Override
	public void addTreeModelListener(TreeModelListener l) {}

	@Override
	public Object getChild(Object parent, int index) {

		Object res = null;

		if (parent instanceof AccountingNode) {
			AccountingNode parentNode = (AccountingNode) parent;
			res = parentNode.getChild(index);
		}

		return res;
	}

	@Override
	public int getChildCount(Object parent) {

		int res = 0;

		if (parent instanceof AccountingNode) {
			AccountingNode parentNode = (AccountingNode) parent;
			res = parentNode.getChildrenCount();
		}

		return res;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {

		int res = -1;

		if (parent instanceof AccountingNode) {

			AccountingNode parentNode = (AccountingNode) parent;

			for (int i = 0; i < parentNode.getChildren().size() && res == -1; i++) {

				INode<BigDecimal> childNode = parentNode.getChild(i);

				if (childNode.equals(child)) {
					res = i;
				}
			}
		}

		return res;
	}

	@Override
	public Object getRoot() {

		return tree.getRoot();
	}

	@Override
	public boolean isLeaf(Object node) {
		
		boolean res = true;
		
		if (node instanceof AccountingNode) {
			res = ((AccountingNode) node).isLeaf(); 
		}
		
		return res;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {}
}
