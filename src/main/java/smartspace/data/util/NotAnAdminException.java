package smartspace.data.util;

public class NotAnAdminException extends RuntimeException {

	private static final long serialVersionUID = 7314836193873052266L;

	String type;

	public NotAnAdminException(String type) {
		super();
		this.type = type;
	}

	public String toString() {
		return this.getStackTrace() + "/n Only admins are allowed to import " + type;
	}

}
