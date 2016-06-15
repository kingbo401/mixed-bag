package com.kingbosky.commons.xml.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;

import com.kingbosky.commons.exception.GeneralException;
import com.kingbosky.commons.util.Constants;
import com.kingbosky.commons.util.IOUtil;
import com.kingbosky.commons.util.StringUtil;
import com.kingbosky.commons.xml.XmlCreator;
import com.kingbosky.commons.xml.XmlParser;

public class XmlCreatorBase implements XmlCreator {

	private final Document doc;
	private final XmlParser parser;
	
	public XmlCreatorBase() {
		this.doc = DocumentHelper.createDocument();
		this.parser = new XmlParserBase(doc);
	}
	
	public XmlCreatorBase(XmlParser parser) {
		this.doc = parser.document();
		this.parser = parser;
	}
	
	@Override
	public Element createRootElement(String tagName) {
		return doc.addElement(tagName);
	}
	
	@Override
	public Element rootElement() {
		return doc.getRootElement();
	}
	
	@Override
	public Element addElement(Element root, String tagName) {
		return root.addElement(tagName);
	}
	
	@Override
	public void addCDATA(Element root, String text) {
		root.add(new DefaultCDATA(text));
	}
	
	@Override
	public Element addAttribute(Element root, String name, String value) {
		return root.addAttribute(name, value);
	}
	
	@Override
	public Element remove(Element element) {
		Element parent = element.getParent();
		parent.remove(element);
		return parent;
	}
	
	@Override
	public Element remove(String xpath) {
		Element element = (Element)parser.selectNode(xpath);
		return (element == null) ? null : remove(element);
	}

	@Override
	public String toString() {
		return StringUtil.newString(getBytes(), Constants.DFT_CHARSET);
	}
	
	@Override
	public void save2File(File file) {
		IOUtil.writeFile(file, getBytes());
	}
	
	private byte[] getBytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLWriter writer = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Constants.DFT_CHARSET);
			
			writer = new XMLWriter(new OutputStreamWriter(baos, Constants.DFT_CHARSET), format);
			writer.write(doc);
		} catch (Exception e) {
			throw new GeneralException("create xml string failed.", e);
		} finally {
			try {
				if (writer != null) writer.close();
			} catch (IOException e) {
				throw new GeneralException("", e);
			}
		}
		return baos.toByteArray();
	}
}
