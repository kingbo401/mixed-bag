package com.kingbosky.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 集合工具类
 */
public class CollectionUtil {
	/** 单例 */
	private static final CollectionUtil instance = new CollectionUtil();
	
	private CollectionUtil() {}
	
	/**
	 * 取得该类唯一实例
	 * @return 该类唯实例
	 */
	public static CollectionUtil instance() {
		return instance;
	}
	
	/**
	 * 创建一个新的java.util.HashMap对象
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return 新创建的java.util.HashMap对象
	 */
	public static final <K,V> Map<K,V> newHashMap() {
		return new HashMap<K,V>();
	}
	public static final <K,V> Map<K,V> newHashMap(int initialCapacity) {
		return new HashMap<K,V>(initialCapacity);
	}
	
	/**
	 * 创建一个新的java.util.concurrent.ConcurrentMap对象
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return 新创建的java.util.concurrent.ConcurrentMap对象
	 */
	public static final <K,V> ConcurrentMap<K,V> newConcurrentMap() {
		return new ConcurrentHashMap<K,V>();
	}
	public static final <K,V> Map<K,V> newConcurrentMap(int initialCapacity) {
		return new ConcurrentHashMap<K,V>(initialCapacity);
	}
	
	/**
	 * 创建一个新的java.util.TreeMap对象
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return 新创建的java.util.TreeMap对象
	 */
	public static final <K,V> Map<K,V> newTreeMap() {
		return new TreeMap<K,V>();
	}

	/**
	 * 创建一个新的java.util.HashSet对象
	 * @param <E> Value类型
	 * @return 新创建的java.util.HashSet对象
	 */
	public static final <E> Set<E> newHashSet() {
		return new HashSet<E>();
	}
	public static final <E> Set<E> newHashSet(int initialCapacity) {
		return new HashSet<E>(initialCapacity);
	}
	
	/**
	 * 创建一个新的java.util.TreeSet对象
	 * @param <E> Value类型
	 * @return 新创建的java.util.TreeSet对象
	 */
	public static final <E> Set<E> newTreeSet() {
		return new TreeSet<E>();
	}
	
	/**
	 * 创建一个新的java.util.ArrayList对象
	 * @param <E> Value类型
	 * @return 新创建的java.util.ArrayList对象
	 */
	public static final <E> List<E> newArrayList() {
		return new ArrayList<E>();
	}
	public static final <E> List<E> newArrayList(int initialCapacity) {
		return new ArrayList<E>(initialCapacity);
	}
	public static final <E> List<E> newArrayList(Collection<E> c) {
		return new ArrayList<E>(c);
	}
	/**
	 * 创建一个新的java.util.LinkedHashMap对象
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return 新创建的java.util.LinkedHashMap对象
	 */
	public static final <K, V> Map<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}
	
	/**
	 * 创建一个新的数组
	 * @param <T> Value类型
	 * @param t Value
	 * @return 新创建的数组
	 */
	public static <T> T[] newArray(T... t) {
	    return t;
	}

	/**
	 * 创建一个新的java.util.LinkedList对象
	 * @param <E> Value类型
	 * @return 新创建的java.util.LinkedList对象
	 */
	public static final <E> LinkedList<E> newLinkedList() {
		return new LinkedList<E>();
	}

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
    
}
