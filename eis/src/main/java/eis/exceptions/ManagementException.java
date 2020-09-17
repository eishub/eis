package eis.exceptions;

/**
 * This exception is thrown if an attempt to manage an environment did not
 * succeed.
 */
public class ManagementException extends EnvironmentInterfaceException {
	private static final long serialVersionUID = 8409112005507306177L;

	public ManagementException(final String string) {
		super(string);
	}

	public ManagementException(final String string, final Exception e) {
		super(string, e);
	}
}
