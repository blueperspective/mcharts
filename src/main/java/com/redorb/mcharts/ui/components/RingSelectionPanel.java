package com.redorb.mcharts.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class RingSelectionPanel<T> extends JPanel implements MouseWheelListener {

	/*
	 * Attributes
	 */

	public static final float MAX_FONT_SIZE = 16.0f;
	public static final float MIN_FONT_SIZE = 12.0f;

	public static final String CMD_BACKWARD = "backward";
	public static final String CMD_BACKWARD_LOOP = "backwardLoop";
	public static final String CMD_FORWARD = "forward";
	public static final String CMD_FORWARD_LOOP = "forwardLoop";

	private int elementsToShow = 5;

	private final List<JLabel> labels = new ArrayList<>();

	private List<T> objects;

	private int currentElement = 0;

	private final List<ActionListener> listeners = new ArrayList<>();

	private boolean showBoundaries = true;

	private boolean infiniteRing = true;

	private final Log logger = LogFactory.getLog(RingSelectionPanel.class);

	/*
	 * Ctors
	 */

	public RingSelectionPanel(int pelementsToShow) {
		this(pelementsToShow, true);		
	}

	public RingSelectionPanel(int pelementsToShow, boolean infiniteRing) {

		if (pelementsToShow % 2 == 0) { pelementsToShow++; }

		this.elementsToShow = pelementsToShow;
		this.infiniteRing = infiniteRing;

		initComponents();
		initLayout();
	}

	public RingSelectionPanel(int pelementsToShow, List<T> objects) {
		this(pelementsToShow, objects, true);
	}

	public RingSelectionPanel(int pelementsToShow, List<T> objects, boolean infiniteRing) {

		this(pelementsToShow, infiniteRing);
		this.objects = objects;

		currentElement = (elementsToShow - 1) / 2;
		render();
	}

	/*
	 * Operations
	 */

	public void setObjets(List<T> objects) {
		this.objects = objects;
		render();
	}

	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	public T getSelectedElement() {
		return objects.get(currentElement);
	}

	public int getSelectedIndex() {
		return currentElement;
	}

	private void initComponents() {

		int middleElement = (elementsToShow - 1) / 2;

		Font font = getFont();

		for (int i = 0; i < elementsToShow; i++) {

			float distance = Math.abs(i - middleElement);
			float factor = (distance == 0) ? 1 : (1 - (distance / middleElement));
			float size = (MIN_FONT_SIZE + (factor * MAX_FONT_SIZE));

			System.out.println("element " + i + " d " + distance + " f " + factor + " s " + size);

			JLabel label = new JLabel();
			label.setFont(font.deriveFont(size));
			label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

			// actions

			if (i != middleElement) {

				label.addMouseListener(new RingActionListener<T>(this, i - middleElement));

				if (i == middleElement - 1 || i == middleElement + 1) {
					label.setForeground(Color.GRAY);
				}
				else {
					label.setForeground(Color.LIGHT_GRAY);
				}			
			}						

			labels.add(label);
		}

		if (!showBoundaries) {
			labels.get(0).setText("...");			
			labels.get(0).setForeground(Color.GRAY);
			labels.get(0).setFont(font.deriveFont(MIN_FONT_SIZE + MAX_FONT_SIZE));

			labels.get(elementsToShow - 1).setText("...");			
			labels.get(elementsToShow - 1).setForeground(Color.GRAY);
			labels.get(elementsToShow - 1).setFont(font.deriveFont(MIN_FONT_SIZE + MAX_FONT_SIZE));
		}

		addMouseWheelListener(this);
	}

	private void initLayout() {

		setLayout(new GridLayout(1, elementsToShow));

		for (int i = 0; i < elementsToShow; i++) {
			add(labels.get(i));
		}
	}

	public void move(int inc) {

		if (!infiniteRing && (currentElement + inc < 0 || currentElement + inc >= objects.size())) {
			return;
		}

		currentElement += inc;

		String command = inc > 0 ? CMD_FORWARD : CMD_BACKWARD;

		if (currentElement < 0) {
			currentElement = objects.size() - 1;
			command = CMD_BACKWARD_LOOP;
		}
		else if (currentElement > objects.size() - 1) {
			currentElement = 0;
			command = CMD_FORWARD_LOOP;
		}

		render();

		for (ActionListener listener : listeners) {
			listener.actionPerformed(new ActionEvent(this, 0, command));
		}
	}

	public void setSelectedIndex(int index) {
		currentElement = index;
		render();
	}

	public void setSelectedElement(T element) {
		currentElement = objects.indexOf(element);
		render();
	}

	/**
	 * @return the showBoundaries
	 */
	public boolean isShowBoundaries() {
		return showBoundaries;
	}

	/**
	 * @param showBoundaries the showBoundaries to set
	 */
	public void setShowBoundaries(boolean showBoundaries) {
		this.showBoundaries = showBoundaries;
	}

	private void render() {

		int firstObject = currentElement - ((elementsToShow - 1) / 2);

		String text = "";

		int start = showBoundaries ? 0 : 1;
		int end = showBoundaries ? elementsToShow : elementsToShow - 1;

		System.out.println("render: current " + currentElement);

		for (int i = start; i < end; i++)  {

			try {
				int val = firstObject + i;

				if (!infiniteRing && (val < 0 || val >= objects.size())) {
					text = "";
				}
				else if (val < 0) {
					val = objects.size() + val;
				}
				else if (val >= objects.size()){
					val = val - objects.size();
				}

				if (0 <= val && val < objects.size()) {				
					text = objects.get(val).toString();
				}

				System.out.println("render " + i + ", v " + val + " : " + text);

			} catch (Exception e) {
				LoggerFactory.getLogger(RingSelectionPanel.class).error(e.getMessage(), e);
			}

			labels.get(i).setText(text);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		int notches = e.getWheelRotation();
		if (notches < 0) {
			move(-1);
		} else {
			move(1);
		}
	}
}
