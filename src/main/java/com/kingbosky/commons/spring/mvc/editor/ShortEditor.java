package com.kingbosky.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;

import com.kingbosky.commons.util.NumberUtil;

public class ShortEditor extends PropertyEditorSupport {
	private short defaultValue = 0;
	public ShortEditor(){
		
	}
	
	public ShortEditor(short defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}
	
	public void setAsText(String text) {
		setValue(NumberUtil.toShort(text, defaultValue));
	}

	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}
}
