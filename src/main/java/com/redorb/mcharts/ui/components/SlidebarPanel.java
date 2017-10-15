package com.redorb.mcharts.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class SlidebarPanel<T> extends JPanel {

	/*
	 * Attributes
	 */

	public static final int MAX_FONT_SIZE = 24 - 8;
	public static final int MIN_FONT_SIZE = 12;

	private final Logger log = LoggerFactory.getLogger(SlidebarPanel.class);

	private int elementsToShow;
	private final List<JLabel> labels = new ArrayList<>();

	private List<T> objects;

	private int currentElement = 0;

	private final List<ActionListener> listeners = new ArrayList<>();

	/*
	 * Ctors
	 */

	public SlidebarPanel(int pelementsToShow) {

		if (pelementsToShow % 2 ==.0) { pelementsToShow++; }

		this.elementsToShow = pelementsToShow;
		currentElement = (elementsToShow - 1) / 2;

		initComponents();
		initLayout();
	}

	public SlidebarPanel(int pelementsToShow, List<T> objects) {

		this(pelementsToShow);

		this.objects = objects;		
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

			log.debug("distance " + distance + ", factor " + factor + ", size" + size);

			JLabel label = new JLabel();
			label.setFont(font.deriveFont(size));
			label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

			// actions


			if (i != middleElement) {

				label.addMouseListener(new SlidebarActionListener<T>(this, i - middleElement));

				if (i == middleElement - 1 || i == middleElement + 1) {
					label.setForeground(Color.GRAY);
				}
				else {
					label.setForeground(Color.LIGHT_GRAY);
				}			
			}						

			labels.add(label);
		}
	}

	private void initLayout() {

		setLayout(new GridLayout(1, elementsToShow));

		for (int i = 0; i < elementsToShow; i++) {
			add(labels.get(i));
		}
	}

	public void move(int inc) {

		currentElement += inc;

		if (currentElement < 0) {
			currentElement = 0;
		}
		else if (currentElement > objects.size() - 1) {
			currentElement = objects.size() - 1;
		}

		render();

		for (ActionListener listener : listeners) {
			listener.actionPerformed(new ActionEvent(this, 0, inc > 0 ? "forward" : "backward"));
		}
	}

	public void setSelectedIndex(int index) {

		if (index < objects.size()) {
			currentElement = index;
			render();
		}
	}

	public void setSelectedElement(T element) {
		currentElement = objects.indexOf(element);
		render();
	}

	private void render() {

		int firstObject = currentElement - ((elementsToShow - 1) / 2);

		for (int i = 0; i < elementsToShow; i++)  {

			String text = (0 <= firstObject + i && firstObject + i < objects.size()) ? objects.get(firstObject + i).toString() : "";

			labels.get(i).setText(text);
		}
	}
}
