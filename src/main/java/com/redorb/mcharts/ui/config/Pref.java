package com.redorb.mcharts.ui.config;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.redorb.commons.ui.GBC;
import com.redorb.commons.ui.I18n;
import com.redorb.mcharts.ui.Conf;

public class Pref {

	/*
	 * Attributes
	 */

	public enum Type {
		STRING,
		INT,
		BOOLEAN,
		LIST
	}

	public final static String SUFFIX_DESCRIPTION = ".description";

	/** identifier, to get the property name, description, value */
	private String id;

	private Type type;

	private String name;

	private String description;

	private List<Object> initialValues;

	private JComponent component;

	private GBC gbc;

	/*
	 * Ctors
	 */

	public Pref(String id, Type type) {

		this.id = id;
		this.type = type;
		name = I18n.getMessage(id);
		description = I18n.getMessage(id + SUFFIX_DESCRIPTION);
	}

	/*
	 * Operations
	 */

	public JComponent getComponent() {

		gbc = new GBC(1, 0, GBC.HORIZONTAL);
		
		if (component == null) {

			switch (type) {
			case STRING:
			case INT:
				component = new JTextField();
				break;
			case BOOLEAN:
				component = new JCheckBox();
				break;
			case LIST:
				JTextArea textArea = new JTextArea();
				JScrollPane scrollPane = new JScrollPane(textArea);
				component = scrollPane;
				gbc = new GBC(1, 0, GBC.BOTH);								
				break;
			}
		}

		return component;
	}

	/**
	 * @return the initialValues
	 */
	public List<Object> getInitialValues() {
		return initialValues;
	}

	/**
	 * @param initialValues the initialValues to set
	 */
	public void setInitialValues(List<Object> initialValues) {
		this.initialValues = initialValues;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public GBC getGBC() {
		return gbc;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public void restoreValue() {
		
		Object value = null;
		
		switch (type) {
		case STRING:
			value = Conf.getProps().getString(id, "");
			break;
		case INT:
			value = Conf.getProps().getInt(id, 0);
			break;
		case BOOLEAN:
			value = Conf.getProps().getBoolean(id, false);
			break;
		case LIST:
			value = Conf.getProps().getList(id);
			break;
		}
				
		switch (type) {
		case STRING:
		case INT:
			((JTextField) component).setText(value.toString());
			break;
		case BOOLEAN:
			((JCheckBox) component).setSelected((Boolean) value);
			break;
		case LIST:
			JScrollPane scrollPane = (JScrollPane)component;
			((JTextArea)scrollPane.getViewport().getView()).setText(String.join("\n", (List)value));
			break;
		}
	}
	
	public void saveValue() {
		
		Object value = null;
		
		switch (type) {
		case STRING:
		case INT:
			value = ((JTextField) component).getText();
			break;
		case BOOLEAN:
			value = ((JCheckBox) component).isSelected();
			break;
		case LIST:
			JScrollPane scrollPane = (JScrollPane)component;
			value = ((JTextArea) (JTextArea)scrollPane.getViewport().getView()).getText().split("\n");
		}
		
		Conf.getProps().setProperty(id, value);
	}
}
