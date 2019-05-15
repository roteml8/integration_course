package smartspace.infra;

public class EntityNotInDBException extends RuntimeException{

	private static final long serialVersionUID = 6037678696802229480L;
	
	
	public EntityNotInDBException() {
	}

	public EntityNotInDBException(String message) {
		super(message);
	}

	public EntityNotInDBException(Throwable cause) {
		super(cause);
	}

	public EntityNotInDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public String toString() {
		return "Login failed, there is no such user in the DB.";
	}

}
