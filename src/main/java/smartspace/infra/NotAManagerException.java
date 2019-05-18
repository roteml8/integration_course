package smartspace.infra;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

public class NotAManagerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8741080774555876717L;
	
	public NotAManagerException(String message) {
		super(message);
	}
	
	

}
