package smartspace.infra;

public class PluginProcessingFailedException extends RuntimeException{

	private static final long serialVersionUID = 5085826682104481949L;

	public PluginProcessingFailedException() {
	}

	public PluginProcessingFailedException(String badAction) {
		super(badAction);
	}

	public PluginProcessingFailedException(Throwable cause) {
		super(cause);
	}

	public PluginProcessingFailedException(String badAction, Throwable cause) {
		super(badAction , cause);
	}

}
