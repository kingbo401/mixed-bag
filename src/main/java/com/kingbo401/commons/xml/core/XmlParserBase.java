package com.kingbo401.commons.xml.core;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.kingbo401.commons.util.StringTool;
import com.kingbo401.commons.xml.XmlParser;

public class XmlParserBase implements XmlParser {

	protected final Document doc;
	
	public XmlParserBase(Document doc) {
		this.doc = doc;
	}
	
	@Override
	public Document document() {
		return doc;
	}

	@Override
    public String attrValue(Node node, String attrName) {
        Node attrNode = node.selectSingleNode("./@" + attrName);
        return (attrNode == null) ? null : attrNode.getText();
    }

    @Override
    public String nodeValue(Node node, String childPath) {
        Node childNode = (childPath == null) ? node : node.selectSingleNode("./" + childPath);
        return (childNode == null) ? null : StringTool.getString(childNode.getText());
    }
    
    @Override
    public String nodeValue(Node node) {
    	return StringTool.getString(node.getText());
    }

    @Override
    public Node selectNode(Node node, String childPath) {
        return node.selectSingleNode("./" + childPath);
    }

    @Override
    @SuppressWarnings("unchecked") 
    public List<Node> selectNodes(Node node, String childPath) {
        return node.selectNodes("./" + childPath);
    }
	
	@Override
    public Element rootElement() {
        return doc.getRootElement();
    }

    @Override
    public Node selectNode(String xpath) {
        return doc.selectSingleNode(xpath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Node> selectNodes(String xpath) {
        return doc.selectNodes(xpath);
    }

    @Override
    public String attrValue(String xpath, String attrName) {
        Attribute attribute = (Attribute)doc.selectSingleNode(xpath + "/@" + attrName);
        return (attribute == null) ? null : StringTool.getString(attribute.getValue());
    }

    @Override
    public String nodeValue(String xpath){
        Node node = doc.selectSingleNode(xpath);
        return (node == null) ? null : StringTool.getString(node.getText());
    }

    @Override
    @SuppressWarnings("unchecked") 
    public int nodeCount(String xpath) {
        List<Node> lstNode = doc.selectNodes(xpath);
        return (lstNode == null) ? 0 : lstNode.size();
    }
}
