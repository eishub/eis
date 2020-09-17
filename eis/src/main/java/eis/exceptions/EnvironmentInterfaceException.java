package eis.exceptions;

/**
 * This exception is thrown if something unexpected happens when accessing the
 * environment-interface.
 */
public class EnvironmentInterfaceException extends Exception {
	private static final long serialVersionUID = -851595650112711436L;

	public EnvironmentInterfaceException(final String string) {
		super(string);
	}

	public EnvironmentInterfaceException(final String string, final Exception cause) {
		super(string, cause);
	}
}
