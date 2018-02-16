package com.redorb.mcharts.data.aggregation.aggregator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Category.Kind;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.aggregation.Dimension;
import com.redorb.mcharts.data.aggregation.structure.AccountingTree;

public class KindAggregatorTest {

	@Test
	public void aggregate_one_dimension() {
		
		KindAggregator agg = new KindAggregator(Dimension.PAYEE);
		
		List<Transaction> transactions = new ArrayList<>();
		
		Account a = new Account("a");
		
		Category c1 = new Category("c1", Kind.INCOME);
		Category c2 = new Category("c2", Kind.INCOME);
		Category c3 = new Category("c3", Kind.OUTCOME);
		
		Payee p1 = new Payee("p1");
		Payee p2 = new Payee("p2");
				
		// 1st payee
		
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
				c2,
				null,
				0));
		
		transactions.add(new Transaction(
				2,
				0, 
				a, 
				new Date(), 
				new BigDecimal("70"),
				p1,
				c3,
				null,
				0));
		
		/// 2nd payee
		
		transactions.add(new Transaction(
				3,
				0, 
				a, 
				new Date(), 
				new BigDecimal("50"),
				p2,
				c2,
				null,
				0));
		
		transactions.add(new Transaction(
				4,
				0, 
				a, 
				new Date(), 
				new BigDecimal("30"),
				p2,
				c3,
				null,
				0));
				
		transactions.add(new Transaction(
				5,
				0, 
				a, 
				new Date(), 
				new BigDecimal("150"),
				p2,
				c2,
				null,
				0));
		
		transactions.add(new Transaction(
				6,
				0, 
				a, 
				new Date(), 
				new BigDecimal("1200"),
				p2,
				c3,
				null,
				0));
				
		agg.aggregate(transactions);
		
		AccountingTree itree = agg.getIncomeTree();
		
		Assert.assertEquals(itree.getRoot().getChild(0).getValue(), new BigDecimal("110"));
		Assert.assertEquals(itree.getRoot().getChild(1).getValue(), new BigDecimal("200"));
		
		AccountingTree otree = agg.getOutcomeTree();
		
		Assert.assertEquals(otree.getRoot().getChildrenCount(), 2);
		
		Assert.assertEquals(otree.getRoot().getChild(0).getValue(), new BigDecimal("70"));
		Assert.assertEquals(otree.getRoot().getChild(1).getValue(), new BigDecimal("1230"));
	}
}
