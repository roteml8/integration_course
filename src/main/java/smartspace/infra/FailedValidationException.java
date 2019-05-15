package smartspace.infra;

public class FailedValidationException extends RuntimeException{

	private static final long serialVersionUID = 3002797642596464971L;
	
	public FailedValidationException() {
	}

	public FailedValidationException(String message) {
		super(message);
	}

	public FailedValidationException(Throwable cause) {
		super(cause);
	}

	public FailedValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
