package com.redorb.mcharts.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RingSelectionPanelTest {

	public void createFrame() {
		
		RingSelectionPanel<String> ringSelection = new RingSelectionPanel<>(7,
				Arrays.asList("Janvier,Fevrier,Mars,Avril,Mai,Juin,Juillet,Aout,Septembre,Octobre,Novembre,Decembre".split(",")));
		
		ringSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
			}
		});
		
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
				RingSelectionPanelTest ringTest = new RingSelectionPanelTest();
				ringTest.createFrame();
			}
		});
	}

	/*
	 * Attributes
	 */

	/*
	 * Ctors
	 */

	/*
	 * Operations
	 */

	/*
	 * Getters/Setters
	 */
}
