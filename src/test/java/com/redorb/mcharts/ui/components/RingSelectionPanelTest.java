package com.redorb.mcharts.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RingSelectionPanelTest {

	public void createFrame() {
		
		/*RingSelectionPanel<String> ringSelection = new RingSelectionPanel<>(7,
				Arrays.asList("Janvier,Fevrier,Mars,Avril,Mai,Juin,Juillet,Aout,Septembre,Octobre,Novembre,Decembre".split(",")));*/
		
		final RingSelectionPanel<String> ringSelectionYear = new RingSelectionPanel<>(7,
				Arrays.asList("2015,2016,2017,2018".split(",")), false);
				
		ringSelectionYear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				System.out.println("Selected index: " + ringSelectionYear.getSelectedIndex());
				System.out.println("Selected elt: " + ringSelectionYear.getSelectedElement());				
			}
		});
		
		//System.out.println(ringSelection.getSelectedIndex());
		
		JFrame frame = new JFrame();
		
		JPanel pnl = new JPanel();
		
		//pnl.add(ringSelection);
		pnl.add(ringSelectionYear);
		
		frame.setContentPane(pnl);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 300);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				RingSelectionPanelTest ringTest = new RingSelectionPanelTest();
				ringTest.createFrame();
			}
		});
	}
}
