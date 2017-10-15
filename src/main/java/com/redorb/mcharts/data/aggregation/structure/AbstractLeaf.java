package com.redorb.mcharts.data.aggregation.structure;

import java.util.List;

import com.redorb.mcharts.data.aggregation.visitor.IVisitor;

public class AbstractLeaf<T> implements INode<T> {

	/*
	 * Attributes
	 */
	
	protected INode<T> parent = null;
	
	/** value of the node (aggregated class) */
	protected Object content = null;
	
	protected T value = null;
	
	/*
	 * Ctors
	 */
	
	public AbstractLeaf(final T value) {
		this.value = value;
	}
	
	public AbstractLeaf(final Object content, final T value) {
		this.content = content;
		this.value = value;
	}
	
	/*
	 * Operations
	 */
	
	@Override
	public boolean equals(Object o) {
		
		boolean res = false;
		
		if (o instanceof AbstractLeaf<?>) {
			
			AbstractLeaf<?> leaf = (AbstractLeaf<?>) o;
			res = value.equals(leaf.getValue());
		}
		
		return res;
	}
	
	@Override
	public int hashCode() {
		
		return value.hashCode();
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public int getChildrenCount() {
		return 0;
	}

	@Override
	public List<INode<T>> getChildren() {
		return null;
	}

	@Override
	public INode<T> getChild(int index) {
		return null;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void addChild(INode<T> child) {}

	@Override
	public void removeChild(INode<T> child) {}

	@Override
	public void accept(IVisitor<T> visitor) {
		visitor.visit(this);
	}
	
	@Override
	public INode<T> getParent() {
		return parent;
	}
	
	@Override
	public void setParent(INode<T> parent) {
		this.parent = parent;
	}
	
	public String toString() {
		
		return "leaf: " + content.toString() + " = " + value.toString();
	}

	@Override
	public Object getContent() {
		return content;
	}
}
