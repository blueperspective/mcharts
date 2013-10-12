package com.redorb.mcharts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.Payee;
import com.redorb.mcharts.core.accounting.Transaction;

public class DumpCore {

	private final static Logger log = LoggerFactory.getLogger(DumpCore.class);
	
	public static void dump() {
		
		log.debug("Categories");
		
		for (Category c : Core.getInstance().getCategories().values()) {
			log.debug("cat : " + c.getName());
			
			for (Category sc : c.getSubCategories().values()) {
				log.debug("  sub cat : " + sc.getName());
			}
		}
		
		log.debug("Payees");
		
		for (Payee p : Core.getInstance().getPayees().values()) {
			log.debug(p.getNumber() + " : " + p.getName());
		}
		
		log.debug("Accounts");
		
		for (Account a : Core.getInstance().getAccounts().values()) {
			log.debug("Account : " + a.getName());
			
			for (Transaction t : a.getTransactions()) {
				log.debug("t : " + t.getDate());
				log.debug("t : " + t.getAmount());
				log.debug("t : " + t.getPayee());
			}
		}
	}
}
