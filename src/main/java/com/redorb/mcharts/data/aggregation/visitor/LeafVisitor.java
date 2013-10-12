package com.redorb.mcharts.data.aggregation.visitor;

import java.util.ArrayList;
import java.util.List;

import com.redorb.mcharts.data.aggregation.structure.INode;
import com.redorb.mcharts.utils.MutableFloat;

public class LeafVisitor implements IVisitor<MutableFloat> {

	/*
	 * Attributes
	 */

	private List<INode<MutableFloat>> nodes = new ArrayList<INode<MutableFloat>>();

	/*
	 * Ctors
	 */

	/*
	 * Operations
	 */

	@Override
	public void visit(INode<MutableFloat> node) {

		if (node.isLeaf()) {
			nodes.add(node);
		}
	}

	/**
	 * @return the nodes
	 */
	public List<INode<MutableFloat>> getNodes() {
		return nodes;
	}
}
