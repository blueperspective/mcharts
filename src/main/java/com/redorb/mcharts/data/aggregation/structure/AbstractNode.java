package com.redorb.mcharts.data.aggregation.structure;

import java.util.ArrayList;
import java.util.List;

import com.redorb.mcharts.data.aggregation.visitor.IVisitor;

/**
 * An AccountingNode is part of an AccountingTree.
 * The node represents an aggregated value, the leaf represents an aggregated
 * value + the amount value.
 */
public abstract class AbstractNode<T> implements INode<T> {

	/*
	 * Attributes
	 */
	
	protected INode<T> parent = null;
	
	/** value of the node (aggregated class) */
	protected Object content = null;
			
	/** children nodes */
	protected List<INode<T>> children = new ArrayList<INode<T>>();

	/*
	 * Ctors
	 */
	
	public AbstractNode(Object content) {
		this.content = content;
	}

	/*
	 * Operations
	 */
	
	@Override
	public boolean equals(Object o) {
		
		boolean res = false;
		
		if (o != null && o instanceof AbstractNode<?>) {
			
			AbstractNode<T> node = (AbstractNode<T>) o;
			res = content.equals(node.content);
		}
		
		return res;
	}
	
	@Override
	public int hashCode() {
		
		return content.hashCode();
	}
	
	@Override
	public void addChild(INode<T> child) {
		
		children.add(child);
		child.setParent(this);
	}
	
	@Override
	public void removeChild(INode<T> child) {
		
		children.remove(child);
	}
	
	@Override
	public INode<T> getParent() {
		return parent;
	}
	
	@Override
	public void setParent(INode<T> parent) {
		this.parent = parent;
	}

	/*
	 * Getters/Setters
	 */
	
	public boolean isLeaf() {
		return false;
	}
		
	/**
	 * @return the value
	 */
	@Override
	public Object getContent() {
		return content;
	}
	
	/**
	 * @return the value
	 */
	public void setContent(Object content) {
		this.content = content;
	}

	/**
	 * @return the children
	 */
	public List<INode<T>> getChildren() {
		return children;
	}

	@Override
	public int getChildrenCount() {
		return children.size();
	}

	@Override
	public INode<T> getChild(int index) {
		return children.get(index);
	}
	
	public void accept(IVisitor<T> visitor) {
		
		for (INode<T> child : children) {
			child.accept(visitor);
		}
		
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return toStringRec(this, "");
	}
	
	private String toStringRec(INode<T> node, String indent) {
		
		StringBuilder res = new StringBuilder();
		
		res.append(indent);
		res.append(node.getContent());
		res.append("\n");
		
		for (INode<T> child : node.getChildren()) {
			
			if (child.isLeaf()) {
				res.append(indent + "\t");
				res.append(child.toString());
			}
			else {
				res.append(toStringRec(child, indent + "\t"));
			}
			
			res.append("\n");
		}
		
		return res.toString();
	}
}
