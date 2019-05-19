package smartspace.infra;

public class NotAPlayerException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1624896351702264140L;
	
	public NotAPlayerException(String message) {
		super(message);
	}

}
