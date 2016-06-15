package com.kingbosky.commons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

	private final static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
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
		Map<String, Object> retMap = null;
		if (obj == null) {
			return null;
		}
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
		Map<String, String> retMap = null;
		if (obj == null) {
			return null;
		}
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
		Map<String, Object> retMap = null;
		if (StringUtils.isBlank(str)) {
			return null;
		}
		try {
			retMap = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			logger.error("String to map failed!", e);
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
		Map<String, String> retMap = null;
		if (StringUtils.isBlank(str)) {
			return null;
		}
		try {
			retMap = parseToGeneric(str, Map.class, String.class, String.class);
		} catch (Exception e) {
			logger.error("String to map failed!", e);
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
		T object = null;
		if (StringUtils.isBlank(str)) {
			return null;
		}
		try {
			logger.info("parseToObject : " + t + " , str : " + str);
			object = (T) mapper.readValue(str, t);
			logger.info("parseToObject object : " + object);
		} catch (Exception e) {
			logger.error("String to map failed!", e);
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
		List<Map<String, Object>> array = null;
		if (StringUtils.isBlank(str)) {
			return null;
		}
		try {
			array = mapper.readValue(str, List.class);
		} catch (Exception e) {
			logger.error("String to map failed!", e);
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
		List<T> list = null;
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		try {
			logger.info("parseToList: List<" + clazz + "> , jsonStr : " + jsonStr);
			list = (List<T>) mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
			logger.info("parseToList: " + list);
		} catch (Exception e) {
			logger.error("String to list failed!", e);
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
		Object collectionObj = null;
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		try {
			logger.info("parseToCollection: " + collectionClass + "<" + clazz + "> , jsonStr : " + jsonStr);
			collectionObj = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructCollectionType(collectionClass, clazz));
			logger.info("parseToCollection: " + collectionObj);
		} catch (Exception e) {
			logger.error("String to list failed!", e);
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
		T genericObject = null;
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		try {
			logger.info("parseToGeneric: " + genericClass + "<" + Arrays.toString(parameterClasses) + "> , jsonStr : "
					+ jsonStr);
			genericObject = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses));
			logger.info("parseToGeneric: " + genericObject);
		} catch (Exception e) {
			logger.error("String to list failed!", e);
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
		T genericObject = null;
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		try {
			logger.info("parseToGeneric: " + genericClass + "<" + Arrays.toString(parameterClasses) + "> , jsonStr : "
					+ jsonStr);
			genericObject = mapper.readValue(jsonStr,
					mapper.getTypeFactory().constructParametricType(genericClass, parameterClasses));
			logger.info("parseToGeneric: " + genericObject);
		} catch (Exception e) {
			logger.error("String to list failed!", e);
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
		String retStr = "";
		if (obj == null) {
			return retStr;
		}
		try {
			retStr = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("Object to json string failed!", e);
		}
		return retStr;
	}
}
