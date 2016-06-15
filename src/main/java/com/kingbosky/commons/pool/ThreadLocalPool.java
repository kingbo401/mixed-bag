package com.kingbosky.commons.pool;

import java.util.List;

import com.kingbosky.commons.util.CollectionUtil;

/**
 * 全局ThreadLocal管理类
 * 
 */
public class ThreadLocalPool {

	private static final Object lock = new Object();
	private static final List<ThreadLocal<Object>> lstLocal = CollectionUtil.newArrayList();

	@SuppressWarnings("unchecked")
	public static <T> ThreadLocal<T> createThreadLocal() {
		ThreadLocal<T> t = new ThreadLocal<T>();
		synchronized (lock) {
			lstLocal.add((ThreadLocal<Object>)t);
		}
		return t;
	}
	
	public static void clear() {
		for (ThreadLocal<Object> t : lstLocal) {
			t.remove();
		}
	}
}
