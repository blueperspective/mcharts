package com.redorb.mcharts.io.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.io.IWriter;
import com.redorb.mcharts.ui.Conf;

/**
 * Writes a list of string to a text file, one item per line.
 */
public class ListWriter implements IWriter {

	/*
	 * Attributes
	 */
	
	/** log. */
	private final Logger log = LoggerFactory.getLogger(ListWriter.class);

	/** The file to write. */
	private File file = null;

	/** List to write. */
	private List<String> list = null;

	/*
	 * Ctor 
	 */

	public ListWriter(String filepath, List<String> list) {
		this.file = new File(filepath);
		this.list = list;
	}
	
	/*
	 * Operations
	 */

	@Override
	public void write() {

		Writer output = null;
		
		try {
			
			output = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < list.size(); i++) {
				output.write(list.get(i) + Conf.NEWLINE);
			}
		}
		catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		finally {
			try {
				if (output != null) { output.close(); }
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
