package com.kingbo401.commons.util;

import java.util.Collection;
import java.util.Map;

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
}
