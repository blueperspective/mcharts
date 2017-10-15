package com.redorb.mcharts.data.aggregation.visitor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.redorb.mcharts.data.aggregation.structure.INode;

public class LeafVisitor implements IVisitor<BigDecimal> {

	/*
	 * Attributes
	 */

	private List<INode<BigDecimal>> nodes = new ArrayList<INode<BigDecimal>>();

	/*
	 * Ctors
	 */

	/*
	 * Operations
	 */

	@Override
	public void visit(INode<BigDecimal> node) {

		if (node.isLeaf()) {
			nodes.add(node);
		}
	}

	/**
	 * @return the nodes
	 */
	public List<INode<BigDecimal>> getNodes() {
		return nodes;
	}
}
