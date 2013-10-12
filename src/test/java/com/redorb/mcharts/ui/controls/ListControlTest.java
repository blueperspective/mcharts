package com.redorb.mcharts.ui.controls;

import java.awt.Dialog.ModalityType;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import com.redorb.commons.ui.GBC;
import com.redorb.mcharts.ui.components.SlidebarPanel;

public class ListControlTest {
	
	public static void main(String[] args) {
		
		JDialog dialog = new JDialog((Window) null, ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setSize(600, 800);
		
		dialog.setLayout(new GridBagLayout());
		
		SlidebarPanel<String> testControl = new SlidebarPanel<>(7,
				Arrays.asList("Janvier,Fevrier,Mars,Avril,Mai,Juin,Juillet,Aout,Septembre,Octobre,Novembre,Decembre".split(",")));
		
		dialog.add(testControl, new GBC(0, 0, GBC.BOTH));
		
		dialog.setVisible(true);		
	}
}
