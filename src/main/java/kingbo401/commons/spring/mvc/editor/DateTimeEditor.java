package kingbo401.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

public class DateTimeEditor extends PropertyEditorSupport {

	/**
	 * 按照定义时加入列表的顺序优先匹配.
	 */
	private final List<SimpleDateFormat> formats;
	private final boolean allowEmpty;

	public DateTimeEditor(List<SimpleDateFormat> dateFormat, boolean allowEmpty) {
		this.formats = dateFormat;
		this.allowEmpty = allowEmpty;
	}

	public DateTimeEditor(List<SimpleDateFormat> dateFormat,
			boolean allowEmpty, int exactDateLength) {
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
					setValue(this.formats.get(i).parse(text));
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
		Date date = (Date) getValue();
		String text = "";
		if (date != null) {
			for (int i = 0; i < this.formats.size(); i++) {
				text = this.formats.get(i).format(date);
				if (!StringUtils.isEmpty(text)) {
					break;
				}
			}
		}
		return text;
	}
}
