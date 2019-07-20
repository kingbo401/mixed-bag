package com.kingbo401.commons.sensitive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kingbo401.commons.util.FullHalfConverter;

/**
 * 敏感词过滤 DFA有穷状态机算法的实现 trie树
 *
 * @author kingbo401
 * @date 2019/07/20
 */
public class SensitiveWordManager {
    private static Map<Integer, NumberTree> forbiddenMap = new HashMap<Integer, NumberTree>();
    private static String stopWordStr = " \\/*-_+,`~!@#$%^&()=!<>{}[]！《》【】、，.。（）";
    private static Set<Integer> stopWords = new HashSet<Integer>();
    static{
        for(int i = 0; i < stopWordStr.length(); i++){
            stopWords.add((int)stopWordStr.charAt(i));
        }
    }
    
    private SensitiveWordSource sensitiveSource;
    
    public void setSensitiveSource(SensitiveWordSource sensitiveSource) {
        this.sensitiveSource = sensitiveSource;
    }

    public void init() {
        Map<Integer, NumberTree> map = new HashMap<Integer, NumberTree>();
        List<String> lstWord = sensitiveSource.initSensitiveWords();
        for (String word : lstWord) {
            createWordTree(word.trim(), 0, map);
        }
        forbiddenMap = Collections.unmodifiableMap(map);
    }
    
    private void createWordTree(String word, int index, Map<Integer, NumberTree> map) {
        if (index >= word.length()) {
            return;
        }
        
        int ch = charConvert(word.charAt(index));
        NumberTree tree = map.get(ch);
        
        if (tree == null) {
            tree = new NumberTree();
            map.put(ch, tree);
        }
        
        if (index == word.length() - 1){
            tree.finishFlag = true;
        }
        
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
            if (last > -1){
                return true;
            }
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
        if (!map.containsKey(ch)){
            if(map.equals(forbiddenMap)){//第一层递归，直接返回-1
                return -1;
            }
            if(stopWords.contains(ch)){
                return getFinishFlagIndex(str, index + 1, length, map);
            }else{
                return -1;
            }
        }
        NumberTree tree = map.get(ch);
        if (tree.finishFlag) {
            return index; 
        }
        return getFinishFlagIndex(str, index + 1, length, tree.map);
    }
    
    /**
     * 支持过滤大小写字符
     * @param c
     * @return
     */
    private static int charConvert(char c) { 
        c = FullHalfConverter.full2half(c + "").charAt(0);
        return (c >= 'A' && c <= 'Z') ? c + 32 : c;  
    }  
    
    public static void main(String[] args) {
        SensitiveWordSource source = new SensitiveWordSource() {
            @Override
            public List<String> initSensitiveWords() {
                List<String> lstWord = new ArrayList<String>();
                lstWord.add("法轮功");
                lstWord.add("fuck");
                lstWord.add("江泽民");
                return lstWord;
            }
        };
        
        SensitiveWordManager manager = new SensitiveWordManager();
        manager.setSensitiveSource(source);
        manager.init();
        System.out.println(manager.hasSensitiveWord("llasdFuckyoux"));
        System.out.println(manager.filterSensitiveWord("你丫的【*F#ｕｃｋ】you##哈哈哈【法-轮 -功】",'*'));
    }
}
