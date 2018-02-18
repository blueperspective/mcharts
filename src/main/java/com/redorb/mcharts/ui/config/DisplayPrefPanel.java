package com.redorb.mcharts.ui.config;

import com.redorb.mcharts.ui.Conf;

@SuppressWarnings("serial")
public class DisplayPrefPanel extends GenericPrefPanel {

	/*
	 * Attributes
	 */

	/*
	 * Ctors
	 */
	
	public DisplayPrefPanel() {
		super();
		initPrefs();
		initComponents();
	}

	/*
	 * Operations
	 */
	
	public void initPrefs() {
		
		addPref(new Pref(Conf.PROP_SHOW_TRANSACTIONS, Pref.Type.BOOLEAN));
		
		addPref(new Pref(Conf.PROP_SHOW_TREES, Pref.Type.BOOLEAN));
				
		addPref(new Pref(Conf.PROP_HIDE_TRANSFERTS_LIST, Pref.Type.LIST));
	}
}
