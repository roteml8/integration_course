package smartspace.plugin;

public class WrongElementTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8987287644024836217L;
	
	public WrongElementTypeException() {
		super();
	}
	
	public WrongElementTypeException(String expected) {
		super("This Element is not type "+expected);
	}

}
