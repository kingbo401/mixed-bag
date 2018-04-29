package com.kingbo401.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;

import com.kingbo401.commons.util.NumberUtil;

public class DoubleEditor extends PropertyEditorSupport {

	private double defaultValue = 0;
	
	public DoubleEditor(){
		
	}
	
	public DoubleEditor(double defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}

	public void setAsText(String text) {
		setValue(NumberUtil.toDouble(text, defaultValue));
	}

	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}
}
