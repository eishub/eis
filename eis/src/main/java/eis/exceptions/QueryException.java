package eis.exceptions;

/**
 * This exception is thrown when querying fails.
 * 
 * @author tristanbehrens
 * @author W.Pasman 16feb2012 added constructor for convenience
 * @modified W.Pasman 14mar13 new constructor allowing exception chaining.
 * 
 */
public class QueryException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4100088707624572939L;

	public QueryException(String string) {
		super(string);
	}

	public QueryException(String string, Exception cause) {
		super(string, cause);
	}

}
