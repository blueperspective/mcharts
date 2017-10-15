package com.redorb.mcharts.ui.explorer;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Category;

@SuppressWarnings("serial")
public class CategoriesExplorerPane extends JPanel {

	/*
	 * Attributes
	 */

	/** categories */
	private JLabel lblCategories;
	
	/** panel for buttons (expand & collapse) */
	private JPanel pnlButtons;
	
	/** expands the tree */
	private JButton butExpand;
	
	/** collapses the tree */
	private JButton butCollapse;
	
	/** scrolls the tree */
	private JScrollPane scrolCategories;
	
	/** tree of categories */
	private JTree treeCategories;

	/*
	 * Ctors
	 */

	public CategoriesExplorerPane() {
		initComponents();
		initLayout();
		initContents();
	}

	/*
	 * Operations
	 */

	/**
	 * Initializes the components.
	 */
	private void initComponents() {

		lblCategories = new JLabel();
		pnlButtons = new JPanel();
		scrolCategories = new JScrollPane();
		treeCategories = new JTree();

		lblCategories.setText(I18n.getMessage(
				"categoriesExplorerPane.jlblCategories"));

		treeCategories.setRootVisible(false);
		
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		Icon categoryIcon = Utils.getIcon("/images/16x16/category.png");
		renderer.setClosedIcon(categoryIcon);
		renderer.setOpenIcon(categoryIcon);
		renderer.setLeafIcon(categoryIcon);
		
		treeCategories.setCellRenderer(renderer);
		scrolCategories.setViewportView(treeCategories);

		butCollapse = Utils.createButton(
				"/images/22x22/collapse.png",
				I18n.getMessage("categoriesExplorerPane.collapse.tooltip"),
				I18n.getMessage("categoriesExplorerPane.collapse"), 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						collapseActionPerformed();
					}},
					true);
		
		butExpand = Utils.createButton(
				"/images/22x22/expand.png", 
				I18n.getMessage("categoriesExplorerPane.expand.tooltip"),
				I18n.getMessage("categoriesExplorerPane.expand"), 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						expandActionPerformed();
					}},
					true);
	}
	
	/**
	 * Initializes the layout.
	 */
	private void initLayout() {
		
		// layout

		setLayout(new GridBagLayout());

		// Y 0

		add(lblCategories, new GBC(0, 0).setAnchor(GBC.WEST));

		// Y 1

		add(scrolCategories, new GBC(0, 1, GBC.BOTH));
		
		// X 1
		
		// button panel
		
		pnlButtons.setLayout(new GridBagLayout());
		
		pnlButtons.add(butCollapse, new GBC(0, 0).setAnchor(GBC.NORTH));
		pnlButtons.add(butExpand, new GBC(0, 1));
		
		pnlButtons.add(Box.createGlue(), new GBC(0, 2, GBC.VERTICAL));
		
		add(pnlButtons, new GBC(1, 1, GBC.VERTICAL));
	}

	private void initContents() {

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("");

		for (Category c : Core.getInstance().getCategories()) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(c.getName());

			if (c.getSubCategories() != null) {
				for (Category s : c.getSubCategories()) {
					DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(s.getName());
					node.add(subNode);
				}
			}
			rootNode.add(node);
		}
		
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		
		treeCategories.setModel(treeModel);
		
		expandActionPerformed();
	}
	
	private void expandActionPerformed() {
		
		int n = treeCategories.getRowCount();
		
		for (int i = n - 1; i >= 0; i--) {
			treeCategories.expandRow(i);
		}
	}
	
	private void collapseActionPerformed() {
		
		for (int i = 0; i < treeCategories.getRowCount(); i++) {
			treeCategories.collapseRow(i);
		}
	}
}
