package com.redorb.mcharts.io.grisbi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class GsbContentHandler implements ContentHandler {

	/*
	 * Attributes
	 */

	private final Logger log = LoggerFactory.getLogger(GsbContentHandler.class);

	// generalities for 0.5
	private static final String XML_ELT_GENERALITES = "Generalites";

	private boolean inGeneralites = false;

	// file version for 0.5
	private static final String XML_ELT_VERSION_FICHIER = "Version_fichier";

	// generalities for 0.6
	private static final String XML_ELT_GENERALITIES = "General";

	// file version for 0.6
	private static final String XML_ATT_FILE_VERSION = "File_version";

	private static final String XML_VAL_VERSION_0_6_0 = "0.6.0";

	private static final String XML_VAL_VERSION_0_5_0 = "0.5.0";

	private ContentHandler contentHandler = null;
	
	private StringBuffer lastContent = null;

	/*
	 * Ctor
	 */

	public GsbContentHandler() {}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		
		if (contentHandler != null) {
			contentHandler.startElement(uri, localName, name, atts);
		}
		else if (XML_ELT_GENERALITES.equals(localName)) {
			inGeneralites = true;
		}
		else if (XML_ELT_GENERALITIES.equals(localName)) {
			String version = atts.getValue(XML_ATT_FILE_VERSION);
			if (version != null && XML_VAL_VERSION_0_6_0.equals(version)) {
				log.debug("GSB version 0.6.0");
				contentHandler = new GsbContentHandler6();
			}
			else {
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException {
		
		if (contentHandler != null) {
			contentHandler.characters(ch, start, length);
		}
		else if (inGeneralites) {
			lastContent = new StringBuffer();
			lastContent.ensureCapacity(length);
			
			for (int i = 0 ; i < length; i++) {
				lastContent.append(ch[start + i]);
			}
		}
	}

	@Override
	public void endDocument() throws SAXException {
		
		if (contentHandler != null) {
			contentHandler.endDocument();
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
	throws SAXException {
		
		if (contentHandler != null) {
			contentHandler.endElement(uri, localName, name);
		}
		else if (XML_ELT_GENERALITES.equals(localName)) {
			inGeneralites = false;
		}
		else if (XML_ELT_VERSION_FICHIER.equals(localName)) {
			if (lastContent != null && XML_VAL_VERSION_0_5_0.equals(lastContent.toString())) {
				throw new RuntimeException("Version not supported");
			}
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		
		if (contentHandler != null) {
			contentHandler.endPrefixMapping(prefix);
		}
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
	throws SAXException {

		if (contentHandler != null) {
			contentHandler.ignorableWhitespace(ch, start, length);
		}
	}

	@Override
	public void processingInstruction(String target, String data)
	throws SAXException {

		if (contentHandler != null) {
			contentHandler.processingInstruction(target, data);
		}
	}

	@Override
	public void setDocumentLocator(Locator locator) {

		if (contentHandler != null) {
			contentHandler.setDocumentLocator(locator);
		}
	}

	@Override
	public void skippedEntity(String name) throws SAXException {

		if (contentHandler != null) {
			contentHandler.skippedEntity(name);
		}
	}

	@Override
	public void startDocument() throws SAXException {

		if (contentHandler != null) {
			contentHandler.startDocument();
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
	throws SAXException {

		if (contentHandler != null) {
			contentHandler.startPrefixMapping(prefix, uri);
		}
	}
}
