package com.kingbo401.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;

import org.springframework.web.util.HtmlUtils;

public class StringHtmlEscapeEditor extends PropertyEditorSupport {

	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String value = text.trim();
			setValue(HtmlUtils.htmlEscape(value));
		}
	}

	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}

}
