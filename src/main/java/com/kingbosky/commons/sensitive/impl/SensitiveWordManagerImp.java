/**
 * $Id: ForbiddenService.java 774 2013-04-08 03:44:02Z qiongbo.tian $
 * Copyright(C) 2011-2016 dreamingame.com All rights reserved.
 */
package com.kingbosky.commons.sensitive.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingbosky.commons.sensitive.SensitiveWordManager;
import com.kingbosky.commons.sensitive.SensitiveWordSource;

public class SensitiveWordManagerImp implements SensitiveWordManager{
	private static Map<Integer, NumberTree> forbiddenMap = new HashMap<Integer, NumberTree>();
	
	private SensitiveWordSource sensitiveSource;
	
	public SensitiveWordSource getSensitiveSource() {
		return sensitiveSource;
	}

	public void setSensitiveSource(SensitiveWordSource sensitiveSource) {
		this.sensitiveSource = sensitiveSource;
	}

	public void run() {
		Map<Integer, NumberTree> map = new HashMap<Integer, NumberTree>();
		List<String> lstWord = sensitiveSource.getAll();
		for (String word : lstWord) {
			createWordTree(word.trim(), 0, map);
		}
		forbiddenMap = Collections.unmodifiableMap(map);
	}
	
	private void createWordTree(String word, int index, Map<Integer, NumberTree> map) {
		if (index >= word.length()) return;
		
		int ch = word.charAt(index);
		NumberTree tree = map.get(ch);
		
		if (tree == null) {
			tree = new NumberTree();
			map.put(ch, tree);
		}
		
		if (index == word.length() - 1) tree.finishFlg = true;
		
		createWordTree(word, index + 1, tree.map);
	}
	
	private class NumberTree {
		Map<Integer, NumberTree> map = new HashMap<Integer, NumberTree>();;
		boolean finishFlg = false;
	}
	
	public boolean hasSensitive(String word){
		int length = word.length();
		for (int i = 0; i < length; i++) {
			if (hasSensitive(word, i, length, forbiddenMap)) return true;
		}
		return false;
	}
	
	private boolean hasSensitive(String word, int index, int length, Map<Integer, NumberTree> map) {
		if (index >= length) return false;
		int ch = word.charAt(index);
		if (!map.containsKey(ch)) return false;
		NumberTree tree = map.get(ch);
		if (tree.finishFlg) return true; 
		return hasSensitive(word, index + 1, length, tree.map);
	}
}
