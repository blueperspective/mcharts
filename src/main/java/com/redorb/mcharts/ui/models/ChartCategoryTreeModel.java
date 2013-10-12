package com.redorb.mcharts.ui.models;

import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.redorb.mcharts.core.charts.ChartFileInfo;
import com.redorb.mcharts.core.charts.ChartsCategory;
import com.redorb.mcharts.utils.TreeModelSupport;

public class ChartCategoryTreeModel extends TreeModelSupport implements TreeModel {

	/*
	 * Attributes
	 */
	
	private static final String FAKE_ROOT = "root";

	private List<ChartsCategory> categories;

	/*
	 * Ctor
	 */
	
	public ChartCategoryTreeModel(JTree source, List<ChartsCategory> categories) {
		super(source);
		this.categories = categories;
	}
	
	/*
	 * Operations
	 */

	@Override
	public Object getRoot() {
		return FAKE_ROOT;
	}

	@Override
	public Object getChild(Object parent, int index) {

		Object child = null;

		if (FAKE_ROOT.equals(parent)) {
			child = categories.get(index);
		}
		else if (parent instanceof ChartsCategory) {
			child = ((ChartsCategory) parent).getChart(index);
		}

		return child;
	}

	@Override
	public int getChildCount(Object parent) {

		int childCount = 0;

		if (FAKE_ROOT.equals(parent)) {
			childCount = categories.size();
		}
		else if (parent instanceof ChartsCategory) {
			childCount = ((ChartsCategory) parent).getCount();
		}

		return childCount;
	}

	@Override
	public boolean isLeaf(Object node) {

		boolean leaf = true;

		if (FAKE_ROOT.equals(node)) {
			leaf = (categories == null || categories.isEmpty());
		}
		else if (node instanceof ChartsCategory) {
			leaf = ((ChartsCategory) node).getCount() == 0;
		}

		return leaf;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

		Object lastPath = path.getLastPathComponent();
		
		if (lastPath instanceof ChartsCategory) {
			ChartsCategory category = (ChartsCategory) lastPath;
			category.setName(newValue.toString());
		}
		else if (lastPath instanceof ChartFileInfo) {
			ChartFileInfo chartFileInfo = (ChartFileInfo) lastPath;
			chartFileInfo.setName(newValue.toString());
		}
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {

		int index = -1;

		if (FAKE_ROOT.equals(parent)) {
			index = categories.indexOf(child);
		}
		else if (parent instanceof ChartsCategory) {
			index = ((ChartsCategory) parent).indexOf((ChartFileInfo) child);
		}

		return index;
	}

	public void addCategory(ChartsCategory category) {

		categories.add(category);

		fireTreeNodesInserted(
				new Object[] { FAKE_ROOT },
				new int[] { categories.size() - 1 },
				new Object[] { category } );
	}

	public void removeCategory(ChartsCategory category) {

		fireTreeNodesRemoved(
				new Object[] { FAKE_ROOT },
				new int [] { categories.indexOf(category) },
				new Object[] { FAKE_ROOT });
		
		categories.remove(category);
	}

	public void addChart(ChartsCategory category, ChartFileInfo chartFileInfo) {

		category.add(chartFileInfo);

		TreeModelEvent evt = new TreeModelEvent(this, 
				new Object[] { FAKE_ROOT, category });

		for (TreeModelListener listener : listeners) {
			listener.treeStructureChanged(evt);
		}
	}

	public void removeChart(ChartsCategory category, ChartFileInfo chartFileInfo) {
		
		fireTreeNodesRemoved(
				new Object[] { FAKE_ROOT, category },
				new int [] { category.indexOf(chartFileInfo) },
				new Object[] { chartFileInfo });
		
		category.remove(chartFileInfo);
	}
}
