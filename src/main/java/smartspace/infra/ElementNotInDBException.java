package smartspace.infra;

public class ElementNotInDBException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -905826369023552013L;

	public ElementNotInDBException() {
		super();
	}

	public String toString() {
		return this.getStackTrace() + "/n Can't import actions which their elements are not in DB!";
				 
	}
}
