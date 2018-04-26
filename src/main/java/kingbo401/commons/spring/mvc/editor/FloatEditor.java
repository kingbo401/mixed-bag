package kingbo401.commons.spring.mvc.editor;

import java.beans.PropertyEditorSupport;

import kingbo401.commons.util.NumberUtil;

public class FloatEditor extends PropertyEditorSupport {

	private float defaultValue = 0;
	
	public FloatEditor(){
		
	}
	
	public FloatEditor(float defaultValue) {
		super();
		this.defaultValue = defaultValue;
	}
	
	public void setAsText(String text) {
		setValue(NumberUtil.toFloat(text, defaultValue));
	}

	public String getAsText() {
		Object value = getValue();
		return (value != null ? value.toString() : "");
	}
}
