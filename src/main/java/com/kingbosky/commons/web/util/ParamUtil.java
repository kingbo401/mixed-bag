package com.kingbosky.commons.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.kingbosky.commons.util.CollectionUtil;

public class ParamUtil {

	public static String urlEncode(String s, String charset) {
		if(s == null || "".equals(s)) return "";
        try {
            return URLEncoder.encode(s, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

	public static String urlDecode(String s, String charset) {
		if(s == null || "".equals(s)) return "";
        try {
            return URLDecoder.decode(s, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
	public static Map<String, String> getHttpParams(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Map<String, String[]> paraMap = request.getParameterMap();
		Map<String, String> retMap = new HashMap<String, String>();
		for (String key : paraMap.keySet()) {
			String[] values = paraMap.get(key);
			String valueStr = "";
			if(values != null && values.length > 0 && values[0] != null){
				valueStr = values[0];
			}
			retMap.put(key, valueStr);
		}
		return retMap;
	}
	
    public static <T> String getKeyAndValueStr(Map<String, T> params){
		if(CollectionUtil.isEmpty(params)) return "";
		StringBuilder sb = new StringBuilder();
        Map<String, String> paramsMap = new TreeMap<String, String>();
		for (Map.Entry<String, T> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if(value != null){
				paramsMap.put(key, String.valueOf(value));
			}else {
				paramsMap.put(key, "");
			}
        }
		for (Map.Entry<String, String> entry : paramsMap.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
    
    public static <T> String joinValue(Map<String, T> params){
    	if(CollectionUtil.isEmpty(params)) return "";
    	StringBuilder sb = new StringBuilder();
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
		for (Map.Entry<String, T> entry : params.entrySet()) {
			String key = entry.getKey();
			T value = entry.getValue();
			if(value != null){
				paramsMap.put(key, value.toString());
			}else {
				paramsMap.put(key, "");
			}
        }
		for (Map.Entry<String, String> entry : paramsMap.entrySet()){
            sb.append(entry.getValue());
		}
		return sb.toString();
    }
    
    public static <T> String joinValue(Map<String, T> params, char split){
    	if(CollectionUtil.isEmpty(params)) return "";
		StringBuilder sb = new StringBuilder();
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
		for (Map.Entry<String, T> entry : params.entrySet()) {
			String key = entry.getKey();
			T value = entry.getValue();
			if(value != null){
				paramsMap.put(key, value.toString());
			}else {
				paramsMap.put(key, "");
			}
        }
		for (Map.Entry<String, String> entry : paramsMap.entrySet()){
            sb.append(entry.getValue()).append(split);
		}
		if(sb.length() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static <T> String joinKeyValue(Map<String, T> params){
		if(CollectionUtil.isEmpty(params)) return "";
    	StringBuilder sb = new StringBuilder();
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
		for (Map.Entry<String, T> entry : params.entrySet()) {
			String key = entry.getKey();
			T value = entry.getValue();
			if(value != null){
				paramsMap.put(key, value.toString());
			}else {
				paramsMap.put(key, "");
			}
        }
		for (Map.Entry<String, String> entry : paramsMap.entrySet()){
            sb.append(entry.getKey() + "=" + entry.getValue());
		}
		return sb.toString();
	}
}
