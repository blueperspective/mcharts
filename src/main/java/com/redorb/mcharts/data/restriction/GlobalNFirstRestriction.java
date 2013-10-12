package com.redorb.mcharts.data.restriction;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.IAccountingObject;

import com.redorb.mcharts.data.aggregation.structure.AccountingLeaf;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.aggregation.structure.INode;
import com.redorb.mcharts.data.aggregation.visitor.LeafVisitor;
import com.redorb.mcharts.data.aggregation.visitor.NodeComparator;
import com.redorb.mcharts.utils.MutableFloat;

/**
 * This restriction restricts to the n first values of the last dimension.
 * That means that we may obtain more than N values for the last dimension.
 */
public class GlobalNFirstRestriction implements IRestriction {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(GlobalNFirstRestriction.class);

	private int n = 1;

	private List<INode<MutableFloat>> listObjects = null;

	/*
	 * Ctors
	 */
	
	public GlobalNFirstRestriction(Integer n) {
		this.n = n;
	}

	/*
	 * Operations
	 */
	
	@Override
	public String getValue() {
		return Integer.toString(n);
	}

	@Override
	public AccountingTree computeRestriction(AccountingTree tree) {

		// gather bottom node values with a visitor

		LeafVisitor visitor = new LeafVisitor();
		tree.getRoot().accept(visitor);

		listObjects = visitor.getNodes();
	
		Collections.sort(listObjects, new NodeComparator());

		int size = listObjects.size();

		for (int i = n; i < size; i++) {
			listObjects.remove(n);
		}

		recursiveDelete(tree.getRoot());

		return tree;
	}

	/**
	 * Recursively removes the node which have not been selected in the
	 * restriction
	 * @param node the current node
	 */
	private void recursiveDelete(INode<MutableFloat> node) {

		Iterator<INode<MutableFloat>> it = node.getChildren().iterator();

		AccountingLeaf remaining = null;

		while (it.hasNext()) {

			INode<MutableFloat> child = it.next();

			if (child.isLeaf()) {

				if (! listObjects.contains(child)) {
					
					log.debug("child removed: " + child);

					if (remaining == null) {
						remaining = new AccountingLeaf(
								Core.getInstance().getRemaining((Class<? extends IAccountingObject>) child.getContent().getClass()));
					}

					remaining.add(child.getValue().get());

					it.remove();
				}
				else {
					log.debug("child kept: " + child);
				}
			}
			else {
				recursiveDelete(child);
			}
		}

		if (remaining != null) {
			node.addChild(remaining);
			log.debug("added child " + remaining + " to node " + node);
		}
	}
}
