package com.redorb.mcharts.ui.tree;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.charts.ChartsCategory;
import com.redorb.mcharts.core.charts.Chart;

public class ChartTreeDropTarget implements DropTargetListener {

	/*
	 * Attributes
	 */
	
	private final Logger log = LoggerFactory.getLogger(ChartTreeDropTarget.class);
	
	private DropTarget target;

	private JTree targetTree;

	/*
	 * Ctor
	 */
	
	public ChartTreeDropTarget(JTree tree) {
		targetTree = tree;
		target = new DropTarget(targetTree, this);
	}
	
	/*
	 * Operations
	 */

	/*
	 * Drop Event Handlers
	 */
	private TreeNode getNodeForEvent(DropTargetDragEvent dtde) {
		
		Point p = dtde.getLocation();
		DropTargetContext dtc = dtde.getDropTargetContext();
		JTree tree = (JTree) dtc.getComponent();
		TreePath path = tree.getClosestPathForLocation(p.x, p.y);
		
		return (TreeNode) path.getLastPathComponent();
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		
		TreeNode node = getNodeForEvent(dtde);
		
		if (node.isLeaf()) {
			dtde.rejectDrag();
		} else {
			dtde.acceptDrag(dtde.getDropAction());
		}
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		
		TreeNode node = getNodeForEvent(dtde);
		
		if (node.isLeaf()) {
			dtde.rejectDrag();
		} else {
			dtde.acceptDrag(dtde.getDropAction());
		}
	}

	@Override
	public void dragExit(DropTargetEvent dte) {}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		
		// gets the drop point
		Point pt = dtde.getLocation();
		
		// gets the drop context
		DropTargetContext dtc = dtde.getDropTargetContext();
		
		// gets the tree
		JTree tree = (JTree) dtc.getComponent();
		
		// gets the tree path from the drop point
		TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
		
		// gets the parent node
		ChartsCategory parent =
			(ChartsCategory) parentpath.getLastPathComponent();
		
		try {
			// ges the transferable
			Transferable tr = dtde.getTransferable();
						
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			
			for (int i = 0; i < flavors.length; i++) {
				if (tr.isDataFlavorSupported(flavors[i])) {
					dtde.acceptDrop(dtde.getDropAction());
					
					Object o = tr.getTransferData(flavors[i]);
					
					System.out.println("o : " + o);
					
					TreePath p = (TreePath) tr.getTransferData(flavors[i]);
					System.out.println("drop treepath = " + p);
					
					Chart node = (Chart) p.getLastPathComponent();
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					/*model.addNodeToParent(
							new DefaultMutableTreeNode(parent), 
							new DefaultMutableTreeNode(node));*/
					dtde.dropComplete(true);
					return;
				}
			}
			
			dtde.rejectDrop();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			dtde.rejectDrop();
		}
	}
}