package com.kingbo401.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.util.StringUtils;


public class TimestampEditor extends PropertyEditorSupport {

	/**
	 * 按照定义时加入列表的顺序优先匹配.
	 */
	private final List<SimpleDateFormat> formats;
	private final boolean allowEmpty;

	public TimestampEditor(List<SimpleDateFormat> dateFormat, boolean allowEmpty) {
		this.formats = dateFormat;
		this.allowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && StringUtils.isEmpty(text)) {
			setValue(null);
		} else {
			for (int i = 0; i < this.formats.size(); i++) {
				try {
					setValue(new Timestamp(this.formats.get(i).parse(text)
							.getTime()));
					break;
				} catch (ParseException ex) {
					if (i == this.formats.size() - 1) {
						throw new IllegalArgumentException(
								"Could not parse date: " + ex.getMessage(), ex);
					}
					continue;
				}
			}
		}
	}

	@Override
	public String getAsText() {
		Timestamp stamp = (Timestamp) getValue();
		String text = "";
		if (stamp != null) {
			for (int i = 0; i < this.formats.size(); i++) {
				text = this.formats.get(i).format(stamp);
				if (!StringUtils.isEmpty(text)) {
					break;
				}
			}
		}
		return text;
	}
}
