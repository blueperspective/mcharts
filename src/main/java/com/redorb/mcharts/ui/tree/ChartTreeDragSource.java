package com.redorb.mcharts.ui.tree;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class ChartTreeDragSource implements DragSourceListener, DragGestureListener {

	/*
	 * Attributes
	 */
	
	private DragSource source;

	private TransferableTreeNode transferable;

	private JTree sourceTree;

	/*
	 * Ctor
	 */
	
	public ChartTreeDragSource(JTree tree, int actions) {
		sourceTree = tree;
		source = new DragSource();
		source.createDefaultDragGestureRecognizer(sourceTree,
				actions, this);
	}

	/*
	 * Drag Gesture Handler
	 */
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		
		TreePath path = sourceTree.getSelectionPath();
		
		if ((path == null) || (path.getPathCount() <= 2)) {
			// We can't move the root node or an empty selection
			return;
		}
		
		/*
		oldNode = (SavedChart) path.getLastPathComponent();
		oldCategory = (ChartsCategory) path.getPathComponent(1);
		*/
		transferable = new TransferableTreeNode(path);
		
		System.out.println("source tr = " + transferable + ", treepath = " + path);
		
		source.startDrag(dge, DragSource.DefaultMoveNoDrop, transferable, this);
	}

	/*
	 * Drag Event Handlers
	 */
	@Override
	public void dragEnter(DragSourceDragEvent dsde) {}

	@Override
	public void dragExit(DragSourceEvent dse) {}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
		
		System.out.println("Action: " + dsde.getDropAction());
		System.out.println("Target Action: " + dsde.getTargetActions());
		System.out.println("User Action: " + dsde.getUserAction());
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		
		/*
		if (dsde.getDropSuccess()) {
			((DefaultTreeModel) sourceTree.getModel()).removeChart(oldCategory, oldNode);
		}
		*/
	}
}
