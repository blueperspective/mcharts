package com.redorb.mcharts.data.aggregation.aggregator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.structure.AccountingLeaf;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;
import com.redorb.mcharts.perf.Perf;
import com.redorb.mcharts.ui.Conf;
import com.redorb.mcharts.ui.charts.ChartPanelCreator;

/**
 * A tree aggregator aggregates transactions using a list of dimensions and
 * produces an AccountingTree.
 * Each tree level corresponds to a dimension.
 */
public class KindAggregator implements IAggregator {

	/*
	 * Attributes
	 */
	
	/** list of dimensions of the aggregator */
	private Dimension[] dimensions = null;
	
	private AccountingTree incomeTree = null;
	
	private AccountingTree outcomeTree = null;
	
	private final Logger log = LoggerFactory.getLogger(ChartPanelCreator.class);
	
	private boolean skipInternalTransactions = Conf.getProps().getBoolean(Conf.PROP_SKIP_INTERNAL_TRANSACTIONS, false);
	
	/*
	 * Ctors
	 */

	public KindAggregator(Dimension... dimensions) {
		this.dimensions = dimensions;
		incomeTree = new AccountingTree(dimensions);
		outcomeTree = new AccountingTree(dimensions);
	}

	/*
	 * Operations
	 */

	/**
	 * Aggregates a collection of transaction into an AccountingTree.
	 * @param transactions a collection of transactions
	 * @return the result as an AccountingTree
	 */
	public void aggregate(List<Transaction> transactions) {

		Perf.getInstance().takeMeasure("start aggregation");
		
		// result
		AccountingTree tree = null;

		// for each transaction
		for (Transaction transaction : transactions) {
			
			if (transaction.getLinkedTransaction() != 0 && skipInternalTransactions) {
				continue;
			}
						
			if (transaction.getCategory().getKind() == Category.Kind.INCOME) {
				tree = incomeTree;
			}
			else {
				tree = outcomeTree;
			}

			// get the keys of the transactions: values of the dimensions
			
			List<Object> keys = new ArrayList<Object>();
			
			for (Dimension dimension : dimensions) {
				keys.add(DimensionGetter.getDimension(dimension, transaction));
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
	
	/**
	 * @return the dimensions
	 */
	@Override
	public Dimension[] getDimensions() {
		return dimensions;
	}

	/**
	 * @return the incomeTree
	 */
	public AccountingTree getIncomeTree() {
		return incomeTree;
	}

	/**
	 * @return the outcomeTree
	 */
	public AccountingTree getOutcomeTree() {
		return outcomeTree;
	}
	
	@Override
	public boolean getSkipInternalTransactions() {
		return skipInternalTransactions;
	}

	@Override
	public void setSkipInternalTransactions(boolean value) {
		this.skipInternalTransactions = value;
	}
}
