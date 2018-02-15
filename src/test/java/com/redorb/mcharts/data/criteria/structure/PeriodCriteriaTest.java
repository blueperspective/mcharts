package com.redorb.mcharts.data.criteria.structure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;
import com.redorb.mcharts.data.criteria.filter.CriteriaFilter;

public class PeriodCriteriaTest {

	@Test
	public void filter() {
		
		List<Transaction> transactions = new ArrayList<>();
		
		Account account = new Account("account", null);
		Payee payee = new Payee("payee");
		Category category = new Category("category");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		
		transactions.add(new Transaction(
				0, 
				account, 
				cal.getTime(), 
				new BigDecimal(100),
				payee,
				category,
				null,
				0));
		
		cal.add(Calendar.MONTH, 3);
		
		transactions.add(new Transaction(
				0, 
				account, 
				cal.getTime(), 
				new BigDecimal(10),
				payee,
				category,
				null,
				0));
		
		cal.add(Calendar.MONTH, 1);
		
		transactions.add(new Transaction(
				0, 
				account, 
				cal.getTime(), 
				new BigDecimal(50),
				payee,
				category,
				null,
				0));
		
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		
		Date startDate = cal.getTime();
		
		cal.add(Calendar.MONTH, 6);
		
		Date endDate = cal.getTime();
		
		ICriteria periodCriteria = new PeriodCriteria(startDate, endDate);
		
		Assert.assertEquals(3, transactions.size());
		
		List<Transaction> filtered = CriteriaFilter.filter(transactions, periodCriteria);
		
		Assert.assertEquals(2, filtered.size());
	}
}
