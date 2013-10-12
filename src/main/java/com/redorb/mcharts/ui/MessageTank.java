package com.redorb.mcharts.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * List of alert messages.
 */
public class MessageTank {
	
	private enum MessageTankLevel {
		LEVEL_INFO,
		LEVEL_WARNING,
		LEVEL_FATAL
	}
	
	/*
	 * Attributes
	 */
	
	/** Message tank unique instance. */
	private static MessageTank singleton = null;
	
	/** Get the MessageTank unique instance. */
	public static MessageTank getInstance() {
		
		if (singleton == null) {
			singleton = new MessageTank();
		}
		
		return singleton;
	}
	
	/**  Message level. */
	public List<MessageTankLevel> levels = new ArrayList<MessageTankLevel>();
	
	/** Text message */
	public List<String> messages = new ArrayList<String>();

	/*
	 * Ctors
	 */
	
	private MessageTank() {}
	
	/*
	 * Operations
	 */
	
	public void clear() {
		messages.clear();
	}
	
	public void info(String message) {
		
		levels.add(MessageTankLevel.LEVEL_INFO);
		messages.add(message);
	}
	
	public void warning(String message) {
		
		levels.add(MessageTankLevel.LEVEL_WARNING);
		messages.add(message);
	}
	
	public void error(String message) {
		
		levels.add(MessageTankLevel.LEVEL_FATAL);
		messages.add(message);
	}

	/*
	 * Getters/Setters
	 */
	
	public String getMessage(int index) {
		
		String res = "";
		
		if (index >= 0 && index < messages.size()) {
			res = messages.get(index);
		}
		
		return res;
	}
	
	public int size() {
		return messages.size();
	}
	
	public boolean isEmpty() {
		return messages.isEmpty();
	}
}
