package com.kingbo401.commons.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.kingbo401.commons.exception.MixedBagException;

/**
 * 集合工具类
 */
public class CollectionUtil {
	/**
     * Null-safe check if the specified collection is empty.
     * 
     * @param coll  the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }
    
    /**
     * Null-safe check if the specified collection is empty.
     * 
     * @param coll  the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(Map<?,?> coll) {
    	return (coll == null || coll.isEmpty());
    }
    
    /**
     * Null-safe check if the specified collection is empty.
     * 
     * @param coll  the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }
    
    /**
     * Null-safe check if the specified collection is empty.
     * 
     * @param coll  the collection to check, may be null
     * @return true if empty or null
     */
    public static boolean isNotEmpty(Map<?,?> coll) {
    	return !isEmpty(coll);
    }
    
    public static <T> Map<Object, T> toMap(Collection<T> coll, String fieldName){
		Map<Object, T> map = new HashMap<Object, T>();
		if(CollectionUtils.isEmpty(coll)){
			return map;
		}
		Assert.hasText(fieldName, "fieldName不能为空");
		try{
			for(T item : coll){
				Class<? extends Object> clazz = item.getClass();
				PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
				Method readMethod = pd.getReadMethod();
				map.put(readMethod.invoke(item), item);
			}
		}catch (Exception e) {
			throw new MixedBagException(e);
		}
		return map;
	} 
    
    public static <T> Map<Object, T> toIdMap(Collection<T> coll){
    	return toMap(coll, "id");
    }
}
