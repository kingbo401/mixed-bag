/**
 * $Id$
 * Copyright(C) 2011-2016 dreamingame.com All rights reserved.
 */
package com.kingbosky.commons.xml;

import java.io.File;

import org.dom4j.Element;

/**
 * 
 * @author <a href="mailto:shiyang.zhao@dreamingame.com">Rex Zhao</a>
 * @version 1.0 Dec 23, 2011 2:08:26 PM
 */
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
