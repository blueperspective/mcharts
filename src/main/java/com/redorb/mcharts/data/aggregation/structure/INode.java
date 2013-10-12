package com.redorb.mcharts.data.aggregation.structure;

import java.util.List;

import com.redorb.mcharts.data.aggregation.visitor.IVisitor;

public interface INode<T> {

	boolean isLeaf();
	
	INode<T> getParent();
	
	void setParent(INode<T> parent);

	int getChildrenCount();

	List<INode<T>> getChildren();

	INode<T> getChild(int index);

	T getValue();
	
	Object getContent();

	void addChild(INode<T> child);
	
	void removeChild(INode<T> child);
	
	void accept(IVisitor<T> visitor);
}
