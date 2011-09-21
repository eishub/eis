package apleis.repositoryagents;

/**
 * This exception is thrown when parsing fails.
 * 
 * @author tristanbehrens
 *
 */
public class ParseException extends Exception {

	private String path="";
	
	public ParseException(String message) {
		super(message);
	}

	public ParseException(String path, String message) {
		super(message);
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

}
