package kingbo401.commons.util;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtils;

import kingbo401.commons.annotation.Ignore;
import kingbo401.commons.exception.GeneralException;
import kingbo401.commons.exception.MapObjectConvertException;

public class MapObjectConverter {
	public static <T> Object convertSingleFieldObject(Object value, Class<T> clazz){
		if(value == null) return null;
		Object obj = value;
		if(Number.class.isAssignableFrom(clazz) || clazz.isPrimitive()){
			if(value instanceof Number){
				if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
					obj = ((Number) value).intValue();
				} else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
					obj = ((Number) value).longValue();
				} else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
					obj = ((Number) value).floatValue();
				} else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
					obj = ((Number) value).doubleValue();
				} else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
					obj = ((Number) value).shortValue();
				}else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
					obj = ((Number) value).byteValue();
				}else if(clazz.equals(boolean.class) || clazz.equals(Boolean.class)){
					if(((Number) value).longValue() > 0){
						obj = true;
					}else{
						obj = false;
					}
				}
			}else if(value instanceof String){
				String valStr = value.toString();
				if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
					obj = Long.valueOf(valStr).intValue();
				} else if (clazz.equals(long.class) || clazz.equals(Long.class)) {
					obj = Long.valueOf(valStr);
				} else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
					obj = Float.valueOf(valStr);
				} else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
					obj = Double.valueOf(valStr);
				} else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
					obj = Long.valueOf(valStr).shortValue();
				}else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
					obj = Long.valueOf(valStr).byteValue();
				}else if(clazz.equals(boolean.class) || clazz.equals(Boolean.class)){
					
					if(toInt(valStr, 0) > 0 || "true".equalsIgnoreCase(valStr)){
						obj = true;
					}else{
						obj = false;
					}
				}
			}
		}
		return obj;
	}
	
	 private static int toInt(String str, int defaultValue) {
        if(str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
	
	public static <T> boolean isSingleFieldClass(Class<T> clazz){
		return clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) 
				|| clazz.equals(String.class) || clazz.equals(Date.class) 
				|| clazz.equals(Timestamp.class);
	}
	
	
	//可通过此方发避免Date空值异常
//	static{
//		ConvertUtils.register(new Converter(){
//			@SuppressWarnings("rawtypes")
//			@Override
//			public Object convert(Class clazz, Object value) {
//				if(value == null) return null;
//				return value;
//			}
//			
//		}, java.util.Date.class);
//	}
	
	/**
	 * 
	 * @param dataMap
	 * @param clazz
	 * @param mapKeyUnderlineToCamelCase 是否把dataMap key的下划线转换成驼峰模式
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertMapToObject(Map<String, Object> dataMap, Class<T> clazz, boolean mapKeyUnderlineToCamelCase) {
		if (dataMap == null || clazz == null || clazz.equals(Void.class)) return null;
		Object obj = null;
		if(isSingleFieldClass(clazz)){//只有一列
			if(dataMap.size() != 1) {
				//如果clazz为单属性类且dataMap不是单值，抛出异常
				throw new MapObjectConvertException("map convert to bean failed, map:" + dataMap + " clazz:" + clazz.getName());
			}
			Iterator<Entry<String, Object>> itr = dataMap.entrySet().iterator();
			if (itr.hasNext()) {
				Map.Entry<String, Object> entry = itr.next();
				Object value = entry.getValue();
				try{
					obj = convertSingleFieldObject(value, clazz);
				}catch(NumberFormatException e){
					throw new MapObjectConvertException("mapSingleColumnRow failed, map:" + dataMap + " clazz:" + clazz.getName(), e);
				}
			}
		}else {
			try {
				obj = Class.forName(clazz.getName()).newInstance();
				Map<String, Object> data = new HashMap<String, Object>();
				//避免空值引起BeanUtils.populate异常
				Iterator<Entry<String, Object>> itr = dataMap.entrySet().iterator();
				while (itr.hasNext()) {//删除null值，避免BeanUtils.populate date类型报异常
					Map.Entry<String, Object> entry = itr.next();
					if(entry.getValue() == null){
						itr.remove();
					}
				}
				if(mapKeyUnderlineToCamelCase){
					Set<String> keys = dataMap.keySet();
					for(String key : keys){
						data.put(underlineToCamel(key), dataMap.get(key));
					}
					BeanUtils.populate(obj, data);
				}else{
					BeanUtils.populate(obj, dataMap);
				}
			} catch (Exception e) {
				throw new MapObjectConvertException("map convert to bean failed, map:" + dataMap + " clazz:" + clazz.getName(), e);
			}
		}
		return (T) obj;
	}
	
	private static final char UNDERLINE = '_';
	public static String underlineToCamel(String name) {
		if (name == null || "".equals(name.trim())) {
			return "";
		}
		int len = name.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = name.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(name.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}  
	
//	private static void convertObjectToMap(Object obj, Class<?> clazz, Map<String, Object> rst){
//	Field[] fields = clazz.getDeclaredFields();
//	try {
//		for (int i = 0; i < fields.length; i++) {
//			try {
//				Field f = fields[i];
//				f.setAccessible(true);
//				Object o = f.get(obj);
//				rst.put(fields[i].getName(), o);
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
//		}
//	} catch (SecurityException e) {
//		e.printStackTrace();
//	}
//	Class<?> parentClass  = clazz.getSuperclass();
//	if(!(parentClass.getCanonicalName().equals(Object.class.getCanonicalName()))){
//		convertBeanToMap(obj, parentClass, rst);
//	}
//}
//
//private static Map<String, Object> convertObjectToMap(Object obj) {
//	if (obj == null) return null;
//	Map<String, Object> rst = new HashMap<String, Object>();
//	convertObjectToMap(obj, obj.getClass(), rst);
//	return rst;
//}
	
	private static final Map<Class<?>, List<Method>> clazzMethodCache = new ConcurrentHashMap<Class<?>, List<Method>>();
	public static Map<String, Object> convertObjectToMap(Object obj){
		return convertObjectToMap(obj, false);
	}
	
	/**
	 * 从get方法中获取属性
	 * @param obj
	 * @param objFiledcamelToUnderline是否把obj属性字段的驼峰模式改成下划线
	 * @return
	 */
	public static Map<String, Object> convertObjectToMap(Object obj, boolean objFiledCamelToUnderline){
		if (obj == null) return null;
		Map<String, Object> rst = new HashMap<String, Object>();
		Class<?> clazz = obj.getClass();
		
		List<Method> methodList = clazzMethodCache.get(clazz);
		if(methodList == null || methodList.size() == 0){
			List<Method> tmpList = new ArrayList<Method>();
			Method[] methods = clazz.getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if(method.isAnnotationPresent(Ignore.class) || method.getParameterTypes().length > 0) {
					continue;
				}
				String name = method.getName();
				if(name.equals("getClass")){
					continue;
				}
				int len = name.length();
				if(name.startsWith("get") && len > 3){
					tmpList.add(method);
				}
			}
			methodList = Collections.unmodifiableList(tmpList);
			clazzMethodCache.put(clazz, methodList);
		}
		for (Method method : methodList) {
			try {
				String name = method.getName();
				int len = name.length();
				StringBuilder filedName = new StringBuilder();
				filedName.append(Character.toLowerCase(name.charAt(3)));
				if(len > 4){
					filedName.append(name.substring(4));
				}
				Object o = method.invoke(obj);
				String key = filedName.toString();
				if(objFiledCamelToUnderline){
					key = camelToUnderline(key);
				}
				rst.put(key, o);
			}catch (Exception e){
				throw new GeneralException(e);
			}
		}
		return rst;
	}
}
