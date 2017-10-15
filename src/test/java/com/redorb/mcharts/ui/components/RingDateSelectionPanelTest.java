package com.redorb.mcharts.ui.components;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RingDateSelectionPanelTest {

	public void createFrame() {
		
		RingDateSelector ringSelection = new RingDateSelector();
		
		JFrame frame = new JFrame();
		frame.setContentPane(ringSelection);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 300);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				RingDateSelectionPanelTest ringTest = new RingDateSelectionPanelTest();
				ringTest.createFrame();
			}
		});
	}
}
