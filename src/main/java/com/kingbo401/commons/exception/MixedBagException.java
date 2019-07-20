package com.kingbo401.commons.exception;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.kingbo401.commons.enums.ErrorCode;

/**
 * 自定义异常
 * @author kingbo401
 * @date 2019/07/20
 */
public class MixedBagException extends RuntimeException {
	
	private static final long serialVersionUID = 9012991893199059965L;
	
	/**
	 * 异常编码
	 */
	private String code;
	
	public MixedBagException() {
		super();
	}

	public MixedBagException(String message) {
        super(message);
    }
	

    public MixedBagException(String message, Throwable cause) {
        super(message, cause);
    }
    
	public MixedBagException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public MixedBagException(String code, String message) {
		super(message);
		this.code = code;
	}

	public MixedBagException(Throwable cause) {
		super(cause);
	}
	
	public MixedBagException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
    }
	
	public MixedBagException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
	
	public MixedBagException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.code = errorCode.getCode();
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
