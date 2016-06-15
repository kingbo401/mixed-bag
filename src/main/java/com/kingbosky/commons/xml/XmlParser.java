/**
 * $Id$
 * Copyright(C) 2011-2016 dreamingame.com All rights reserved.
 */
package com.kingbosky.commons.xml;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 
 * @author <a href="mailto:shiyang.zhao@dreamingame.com">Rex Zhao</a>
 * @version 1.0 Dec 23, 2011 2:07:16 PM
 */
public interface XmlParser {

	public Document document();
	
    /**
     * 取得节点的指定属性值
     * @param node 节点
     * @param attrName 属性名称
     * @return 属性值
     */
    public String attrValue(Node node, String attrName);

    /**
     * 取得指定节点的子节点值
     * @param node 节点
     * @param childPath 子节点相对路径
     * @return 节点值
     */
    public String nodeValue(Node node, String childPath);
    
    public String nodeValue(Node node);

    /**
     * 取得相对根接点路径下的第一个符合条件的节点列表
     * @param node 根节点对象
     * @param childPath 单个节点对于根节点的路径
     * @return 取得的节点对象
     */
    public Node selectNode(Node node, String childPath);

    /**
     * 取得相对根接点路径下的所有符合条件的节点列表
     * @param node 根节点
     * @param childPath 相对路径
     * @return 取得的节点对象列表
     */
    public List<Node> selectNodes(Node node, String childPath);	
	
    /**
     * 取得根节点元素对象
     * @return 根节点元素对象
     */
    public Element rootElement();

    /**
     * 取得指定xpath路径下的节点对象
     * @param xpath 节点xpath路径
     * @return 符合路径条件的第一个节点对象
     */
    public Node selectNode(String xpath);

    /**
     * 取得指定xpath路径下的全部节点对象
     * @param xpath 节点xpath路径
     * @return 符合路径条件的所有节点对象
     */
    public List<Node> selectNodes(String xpath);

    /**
     * 取得指定xpath路径节点下的指定名称属性值
     * @param xpath 节点xpath路径
     * @param attrName 属性名
     * @return
     * <li>值存在时，返回属性值</li>
     * <li>不存在节点或该属性时，返回null</li>
     */
    public String attrValue(String xpath, String attrName);

    /**
     * 取得指定xpath路径下的节点值
     * @param xpath 节点xpath路径
     * @return
     * <li>指定路径节点存在时，返回节点值</li>
     * <li>指定路径节点不存在时，返回null</li>
     */
    public String nodeValue(String xpath);
    
    /**
     * 取得指定xpath路径下的元素数量
     * @param xpath 路径
     * @return 指定路径下的元素数量
     */
    public int nodeCount(String xpath);
}
