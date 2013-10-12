package com.redorb.mcharts;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.LoggerFactory;

import com.jgoodies.looks.Options;

import com.redorb.mcharts.perf.Perf;
import com.redorb.mcharts.ui.Conf;
import com.redorb.mcharts.ui.MchartFrame;

public class MCharts {

	/**
	 * Runs the application
	 * @param args
	 */
	public static void main(final String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					
					Perf.getInstance();

					UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
					UIManager.put("jgoodies.useNarrowButtons", Boolean.TRUE);
					UIManager.put("jgoodies.popupDropShadowEnabled", Boolean.TRUE);
					Options.setDefaultIconSize(new Dimension(18, 18));

					try {
						UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
					} catch (Exception e) {
						LoggerFactory.getLogger(MCharts.class).error(e.getMessage(), e);
					}
					
					Conf.getInstance().load();
					
					//new MainFrame();
					new MchartFrame();
					
				} catch (IOException e) {
					//log.error(e.getMessage(), e);
					JOptionPane.showMessageDialog(null, 
							"Exception occured while launching the application (" + e.getMessage() + ")");
				}
			}
		});
	}
}
