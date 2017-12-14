package eis.exceptions;

/**
 * This action is thrown if an attempt to perform an action or to retrieve
 * percepts has failed.
 * 
 * 
 */
public class NoEnvironmentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7234401388974172460L;

	/**
	 * @param string
	 *            the error message
	 */
	public NoEnvironmentException(String string) {
		super(string);
	}

	/**
	 * @param string
	 *            the error message
	 * @param cause
	 *            the cause of the error
	 */
	public NoEnvironmentException(String string, Exception cause) {
		super(string, cause);
	}

}
