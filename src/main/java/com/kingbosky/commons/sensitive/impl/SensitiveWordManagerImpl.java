package com.kingbosky.commons.sensitive.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingbosky.commons.sensitive.SensitiveWordManager;
import com.kingbosky.commons.sensitive.SensitiveWordSource;

/**
 * 敏感词过滤 DFA有穷状态机算法的实现
 * @author tianqiongbo
 *
 */
public class SensitiveWordManagerImpl implements SensitiveWordManager{
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
		
		int ch = charConvert(word.charAt(index));
		NumberTree tree = map.get(ch);
		
		if (tree == null) {
			tree = new NumberTree();
			map.put(ch, tree);
		}
		
		if (index == word.length() - 1) tree.finishFlag = true;
		
		createWordTree(word, index + 1, tree.map);
	}
	
	private static class NumberTree {
		Map<Integer, NumberTree> map = new HashMap<Integer, NumberTree>();;
		boolean finishFlag = false;
	}
	
	public boolean hasSensitiveWord(String str){
		int length = str.length();
		for (int i = 0; i < length; i++) {
			int last = getFinishFlagIndex(str, i, length, forbiddenMap);
			if (last > -1) return true;
		}
		return false;
	}
	
	/**
	 * 过滤并替换敏感词
	 * @param str
	 * @param c 敏感词被替换成的字符
	 * @return
	 */
	public String filterSensitiveWord(String str, char c){
		int length = str.length();
		StringBuilder newStr = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int finishIndex = getFinishFlagIndex(str, i, length, forbiddenMap);
			if (finishIndex > -1) {
				for(int startIndex = i; startIndex <= finishIndex; startIndex++){
					newStr.append(c);
				}
				i = finishIndex;
			}else{
				newStr.append(str.charAt(i));
			}
		}
		return newStr.toString();
	}
	
	private int getFinishFlagIndex(String str, int index, int length, Map<Integer, NumberTree> map) {
		if (index >= length) return -1;
		int ch = charConvert(str.charAt(index));
		if (!map.containsKey(ch)) return -1;
		NumberTree tree = map.get(ch);
		if (tree.finishFlag) return index; 
		return getFinishFlagIndex(str, index + 1, length, tree.map);
	}
	
	/**
	 * 支持过滤大小写字符
	 * @param c
	 * @return
	 */
	private static int charConvert(char c) {  
        return (c >= 'A' && c <= 'Z') ? c + 32 : c;  
    }  
	
	public static void main(String[] args) {
		List<String> lstWord = new ArrayList<String>();
		lstWord.add("法轮功");
		lstWord.add("fuck");
		lstWord.add("江泽民");
		SensitiveWordManagerImpl manager = new SensitiveWordManagerImpl();
		for (String word : lstWord) {
			manager.createWordTree(word.trim(), 0, forbiddenMap);
		}
		System.out.println(manager.hasSensitiveWord("llasdFuckyoux"));
		System.out.println(manager.filterSensitiveWord("法轮功ll法轮功asdFFFuckyou江泽民x",'*'));
	}
}
