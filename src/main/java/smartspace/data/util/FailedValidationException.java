package smartspace.data.util;

public class FailedValidationException extends RuntimeException{

	private static final long serialVersionUID = 3002797642596464971L;

	String type;

	public FailedValidationException(String type) {
		super();
		this.type = type;
	}

	public String toString() {
		return this.getStackTrace() + "/n Invalid " + type;
	}

}
