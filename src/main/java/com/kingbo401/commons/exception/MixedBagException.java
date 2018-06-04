package com.kingbo401.commons.exception;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MixedBagException extends RuntimeException {
	
	private static final long serialVersionUID = 9012991893199059965L;
	
	private String code;
	
	public MixedBagException() {
		super();
	}

	public MixedBagException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public MixedBagException(String code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.code = code;
	}

	public MixedBagException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MixedBagException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public MixedBagException(String message) {
		super(message);
	}
	
	public MixedBagException(String code, String message) {
		super(message);
		this.code = code;
	}

	public MixedBagException(Throwable cause) {
		super(cause);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
