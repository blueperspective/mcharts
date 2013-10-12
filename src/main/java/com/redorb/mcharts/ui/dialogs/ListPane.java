package com.redorb.mcharts.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.redorb.commons.ui.Utils;

/**
 * JPanel containing a scrollable list, and add, remove, up and down buttons
 */
@SuppressWarnings("serial")
public class ListPane<T> extends JPanel {

	/*
	 * Attributes
	 */

	public enum Orientation {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT
	};

	private JList<T> lstList;
	private JScrollPane scrollList;

	private JButton butAdd;
	private JButton butRemove;
	private JButton butUp;
	private JButton butDown;

	private List<ActionListener> listeners = new ArrayList<>();

	private DefaultListModel<T> model = new DefaultListModel<>();

	private Orientation orientation = Orientation.RIGHT;

	/*
	 * Ctors
	 */

	public ListPane(Orientation orientation) {

		this.orientation = orientation;
		initComponents();
		initLayout();
	}

	public ListPane(Orientation orientation, List<T> list) {

		this(orientation);

		for (T o : list) {
			model.addElement(o);
		}
	}

	/*
	 * Operations
	 */

	/**
	 * Initializes components.
	 */
	private void initComponents() {

		lstList = new JList<T>();
		scrollList = new JScrollPane();

		butAdd = Utils.createButtonNoBorder(
				"/images/22x22/add.png", "", "", 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						add();
					}
				});

		butRemove = Utils.createButtonNoBorder(
				"/images/22x22/remove.png", "", "", 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						int index = lstList.getSelectedIndex();
						remove(index);
					}
				});

		butUp = Utils.createButtonNoBorder(
				"/images/22x22/up.png", "", "",
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						int index = lstList.getSelectedIndex();
						up(index);
					}
				});

		butDown = Utils.createButtonNoBorder(
				"/images/22x22/down.png", "", "", 
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						int index = lstList.getSelectedIndex();
						down(index);
					}
				});

		scrollList.setViewportView(lstList);

		lstList.setModel(model);
	}

	/**
	 * Initializes the layout.
	 */
	private void initLayout() {

		// layout

		setLayout(new GridBagLayout());

		GridBagConstraints constraints = null;

		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.insets = new Insets(4, 4, 4, 4);

		switch (orientation) {
		case TOP:
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.gridx = 0;
			constraints.gridy = 1;
			break;
		case BOTTOM:
			constraints.gridwidth = GridBagConstraints.REMAINDER;
			constraints.gridx = 0;
			constraints.gridy = 0;
			break;
		case RIGHT:
			constraints.gridheight = GridBagConstraints.REMAINDER;
			constraints.gridx = 0;
			constraints.gridy = 0;
			break;
		case LEFT:
			constraints.gridheight = GridBagConstraints.REMAINDER;
			constraints.gridx = 1;
			constraints.gridy = 0;
			break;				
		}

		add(scrollList, constraints);

		JButton[] buttons = new JButton[4];
		buttons[0] = butAdd;
		buttons[1] = butRemove;
		buttons[2] = butUp;
		buttons[3] = butDown;

		for (int i = 0; i < buttons.length; i++) {

			constraints = new GridBagConstraints();

			switch (orientation) {
			case TOP:
				constraints.gridx = i;
				constraints.gridy = 0;
				constraints.insets = new Insets(4, 0, 4, 0);
				break;
			case BOTTOM:
				constraints.gridx = i;
				constraints.gridy = 1;
				constraints.insets = new Insets(4, 0, 4, 0);
				break;
			case RIGHT:
				constraints.gridx = 1;
				constraints.gridy = i;
				constraints.insets = new Insets(0, 4, 0, 4);
				break;
			case LEFT:
				constraints.gridx = 0;
				constraints.gridy = i;
				constraints.insets = new Insets(0, 4, 0, 4);
				break;				
			}

			add(buttons[i], constraints);
		}

		// spacer

		constraints = new GridBagConstraints();
		constraints.insets = new Insets(0, 0, 0, 0);

		switch (orientation) {
		case TOP:
		case BOTTOM:
			constraints.fill = GridBagConstraints.VERTICAL;
			constraints.weightx = 1.0;
			break;
		case RIGHT:
		case LEFT:
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weighty = 1.0;			
			break;				
		}

		switch (orientation) {
		case TOP:
			constraints.gridx = 4;
			constraints.gridy = 0;
			break;
		case BOTTOM:
			constraints.gridx = 4;
			constraints.gridy = 1;
			break;
		case RIGHT:
			constraints.gridx = 1;
			constraints.gridy = 4;
			break;
		case LEFT:
			constraints.gridx = 0;
			constraints.gridy = 4;
			break;				
		}

		add(Box.createGlue(), constraints);
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	/**
	 * Alert all listeners to add a new element.
	 */
	public void add() {

		for (ActionListener list : listeners) {
			list.actionPerformed(new ActionEvent(model, 0, ""));
		}
	}

	/**
	 * Removes an element
	 * @param index
	 */
	public void remove(int index) {

		if (0 <= index && index < model.getSize()) {
			model.remove(index);
		}
	}

	/**
	 * Moves up an element
	 * @param index
	 */
	public void up(int index) {

		if (0 < index && index < model.getSize()) {
			T o = model.get(index);
			model.remove(index);
			model.insertElementAt(o, index - 1);
			lstList.setSelectedIndex(index - 1);
		}
	}

	/**
	 * Moves down an element.
	 * @param index
	 */
	public void down(int index) {

		if (0 <= index && index < (model.getSize() - 1)) {
			T o = model.get(index);
			model.remove(index);
			model.insertElementAt(o, index + 1);
			lstList.setSelectedIndex(index + 1);
		}
	}

	/*
	 * Getters/Setters
	 */

	/**
	 * @return the model
	 */
	public DefaultListModel<T> getModel() {
		return model;
	}

	public void setRenderer(ListCellRenderer<T> cellRenderer) {
		lstList.setCellRenderer(cellRenderer);
	}
}
