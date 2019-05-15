package smartspace.layout;

public class UnsupportedSearchException extends RuntimeException{

	private static final long serialVersionUID = -7743417468097606948L;
	
	private static String messagePart1 = "Unsupported search parameter! wtf is ";
	private static String messagePart2 = "? consult the guidebook for a list of supported and FDA approved searches.";
	
	public UnsupportedSearchException() {
	}

	public UnsupportedSearchException(String badSearch) {
		super(messagePart1 + badSearch + messagePart2);
	}

	public UnsupportedSearchException(Throwable cause) {
		super(cause);
	}

	public UnsupportedSearchException(String badSearch, Throwable cause) {
		super(messagePart1 + badSearch + messagePart2, cause);
	}

}
