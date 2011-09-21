package eis.exceptions;


/**
 * This exception is thrown if an attempt to manage an environment did not succeed.
 * @author tristanbehrens
 *
 */
@SuppressWarnings("serial")
public class ManagementException extends EnvironmentInterfaceException {

	public ManagementException(String string) {
		super(string);
	}

	public ManagementException(String string, Exception e) {
		super(string,e);
	}
	
}
