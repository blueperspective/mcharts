package com.redorb.mcharts.ui.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.TreePath;

public class TransferableTreeNode implements Transferable {

	public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class,
			"Tree Path");

	/*
	 * Attributes
	 */
	
	private DataFlavor flavors[] = { TREE_PATH_FLAVOR };

	private TreePath path;

	/*
	 * Constructors
	 */
	
	public TransferableTreeNode(TreePath tp) {
		path = tp;
	}
	
	/*
	 * Operations
	 */

	public synchronized DataFlavor[] getTransferDataFlavors() {
		System.out.println("getTransferDataFlavors");
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		System.out.println("isDataFlavorSupported");
		return (flavor.getRepresentationClass() == TreePath.class);
	}

	public synchronized Object getTransferData(DataFlavor flavor)
	throws UnsupportedFlavorException, IOException {
		System.out.println("getTransferData : " + path);
		if (isDataFlavorSupported(flavor)) {
			Object res = path;
			System.out.println("res : " + res);
			return res;
		} else {
			throw new UnsupportedFlavorException(flavor);
		}
	}
}
