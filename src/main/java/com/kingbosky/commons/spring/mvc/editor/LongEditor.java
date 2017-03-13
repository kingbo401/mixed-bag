package com.kingbosky.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;

import com.kingbosky.commons.util.NumberUtil;

public class LongEditor extends PropertyEditorSupport {
	private long defaultValue = 0;
	public LongEditor(){
		
	}
	
	public LongEditor(long defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}
	
	public void setAsText(String text) {
		setValue(NumberUtil.toLong(text, defaultValue));
	}

	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}
}
