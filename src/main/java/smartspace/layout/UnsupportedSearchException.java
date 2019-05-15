package smartspace.layout;

public class UnsupportedSearchException extends RuntimeException{

	private static final long serialVersionUID = -7743417468097606948L;
	
	public UnsupportedSearchException() {
	}

	public UnsupportedSearchException(String message) {
		super(message);
	}

	public UnsupportedSearchException(Throwable cause) {
		super(cause);
	}

	public UnsupportedSearchException(String message, Throwable cause) {
		super(message, cause);
	}

}
