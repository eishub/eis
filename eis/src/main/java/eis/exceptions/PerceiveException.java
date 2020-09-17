package eis.exceptions;

/**
 * This action is thrown if an attempt to perform an action or to retrieve
 * percepts has failed.
 */
public class PerceiveException extends EnvironmentInterfaceException {
	private static final long serialVersionUID = -3368129059270218180L;

	public PerceiveException(final String string) {
		super(string);
	}

	public PerceiveException(final String message, final Exception cause) {
		super(message);
		initCause(cause);
	}
}
