package smartspace.plugin;

public class PlayerNotRegisteredToTaskException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6965616086117914249L;
	
	public PlayerNotRegisteredToTaskException() {
		super("This Player is not registered to this Task");
	}

}
