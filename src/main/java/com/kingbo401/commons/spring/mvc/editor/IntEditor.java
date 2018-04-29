package com.kingbo401.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;

import com.kingbo401.commons.util.NumberUtil;

public class IntEditor extends PropertyEditorSupport {

	private int defaultValue = 0;
	public IntEditor(){
		
	}
	
	public IntEditor(int defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}
	
	public void setAsText(String text) {
		setValue(NumberUtil.getInt(text, defaultValue));
	}

	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}
}
