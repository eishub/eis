package eis.exceptions;

/**
 * This action is thrown if an attempt to perform an action or to retrieve
 * percepts has failed.
 * 
 * @author tristanbehrens
 * 
 */
public class PerceiveException extends EnvironmentInterfaceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3368129059270218180L;

	public PerceiveException(String string) {
		super(string);
	}

	public PerceiveException(String message, Exception cause) {
		super(message);

		this.initCause(cause);

	}

}
