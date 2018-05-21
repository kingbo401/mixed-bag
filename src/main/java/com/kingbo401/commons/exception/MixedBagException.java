package com.kingbo401.commons.exception;

public class MixedBagException extends RuntimeException {
	
	private static final long serialVersionUID = 9012991893199059965L;

	public MixedBagException(){}
	
	public MixedBagException(String message){
		super(message);
	}
	
	public MixedBagException(String message, Throwable cause){
		super(message, cause);
	}
	
	public MixedBagException(Throwable cause){
		super(cause);
	}
}
