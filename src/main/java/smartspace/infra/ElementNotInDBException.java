package smartspace.infra;

public class ElementNotInDBException extends RuntimeException{

	private static final long serialVersionUID = -905826369023552013L;
	
	public ElementNotInDBException() {
	}

	public ElementNotInDBException(String message) {
		super(message);
	}

	public ElementNotInDBException(Throwable cause) {
		super(cause);
	}

	public ElementNotInDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public String toString() {
		return "/n Can't import actions which their elements are not in DB!";
				 
	}
}
