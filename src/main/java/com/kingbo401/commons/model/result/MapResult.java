package com.kingbo401.commons.model.result;

import java.util.Map;

public class MapResult<K, V> extends BaseResult{
	private Map<K, V> content;
	
	public MapResult() {
		super();
	}

	public MapResult(Map<K, V> content) {
		super();
		this.content = content;
	}

	public Map<K, V> getContent() {
		return content;
	}

	public void setContent(Map<K, V> content) {
		this.content = content;
	}
}
