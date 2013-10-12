package com.redorb.mcharts.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import com.redorb.mcharts.core.Core;
import com.redorb.mcharts.core.accounting.Account;
import com.redorb.mcharts.core.accounting.Category;
import com.redorb.mcharts.core.accounting.IAccountingObject;
import com.redorb.mcharts.core.accounting.Payee;

/**
 * List model for showing of list of accounting objects.
 */
@SuppressWarnings("serial")
public class AccountingObjectListModel extends AbstractListModel<IAccountingObject> {
	
	/*
	 * Attributes
	 */
	
	private List<IAccountingObject> objects =
		new ArrayList<IAccountingObject>();
	
	/*
	 * Ctor
	 */
	
	public AccountingObjectListModel(Class<? extends IAccountingObject> accountingObjectListModel) {
		
		if (accountingObjectListModel.equals(Account.class)) {
			for (Account a : Core.getInstance().getAccounts().values()) {
				objects.add(a);
			}
		}
		else if (accountingObjectListModel.equals(Category.class)) {
			for (Category c : Core.getInstance().getCategories().values()) {
				objects.add(c);
			}
		}
		else if (accountingObjectListModel.equals(Payee.class)) {
			for (Payee p : Core.getInstance().getPayees().values()) {
				objects.add(p);
			}
		}
	}
	
	public AccountingObjectListModel(List<IAccountingObject> selected) {
		
		for (IAccountingObject o : selected) {
			objects.add(o);
		}
	}
	
    /*
     * Operations
     */
    
    public void add(IAccountingObject o) {
    	
    	objects.add(o);
    	fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
    }
    
    public void remove(IAccountingObject o) {
    	
    	int index = objects.indexOf(o);
    	objects.remove(index);
    	fireIntervalRemoved(this, index, index);
    }
    
    /*
     * Getters/Setters
     */
    
    @Override
    public int getSize() { 
    	return objects.size(); 
    }
    
    @Override
    public IAccountingObject getElementAt(int i) { 
    	return objects.get(i); 
    }
    
    public List<IAccountingObject> getObjects() {
    	return objects;
    }
}
