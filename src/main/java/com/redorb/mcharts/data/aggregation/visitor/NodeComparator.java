package com.redorb.mcharts.data.aggregation.visitor;

import java.math.BigDecimal;
import java.util.Comparator;

import com.redorb.mcharts.data.aggregation.structure.INode;

public class NodeComparator implements Comparator<INode<BigDecimal>> {

	/*
	 * Operations
	 */

	@Override
	public int compare(INode<BigDecimal> o1, INode<BigDecimal> o2) {
		
		return o1.getValue().compareTo(o2.getValue());
	}
}
