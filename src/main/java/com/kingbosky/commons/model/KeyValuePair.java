/**
 * $Id$
 * Copyright(C) 2011-2016 dreamingame.com. All rights reserved.
 */
package com.kingbosky.commons.model;

/**
 * 
 * @author <a href="mailto:shiyang.zhao@dreamingame.com">Rex.Zhao</a>
 * @version 1.0
 * @since 1.0 2011-11-09 22:11:05
 */
public class KeyValuePair<K,V> {

	private final K key;
	private final V value;
	
	public KeyValuePair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}
	public V getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return key + "=" + value;
	}
}
