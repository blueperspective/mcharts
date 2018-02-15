package com.redorb.mcharts.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RingDateSelectionPanelTest {

	public void createFrame() {

		final RingDateSelector ringSelection = new RingDateSelector();

		ringSelection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("Selected month: " + ringSelection.getMonth());
				System.out.println("Selected elt: " + ringSelection.getYear());				
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
				RingDateSelectionPanelTest ringTest = new RingDateSelectionPanelTest();
				ringTest.createFrame();


			}
		});
	}
}
