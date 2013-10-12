package com.redorb.mcharts.ui.config;

import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.commons.ui.Utils;
import com.redorb.mcharts.ui.Conf;

@SuppressWarnings("serial")
public class LanguagePane extends JPanel implements IConfigPane {

	/*
	 * Attributes
	 */

	private JToggleButton butFrench;
	private JToggleButton butEnglish;
	
	/*
	 * Ctors
	 */
	
	public LanguagePane() {
		initComponents();
		initLayout();
	}

	/*
	 * Operations
	 */
	
	private void initComponents() {
		
		butFrench = new JToggleButton(
				I18n.getMessage("languagePane.french"),
				Utils.getIcon("/images/32x32/french.png"));
		
		butEnglish = new JToggleButton(
				I18n.getMessage("languagePane.english"), 
				Utils.getIcon("/images/32x32/english.png"));
		
		ButtonGroup group = new ButtonGroup();
		group.add(butEnglish);
		group.add(butFrench);
	}
	
	private void initLayout() {
		
		setLayout(new GridBagLayout());
		
		add(butFrench, new GBC(0, 0));
		add(butEnglish, new GBC(0, 1));
		
		add(Box.createGlue(), new GBC(0, 2, GBC.BOTH));
	}

	@Override
	public void restoreProperties() {
		
		if (Conf.LANGUAGE_EN.equals(Conf.getProps().getString(Conf.PROP_LANGUAGE))) {
			butEnglish.setSelected(true);
		}
		else {
			butFrench.setSelected(true);
		}
	}

	@Override
	public void saveProperties() {
		
		String language = Conf.LANGUAGE_FR;
		String country = Conf.COUNTRY_FR;		
		
		if (butEnglish.isSelected()) {
			language = Conf.LANGUAGE_EN;
			country = Conf.COUNTRY_EN;			
		}
		
		Conf.getProps().setProperty(Conf.PROP_LANGUAGE, language);
		Conf.getProps().setProperty(Conf.PROP_COUNTRY, country);
		
		I18n.setLanguage(language, country);
	}
}
