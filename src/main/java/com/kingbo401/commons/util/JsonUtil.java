package com.kingbo401.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

	private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 对象转为map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> convertToMap(Object obj) {
		if(obj == null) return null;
		Map<String, Object> retMap = null;
		try {
			JavaType mapType = constructGenericType(Map.class, String.class, Object.class);
			retMap = mapper.convertValue(obj, mapType);
		} catch (Exception e) {
			logger.error("Object to map failed!", e);
		}
		return retMap;
	}

	/**
	 * 对象转为String map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> convertToStringMap(Object obj) {
		if(obj == null) return null;
		Map<String, String> retMap = null;
		try {
			JavaType mapType = constructGenericType(Map.class, String.class, String.class);
			retMap = mapper.convertValue(obj, mapType);
		} catch (Exception e) {
			logger.error("Object to map failed!", e);
		}
		return retMap;
	}

	/**
	 * 解析json string为map对象
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parse(String str) {
		if(StringTool.isEmpty(str)) return null;
		Map<String, Object> retMap = null;
		try {
			retMap = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			logger.error("String to map failed,str:" + str, e);
		}
		return retMap;
	}

	/**
	 * 解析json string为String 键值 map对象
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseToStringMap(String str) {
		if(StringTool.isEmpty(str)) return null;
		Map<String, String> retMap = null;
		try {
			retMap = parseToGeneric(str, Map.class, String.class, String.class);
		} catch (Exception e) {
			logger.error("String to map failed,str:" + str, e);
		}
		return retMap;
	}

	/**
	 * 解析json string为String 键值 map对象
	 * 
	 * @param <T>
	 * @param str
	 * @return
	 */
	public static <T> T parseToObject(String str, Class<T> t) {
		if(StringTool.isEmpty(str)) return null;
		T object = null;
		try {
			object = (T) mapper.readValue(str, t);
		} catch (Exception e) {
			logger.error("String to map failed,str:" + str, e);
		}
		return object;
	}

	/**
	 * 解析json string为list对象
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> parseArray(String str) {
		if(StringTool.isEmpty(str)) return null;
		List<Map<String, Object>> array = null;
		try {
			array = mapper.readValue(str, List.class);
		} catch (Exception e) {
			logger.error("String to map failed,str:" + str, e);
		}
		return array;
	}

	/**
	 * 解析json string为list对象
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> parseToList(String jsonStr, Class<T> clazz) {
		if(StringTool.isEmpty(jsonStr)) return null;
		List<T> list = null;
		try {
			list = (List<T>) mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
		} catch (Exception e) {
			logger.error("String to list failed,jsonStr:" + jsonStr, e);
		}
		return list;
	}

	/**
	 * 解析json string为集合对象
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> Object parseToCollection(String jsonStr, Class<? extends Collection> collectionClass,
			Class<T> clazz) {
		if(StringTool.isEmpty(jsonStr)) return null;
		Object collectionObj = null;
		try {
			collectionObj = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructCollectionType(collectionClass, clazz));
		} catch (Exception e) {
			logger.error("String to list failed,jsonStr:" + jsonStr, e);
		}
		return collectionObj;
	}

	/**
	 * 解析json string为泛型对象
	 * 
	 * @param str
	 * @return
	 */
	public static <T> T parseToGeneric(String jsonStr, Class<T> genericClass, Class<?>... parameterClasses) {
		if(StringTool.isEmpty(jsonStr)) return null;
		T genericObject = null;
		try {
			genericObject = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses));
		} catch (Exception e) {
			logger.error("String to list failed,jsonStr:" + jsonStr, e);
		}
		return genericObject;
	}

	/**
	 * 解析json string为泛型对象
	 * 
	 * @param str
	 * @return
	 */
	public static <T> T parseToGeneric(String jsonStr, Class<T> genericClass, JavaType... parameterClasses) {
		if(StringTool.isEmpty(jsonStr)) return null;
		T genericObject = null;
		try {
			genericObject = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses));
		} catch (Exception e) {
			logger.error("String to list failed,jsonStr:" + jsonStr, e);
		}
		return genericObject;
	}

	/**
	 * 构造泛型java类型
	 * 
	 * @param genericClass
	 * @param parameterClasses
	 * @return
	 */
	public static JavaType constructGenericType(Class<?> genericClass, Class<?>... parameterClasses) {
		return mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses);
	}

	/**
	 * 构造泛型java类型
	 * 
	 * @param genericClass
	 * @param parameterClasses
	 * @return
	 */
	public static JavaType constructGenericType(Class<?> genericClass, JavaType... parameterClasses) {
		return mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses);
	}

	/**
	 * 构造简单java类型
	 * 
	 * @param genericClass
	 * @param parameterClasses
	 * @return
	 */
	public static JavaType constructJavaType(Class<?> clazz) {
		return mapper.getTypeFactory().constructType(clazz);
	}

	/**
	 * object对象转换给json string
	 * 
	 * @param obj
	 * @return
	 */
	public static String objectToJson(Object obj) {
		if(obj == null) return null;
		String retStr = "";
		try {
			retStr = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("Object to json string failed!", e);
		}
		return retStr;
	}
}
