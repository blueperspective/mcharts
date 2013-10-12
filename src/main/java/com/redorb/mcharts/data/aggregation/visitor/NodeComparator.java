package com.redorb.mcharts.data.aggregation.visitor;

import java.util.Comparator;

import com.redorb.mcharts.data.aggregation.structure.INode;
import com.redorb.mcharts.utils.MutableFloat;

public class NodeComparator implements Comparator<INode<MutableFloat>> {

	/*
	 * Operations
	 */

	@Override
	public int compare(INode<MutableFloat> o1, INode<MutableFloat> o2) {
		
		return o1.getValue().compareTo(o2.getValue());
	}
}
