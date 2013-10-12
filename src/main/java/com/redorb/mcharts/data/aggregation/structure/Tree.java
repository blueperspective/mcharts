package com.redorb.mcharts.data.aggregation.structure;

import java.util.List;

import com.redorb.mcharts.utils.MutableFloat;

public class Tree {

	/*
	 * Attributes
	 */
	
	private INode<MutableFloat> root = new AccountingNode(Tree.class);

	/*
	 * Ctors
	 */
	
	public Tree() {}

	/*
	 * Operations
	 */
	
	/**
	 * Get the value associated to the key list.
	 * Constructive call, will always return a result.
	 */
	public AccountingLeaf getLeaf(List<Object> keys) {
		
		return getLeafRecursive(root, keys, 0);
	}
	
	/**
	 * Recursive getValue
	 * @param node the current node
	 * @param keys the list of keys
	 * @param depth the depth in the tree
	 * @return
	 */
	private AccountingLeaf getLeafRecursive(
			INode<MutableFloat> node,
			List<Object> keys, 
			int depth) {
		
		AccountingLeaf result = null;
		
		// indicates that the key has been found in the childs
		boolean found = false;
				
		// search recursively for the node
		for (int i = 0; i < node.getChildren().size() && !found; i++) {
			
			INode<MutableFloat> child = node.getChildren().get(i);
						
			if (child.getContent().equals(keys.get(depth))) {
				found = true;
				
				if (depth == (keys.size() - 1)) { // leaf : return amount
					result = (AccountingLeaf) child;
				}
				else { // continue exploration in child node
					result = getLeafRecursive(child, keys, depth + 1);
				}
			}
		}
		
		// no amount found, and depth == last index of keys, build the leaf
		if (result == null && depth == (keys.size() - 1)) {
			
			AccountingLeaf child = new AccountingLeaf(keys.get(keys.size() - 1));
			node.addChild(child);
			
			result = child;
		}
		// no amount, but depth < last index of keys, build a node recursively
		else if (result == null && depth < (keys.size() - 1)) {
			
			INode<MutableFloat> child = new AccountingNode(keys.get(depth));
			node.addChild(child);
			
			result = getLeafRecursive(child, keys, depth + 1);
		}
		
		return result;
	}

	/*
	 * Getters/Setters
	 */
	
	/**
	 * @return the root
	 */
	public INode<MutableFloat> getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(INode<MutableFloat> root) {
		this.root = root;
	}
	
	public String toString() {
		return root.toString();
	}
}
