package com.redorb.mcharts.ui.components;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.ListenerList;

@SuppressWarnings("serial")
public class RingDateSelector extends JPanel {

	/*
	 * Attributes
	 */
	
	private static final int MAX_YEARS = 7;
	
	private ListenerList listeners = new ListenerList();
	
	private SlidebarPanel<Integer> yearSelector = new SlidebarPanel<>(5); 
	
	private RingSelectionPanel<String> monthSelector = new RingSelectionPanel<>(
			5, Arrays.asList("Janvier,Fevrier,Mars,Avril,Mai,Juin,Juillet,Aout,Septembre,Octobre,Novembre,Decembre".split(",")));
	
	/*
	 * Ctors
	 */
	
	public RingDateSelector() {
		initComponents();
		initLayout();
	}
	
	private void initComponents() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		List<Integer> years = new ArrayList<>();
		
		for (int i = MAX_YEARS - 1; i >= 0; i--) {
			years.add(cal.get(Calendar.YEAR) - i);
		}
		
		yearSelector.setObjets(years);
		
		yearSelector.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				listeners.fireAction(new ActionEvent(this, 0, ""));
			}
		});
		
		monthSelector.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (e.getActionCommand() == RingSelectionPanel.CMD_BACKWARD_LOOP) {
					yearSelector.setSelectedIndex(yearSelector.getSelectedIndex() - 1);
				}
				else if (e.getActionCommand() == RingSelectionPanel.CMD_FORWARD_LOOP) {
					yearSelector.setSelectedIndex(yearSelector.getSelectedIndex() + 1);
				}
				
				listeners.fireAction(new ActionEvent(this, 0, ""));
			}
		});
	}
	
	private void initLayout() {
		
		setLayout(new GridBagLayout());
		
		add(yearSelector, new GBC(0, 0, GBC.HORIZONTAL));
		add(monthSelector, new GBC(0, 1, GBC.HORIZONTAL));
	}

	/*
	 * Operations
	 */
	
	public void addActionListener(ActionListener l) { 
		
		listeners.addActionListener(l);
	}
	
	public void setMonth(int month) {
		
		monthSelector.setSelectedIndex(month);
	}
	
	public int getMonth() {
		
		return monthSelector.getSelectedIndex();
	}
	
	public void setYear(int year) {
		
		yearSelector.setSelectedElement(year);
	}
	
	public int getYear() {
		
		return yearSelector.getSelectedElement();
	}
}
