package eis.exceptions;

/**
 * This exception is thrown if an attempt to register or unregister an agent has
 * failed.
 * 
 * @author tristanbehrens
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

}
