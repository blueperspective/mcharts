package com.redorb.mcharts.io.grisbi;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * GSB Reader.
 */
public class GsbReader {

	/*
	 * Ctor
	 */

	public GsbReader() {}

	/*
	 * Operations
	 */

	public void read(String filepath) throws SAXException, IOException {

		// Obtain an instance of an XMLReader implementation 
		// from a system property
		XMLReader 
		parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();

		// Create a new instance and register it with the parser
		ContentHandler contentHandler = new GsbContentHandler();
		parser.setContentHandler(contentHandler);

		parser.parse(filepath);
	}
}
