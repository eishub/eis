package eis.exceptions;

/**
 * This exception is thrown when querying fails.
 * 
 * @author tristanbehrens
 * @author W.Pasman 16feb2012 added constructor for convenience
 * 
 */
public class QueryException extends Exception {
	public QueryException(String string) {
		super(string);
	}
}
