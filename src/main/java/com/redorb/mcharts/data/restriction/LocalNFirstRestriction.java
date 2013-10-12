package com.redorb.mcharts.data.restriction;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.IAccountingObject;

import com.redorb.mcharts.data.aggregation.structure.AccountingLeaf;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.data.aggregation.structure.INode;
import com.redorb.mcharts.data.aggregation.visitor.LastDimensionVisitor;
import com.redorb.mcharts.data.aggregation.visitor.NodeComparator;
import com.redorb.mcharts.utils.MutableFloat;

/**
 * This restriction restricts to the n first values of the last dimension, but
 * globally (first dimension wide).
 * That means that we will have N and only N values of the last dimension.
 */
public class LocalNFirstRestriction implements IRestriction {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(LocalNFirstRestriction.class);

	private int n = 0;
	
	private Map<INode<MutableFloat>, List<AccountingLeaf>> mapDimensions = null;

	/*
	 * Constructors
	 */

	public LocalNFirstRestriction(Integer n) {
		this.n = n;
	}

	/*
	 * Operation
	 */
	
	@Override
	public String getValue() {
		return Integer.toString(n);
	}

	@Override
	public AccountingTree computeRestriction(AccountingTree tree) {

		AccountingTree restrictedTree = new AccountingTree(tree.getDimensions());

		// gather bottom node values with a visitor

		LastDimensionVisitor visitor = new LastDimensionVisitor();		
		tree.getRoot().accept(visitor);

		mapDimensions = visitor.getMapNodeLeaves();
		
		for (List<AccountingLeaf> list : mapDimensions.values()) {

			Collections.sort(list, new NodeComparator());

			int size = list.size();
			for (int i = (n + 1); i < size; i++) {
				list.remove(n + 1);
			}
		}

		for (Entry<INode<MutableFloat>, List<AccountingLeaf>> e : mapDimensions.entrySet()) {

			INode<MutableFloat> node = e.getKey();
			List<AccountingLeaf> list = e.getValue();
			
			recursiveDelete(node, list);
		}

		return restrictedTree;
	}

	/**
	 * Recursively removes the node which have not been selected in the
	 * restriction
	 * @param node the current node
	 */
	private void recursiveDelete(INode<MutableFloat> node, List<AccountingLeaf> list) {

		Iterator<INode<MutableFloat>> it = node.getChildren().iterator();

		AccountingLeaf remaining = null;

		while (it.hasNext()) {

			INode<MutableFloat> child = it.next();

			if (child.isLeaf()) {

				if (! list.contains(child)) {

					if (remaining == null) {
						remaining = new AccountingLeaf(
								Core.getInstance().getRemaining((Class<? extends IAccountingObject>) child.getContent().getClass()));
					}

					remaining.add(child.getValue().get());

					it.remove();
				}
				else {
					log.debug("child: " + child);
				}
			}
			else {
				recursiveDelete(child, list);
			}
		}

		if (remaining != null) {
			node.addChild(remaining);
			log.debug("added child " + remaining + " to node " + node);
		}
	}
}
