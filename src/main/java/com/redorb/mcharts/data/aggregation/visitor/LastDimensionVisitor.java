package com.redorb.mcharts.data.aggregation.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redorb.mcharts.data.aggregation.structure.AccountingLeaf;
import com.redorb.mcharts.data.aggregation.structure.INode;
import com.redorb.mcharts.utils.MutableFloat;

public class LastDimensionVisitor implements IVisitor<MutableFloat> {

	/*
	 * Attributes
	 */
	
	private Map<INode<MutableFloat>, List<AccountingLeaf>> mapNodeLeaves = 
			new HashMap<INode<MutableFloat>, List<AccountingLeaf>>();
	
	private List<AccountingLeaf> workingList = new ArrayList<AccountingLeaf>();

	/*
	 * Ctors
	 */

	/*
	 * Operations
	 */
	
	@Override
	public void visit(INode<MutableFloat> node) {
		
		if (node.isLeaf()) {
			workingList.add((AccountingLeaf) node);
		}
		else {
			
			for (INode<MutableFloat> child : node.getChildren()) {
				visit(child);
			}
			
			if (node.getChildrenCount() > 0 && node.getChild(0).isLeaf()) {
				mapNodeLeaves.put(node, workingList);
				workingList = new ArrayList<AccountingLeaf>();
			}
		}
	}
	
	/*
	 * Getters/Setters
	 */
	
	/**
	 * @return the mapNodeLeaves
	 */
	public Map<INode<MutableFloat>, List<AccountingLeaf>> getMapNodeLeaves() {
		return mapNodeLeaves;
	}

}
