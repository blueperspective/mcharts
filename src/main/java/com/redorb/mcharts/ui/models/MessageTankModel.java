package com.redorb.mcharts.ui.models;

import javax.swing.AbstractListModel;

import com.redorb.mcharts.ui.MessageTank;

@SuppressWarnings("serial")
public class MessageTankModel extends AbstractListModel {

	/*
	 * Attributes
	 */
	
	private MessageTank messageTank = null;

	/*
	 * Ctors
	 */
	
	public MessageTankModel(MessageTank messageTank) {
		this.messageTank = messageTank;
	}

	/*
	 * Getters/Setters
	 */
	
	@Override
	public Object getElementAt(int index) {
		return messageTank.getMessage(index);
	}

	@Override
	public int getSize() {
		
		return messageTank.size();
	}
	
}
