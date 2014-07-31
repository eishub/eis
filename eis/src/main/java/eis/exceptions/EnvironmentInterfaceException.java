package eis.exceptions;

/**
 * This exception is thrown if something unexpected happens when accessing the
 * environment-interface.
 * 
 * @author tristanbehrens
 * 
 */
public class EnvironmentInterfaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -851595650112711436L;

	public EnvironmentInterfaceException(String string) {
		super(string);
	}

	public EnvironmentInterfaceException(String string, Exception cause) {
		super(string, cause);
	}

}
