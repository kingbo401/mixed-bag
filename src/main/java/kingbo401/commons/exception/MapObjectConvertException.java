package kingbo401.commons.exception;

public class MapObjectConvertException extends RuntimeException{
	private static final long serialVersionUID = 4799650800861283175L;

	public MapObjectConvertException() {
		super();
	}

	public MapObjectConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapObjectConvertException(String message) {
		super(message);
	}

	public MapObjectConvertException(Throwable cause) {
		super(cause);
	}
}
