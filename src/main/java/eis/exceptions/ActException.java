package eis.exceptions;

/**
 * This action is thrown if an attempt to perform an action
 * has failed. An action could fail if the action is not
 * supported by the environment, if it has wrong parameters,
 * or if it cannot be executed because of the state of the
 * environment.
 * 
 * @author tristanbehrens
 *
 */
@SuppressWarnings("serial")
public class ActException extends EnvironmentInterfaceException {

	public ActException(String string) {
		super(string);
	}

	public ActException(String message, Exception cause) {
		super(message);
		
		this.initCause(cause);
		
	} 
	
}
