/**
 * $Id: ThreadLocalPool.java 994 2012-05-29 10:34:25Z zhongmin.li $
 * Copyright(C) 2010-2016 happyelements.com. All rights reserved.
 */
package com.kingbosky.commons.pool;

import java.util.List;

import com.kingbosky.commons.util.CollectionUtil;

/**
 * 全局ThreadLocal管理类
 * 
 * @author <a href="mailto:zhongmin.li@dreamingame.com">Andy Lee</a>
 * @version 1.0 2012-3-8 下午5:29:04
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
