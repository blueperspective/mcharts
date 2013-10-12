package com.redorb.mcharts.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.redorb.mcharts.data.aggregation.structure.AccountingLeaf;
import com.redorb.mcharts.data.aggregation.structure.AccountingNode;
import com.redorb.mcharts.data.aggregation.structure.INode;
import com.redorb.mcharts.data.aggregation.visitor.NodeComparator;
import com.redorb.mcharts.utils.MutableFloat;

public class AccoutingSort {

	/*
	 * Operations
	 */
	
	@Test
	public void test() {
		
		AccountingNode na = new AccountingNode("A");
		
		AccountingLeaf la1 = new AccountingLeaf("la1");
		la1.add(new BigDecimal(80));
		AccountingLeaf la2 = new AccountingLeaf("la2");
		la2.add(new BigDecimal(20));
		
		na.addChild(la1);
		na.addChild(la2);
		
		AccountingNode nb = new AccountingNode("B");
		
		AccountingLeaf lb1 = new AccountingLeaf("lb1");
		lb1.add(new BigDecimal(10));
		AccountingLeaf lb2 = new AccountingLeaf("lb2");
		lb2.add(new BigDecimal(30));
		
		nb.addChild(lb1);
		nb.addChild(lb2);
		
		AccountingNode nc = new AccountingNode("C");
		
		AccountingLeaf lc1 = new AccountingLeaf("lc1");
		lc1.add(new BigDecimal(20));
		AccountingLeaf lc2 = new AccountingLeaf("lc2");
		lc2.add(new BigDecimal(70));
		
		nc.addChild(lc1);
		nc.addChild(lc2);
		
		List<INode<MutableFloat>> list = new ArrayList<INode<MutableFloat>>();
		list.add(na);
		list.add(nb);
		list.add(nc);
		
		Collections.sort(list, new NodeComparator());
		
		for (INode<MutableFloat> n : list) {
			System.out.println(n.getContent() + ":" + n.getValue());
		}
	}
}
