package com.redorb.mcharts.data.aggregation.visitor;

import com.redorb.mcharts.data.aggregation.structure.INode;

/**
 * Visitor pattern interface for visiting AccountingNodes
 */
public interface IVisitor<T> {
	
	void visit(INode<T> node);
}
