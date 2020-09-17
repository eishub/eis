package eis.exceptions;

/**
 * This exception is thrown if an attempt to manipulate the
 * agents-entities-relation has failed.
 */
public class RelationException extends EnvironmentInterfaceException {
	private static final long serialVersionUID = 176485209025703866L;

	/**
	 * @param string the error message
	 */
	public RelationException(final String string) {
		super(string);
	}

	/**
	 * @param string the error message
	 * @param cause  the cause of the error
	 */
	public RelationException(final String string, final Exception cause) {
		super(string, cause);
	}
}
