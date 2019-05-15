package smartspace.infra;

public class NotAnAdminException extends RuntimeException {

	private static final long serialVersionUID = 7314836193873052266L;

	String type;

	public NotAnAdminException() {
	}

	public NotAnAdminException(String message) {
		super(message);
	}

	public NotAnAdminException(Throwable cause) {
		super(cause);
	}

	public NotAnAdminException(String message, Throwable cause) {
		super(message, cause);
	}

	public String toString() {
		return this.getStackTrace() + "/n Only admins are allowed to import " + type;
	}

}
