package com.kingbo401.commons.xml;

import java.io.File;

import org.dom4j.Element;

public interface XmlCreator {

	public Element createRootElement(String tagName);
	
	public Element rootElement();
	
	public Element addElement(Element root, String tagName);
	
	public void addCDATA(Element root, String text);
	
	public Element addAttribute(Element root, String name, String value);
	
	public Element remove(Element element);
	
	public Element remove(String xpath);
	
	public void save2File(File file);
}
