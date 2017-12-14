package eis.exceptions;

/**
 * This exception is thrown if an attempt to register or unregister an agent has
 * failed.
 * 
 */
public class AgentException extends EnvironmentInterfaceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1873593036493656628L;

	/**
	 * @param string
	 *            the error message
	 */
	public AgentException(String string) {
		super(string);
	}

	/**
	 * @param string
	 *            the error message
	 * @param cause
	 *            the cause of the exception
	 */
	public AgentException(String string, Exception cause) {
		super(string, cause);
	}
}
