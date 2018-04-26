package kingbo401.commons.exception;

public class GeneralException extends RuntimeException {
	
	private static final long serialVersionUID = 9012991893199059965L;

	public GeneralException(){}
	
	public GeneralException(String message){
		super(message);
	}
	
	public GeneralException(String message, Throwable cause){
		super(message, cause);
	}
	
	public GeneralException(Throwable cause){
		super(cause);
	}
}
