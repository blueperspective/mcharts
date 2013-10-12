package com.redorb.mcharts.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public class TreeModelSupport {
	
	/*
	 * Attributes
	 */
	
	public static final Logger log = LoggerFactory.getLogger(TreeModelSupport.class);
	
	protected List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
	protected final Object source;
	
	/*
	 * Ctor
	 */

	public TreeModelSupport(Object source) {
		this.source = source;
	}
	
	/*
	 * Operations
	 */
	
	public boolean isEmpty() {
		return listeners.size() == 0;
	}
	
	public int size() {
		return listeners.size();
	}
	
	public void addTreeModelListener(TreeModelListener listener) {
		listeners.add(listener);
	}
	
	public void removeTreeModelListener(TreeModelListener listener) {
		listeners.remove(listener);
	}
	
	public void fireTreeNodesChanged(Object[] path, int[] childIndices, Object[] children) {	
		fireTreeNodesChanged(new TreeModelEvent(source, path, childIndices, children));
	}
	
	public void fireTreeNodesInserted(Object[] path, int[] childIndices, Object[] children) {
		fireTreeNodesInserted(new TreeModelEvent(source, path, childIndices, children));
	}
	
	public void fireTreeNodesRemoved(Object[] path, int[] childIndices, Object[] children) {		
		fireTreeNodesRemoved(new TreeModelEvent(source, path, childIndices, children));
	}
	
	public void fireTreeStructureChanged(Object[] path, int[] childIndices, Object[] children) {
		fireTreeStructureChanged(new TreeModelEvent(source, path, childIndices, children));
	}
	
	public void fireTreeNodesChanged(TreeModelEvent event) {
		
		for(TreeModelListener listener: listeners) {
			listener.treeNodesChanged(event);
		}		
	}
	
	public void fireTreeNodesInserted(TreeModelEvent event) {

		for(TreeModelListener listener: listeners) {
			listener.treeNodesInserted(event);
		}		
	}
	
	public void fireTreeNodesRemoved(TreeModelEvent event) {

		for(TreeModelListener listener: listeners) {
			listener.treeNodesRemoved(event);
		}		
	}
	
	public void fireTreeStructureChanged(TreeModelEvent event) {

		for(TreeModelListener listener: listeners) {
			listener.treeStructureChanged(event);
		}	
	}

	public TreeModelListener[] getTreeModelListeners() {
		return listeners.toArray(new TreeModelListener[listeners.size()]);
	}
}
