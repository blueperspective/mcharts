package com.redorb.mcharts.ui.config;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.redorb.commons.ui.GBC;

@SuppressWarnings("serial")
public class GenericPrefPanel extends JPanel implements IConfigPane {

	/*
	 * Attributes
	 */

	private List<Pref> prefs = new ArrayList<Pref>();

	/*
	 * Ctors
	 */
	
	protected GenericPrefPanel() {
		super();
	}


	/*
	 * Operations
	 */
	
	protected final void addPref(Pref p) {
		prefs.add(p);
	}

	protected void initComponents() {

		setLayout(new GridBagLayout());

		int y = 0;

		for (Pref p : prefs) {

			// label
			JLabel label = new JLabel(p.getName());
			label.setToolTipText(p.getDescription());

			// comp
			JComponent comp = p.getComponent();

			// layout
			add(label, new GBC(0, y).setAnchor(GBC.NORTHWEST));
			GBC gbc = p.getGBC().setGridY(y);
			add(comp, gbc);
			
			y++;
		}
		
		add(Box.createGlue(), new GBC(0, y, GBC.VERTICAL));
	}
	
	@Override
	public final void restoreProperties() {
		for (Pref p : prefs) {
			p.restoreValue();
		}
	}
	
	@Override
	public final void saveProperties() {
		for (Pref p : prefs) {
			p.saveValue();
		}
	}
}
