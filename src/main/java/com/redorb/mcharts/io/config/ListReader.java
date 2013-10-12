package com.redorb.mcharts.io.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redorb.mcharts.io.IReader;

/**
 * Reads a txt file into a list<String>.
 */
public class ListReader implements IReader {

	/*
	 * Attributes 
	 */
	
	/** log. */
	private final Logger log = LoggerFactory.getLogger(ListReader.class);
	
	/** File to read. */
	private Reader reader = null;
	
	/** List<String> created from the lines of the file. */
	private final List<String> list = new ArrayList<String>();
	
	/*
	 * Ctor
	 */
	
	/**
	 * Create a new list reader.
	 * @param filepath the absolute file path of the file to read
	 */
	public ListReader(Reader reader) {
		this.reader = reader;
	}
	
	@Override
	public void read() {
		
		BufferedReader bufReader = null;
		
		try {
			bufReader = new BufferedReader(reader);
			
			String line = null;
			
			while ((line = bufReader.readLine()) != null) {
				list.add(line);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) { reader.close(); }
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @return the list
	 */
	public List<String> getList() {
		return list;
	}
}
