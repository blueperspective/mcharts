package com.redorb.mcharts.data.aggregation.aggregator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public class GlobalAggregatorTest {

	@Test
	public void aggregate() {

		GlobalAggregator agg = new GlobalAggregator(Dimension.PAYEE);

		List<Transaction> transactions = new ArrayList<>();
		
		Account a = new Account("a");
		
		Category c1 = new Category("c1");
		Category c2 = new Category("c2");

		Payee p1 = new Payee("p1");
		Payee p2 = new Payee("p2");
		
		transactions.add(new Transaction(
				0,
				0, 
				a, 
				new Date(), 
				new BigDecimal("100"),
				p1,
				c1,
				null,
				0));

		transactions.add(new Transaction(
				1,
				0, 
				a, 
				new Date(), 
				new BigDecimal("10"),
				p1,				
				c1,
				null,
				0));

		transactions.add(new Transaction( 
				2,
				0, 
				a, 
				new Date(), 
				new BigDecimal("50"),
				p2,
				c2,
				null,
				0));

		transactions.add(new Transaction(
				3,
				0, 
				a, 
				new Date(), 
				new BigDecimal("150"),
				p2,
				c2,
				null,
				0));

		agg.aggregate(transactions);

		AccountingTree tree = agg.getTree();

		Assert.assertEquals(2, tree.getRoot().getChildrenCount());

		Assert.assertEquals(new BigDecimal("110"), tree.getRoot().getChild(0).getValue());

		Assert.assertEquals(new BigDecimal("200"), tree.getRoot().getChild(1).getValue());
	}
}
