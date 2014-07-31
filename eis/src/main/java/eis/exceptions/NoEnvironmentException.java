package eis.exceptions;

/**
 * This action is thrown if an attempt to perform an action or to retrieve
 * percepts has failed.
 * 
 * @author tristanbehrens
 * @modified W.Pasman 14mar13 made unchecked so we can always throw this in
 *           exceptional cases.
 * 
 */
public class NoEnvironmentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7234401388974172460L;

	public NoEnvironmentException(String string) {
		super(string);
	}

	public NoEnvironmentException(String string, Exception cause) {
		super(string, cause);
	}

}
