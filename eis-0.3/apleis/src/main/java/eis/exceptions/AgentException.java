package eis.exceptions;

/**
 * This exception is thrown if an attempt to register or unregister an agent has
 * failed.
 * 
 * @author tristanbehrens
 * @modified W.Pasman 14mar13 new constructor allowing exception chaining.
 * 
 */
public class AgentException extends EnvironmentInterfaceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1873593036493656628L;

	public AgentException(String string) {
		super(string);
	}

	public AgentException(String string, Exception cause) {
		super(string, cause);
	}
}
