package smartspace.infra;

public class UnsupportedActionTypeException extends RuntimeException{

	private static final long serialVersionUID = -7743417468097606948L;
	
	private static String messagePart1 = "Unsupported action type! wtf is ";
	private static String messagePart2 = "? consult the cookbook for a list of supported and inoffensive actions.";
	
	public UnsupportedActionTypeException() {
	}

	public UnsupportedActionTypeException(String badAction) {
		super(messagePart1 + badAction + messagePart2);
	}

	public UnsupportedActionTypeException(Throwable cause) {
		super(cause);
	}

	public UnsupportedActionTypeException(String badAction, Throwable cause) {
		super(messagePart1 + badAction + messagePart2, cause);
	}

}
