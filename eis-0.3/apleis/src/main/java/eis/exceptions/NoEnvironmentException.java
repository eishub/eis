package eis.exceptions;

/**
 * This action is thrown if an attempt to perform an action or to retrieve
 * percepts has failed.
 * 
 * @author tristanbehrens
 * 
 */
public class NoEnvironmentException extends EnvironmentInterfaceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6014167691338765953L;

	public NoEnvironmentException(String string) {
		super(string);
	}

	public NoEnvironmentException(String message, Exception cause) {
		super(message);

		this.initCause(cause);

	}

}
