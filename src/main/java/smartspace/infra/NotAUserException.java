package smartspace.infra;

public class NotAUserException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4759828662971645723L;
	
	public NotAUserException(String message) {
		super(message);
	}

}
