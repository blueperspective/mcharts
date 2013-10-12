package com.redorb.mcharts.ui.misc;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Extension based filter for Open File dialog.
 */
public class ExtensionFilter extends FileFilter {

	/*
	 * Attributes
	 */
	
	/** filter description (ex : "Grisbi files (*.gsb)") */
	private String description;
	
	/** extension (ex : "gsb") */
	private String extension;
	
	/*
	 * Ctor
	 */
	
	public ExtensionFilter(String description, String extension) {
		this.description = description;
		this.extension = extension;
	}
	
	/*
	 * Operation
	 */
	
	@Override
	public boolean accept(File file) {
		
		if(file.isDirectory()) { 
			return true; 
		}
		
		String filename = file.getName().toLowerCase(); 

		return filename.endsWith(extension);
	}
	
	/*
	 * Getters/Setters
	 */
	
	public String getDescription(){
		return description;
	}
}
