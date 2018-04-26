package kingbo401.commons.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import kingbo401.commons.util.CollectionUtil;
import kingbo401.commons.util.Constants;

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
		for(Map.Entry<String, String[]> entry : paraMap.entrySet()){
			String key = entry.getKey();
			String[] values = entry.getValue();
			String valueStr = "";
			if(values != null && values.length > 0 && values[0] != null){
				valueStr = values[0];
			}
			retMap.put(key, valueStr);
		}
		return retMap;
	}
	
    public static <T> String getKeyAndValueStr(Map<String, T> params){
		return getKeyAndValueStr(params, false);
	}
    
    public static <T> String getKeyAndValueStr(Map<String, T> params, boolean urlEncode){
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
			String value = entry.getValue();
			if(urlEncode){
				value = urlEncode(value, Constants.DFT_CHARSET);
			}
            sb.append(entry.getKey()).append("=").append(value).append("&");
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
	
	/**
	 * 
	 * @param params a=b&c=d
	 */
	public static Map<String, String> parseKeyAndValStr(String params){
		Map<String, String> paramMap = new HashMap<String, String>();
		String[] pairs = params.split("&");
		int len = pairs.length;
		for (int i = 0; i < len; i++) {
			String pair = pairs[i];
			String[] keyValue = pair.split("=");
			if (keyValue.length != 2) {
				if (keyValue.length == 1) {
					String key = keyValue[0];
					paramMap.put(key, "");
				} else {
					return null;
				}
			} else {
				String key = keyValue[0];
				String value = keyValue[1];
				paramMap.put(key, value);
			}
		}
		return paramMap;
	}
}
