package com.kingbosky.commons.exception;

public class RemoteException extends RuntimeException{
	private static final long serialVersionUID = 3516054689872431583L;
	public RemoteException(){}
	
	public RemoteException(String message){
		super(message);
	}
	
	public RemoteException(String message, Throwable cause){
		super(message, cause);
	}
	
	public RemoteException(Throwable cause){
		super(cause);
	}
}
