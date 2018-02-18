package com.redorb.mcharts.data.aggregation.aggregator;

import java.util.ArrayList;
import java.util.List;

import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.structure.AccountingLeaf;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.perf.Perf;
import com.redorb.mcharts.ui.Conf;

public class GlobalAggregator implements IAggregator {

	/*
	 * Attributes
	 */

	/** list of dimensions of the aggregator */
	private Dimension[] dimensions = null;

	private AccountingTree tree = null;

	/*
	 * Ctors
	 */

	public GlobalAggregator(Dimension... dimensions) {
		this.dimensions = dimensions;
		tree = new AccountingTree(dimensions);
	}

	/*
	 * Operations
	 */

	@Override
	public void aggregate(List<Transaction> transactions) {

		Perf.getInstance().takeMeasure("start aggregation");

		// for each transaction
		for (Transaction transaction : transactions) {

			// get the keys of the transactions: values of the dimensions

			List<Object> keys = null;

			if (dimensions != null)
			{
				keys = new ArrayList<>();
				for (Dimension dimension : dimensions) {
					keys.add(DimensionGetter.getDimension(dimension, transaction));
				}
			}

			// get the amount using the keys (the getValue builds the tree)

			AccountingLeaf value = tree.getLeaf(keys);
			value.add(transaction.getAmount());
		}

		Perf.getInstance().takeMeasure("end aggregation");
	}

	/*
	 * Getters/Setters
	 */

	@Override
	public Dimension[] getDimensions() {
		return dimensions;
	}

	public AccountingTree getTree() {
		return tree;
	}
}
