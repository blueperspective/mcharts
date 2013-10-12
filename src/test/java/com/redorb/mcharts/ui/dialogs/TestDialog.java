package com.redorb.mcharts.ui.dialogs;

import java.awt.Container;

import javax.swing.JFrame;

import com.redorb.commons.ui.I18n;

public class TestDialog {
	
	private static final String PACKAGE = "com.redorb.mcharts.ui.dialogs.";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		I18n.setLanguage("fr", "FR");
		
		//createFrame("explorer.AccountsExplorerPane");
		
		//createFrame("explorer.ExplorerDialog");
		
		//createFrame("config.IgnoreListPane");
		
		// createFrame("SetSelectDialog");
		
		createFrame("control.ControlPane");
	}
	
	public static void createFrame(String dialogName) {
		
		Container container = null;
		Class c;
		try {
			c = Class.forName(PACKAGE + dialogName);
			container = (Container) c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
		
		JFrame frame = new JFrame();
		frame.setContentPane(container);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 300);
		frame.setVisible(true);
	}
}
