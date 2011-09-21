package eis.iilang;

/**
 * Encapsulates a truth value. 
 * That is either true or false or anything else (multi-valued logic).
 * 
 * @author tristanbehrens
 *
 */
public class TruthValue extends Parameter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The value of the numner. */
	private String value;

	/**
	 * Contructs a truth-value.
	 * 
	 * @param value
	 */
	public TruthValue(String value) {
		
		this.value = value;
		
	}
	
	/**
	 * Constructs a truth-value from a boolean one.
	 * @param bool
	 */
	public TruthValue(boolean bool) {
		
		if( bool == true)
			value = "true";
		else 
			value = "false";
		
	}
	
	@Override
	protected String toXML(int depth) {

		return indent(depth) + "<number value=\"" + value + "\"/>" + "\n";

	}
	
	@Override
	public String toProlog() {
		
		String ret = "";
		
		ret += value;
		
		return ret;
	
	}
	
	/**
	 * Returns the value.
	 * 
	 * @return
	 */
	public String getValue() {
		
		return value;
		
	}

	/**
	 * Gets the value as a boolean. Not that this only works iff the value itself
	 * is either true or false.
	 * 
	 * @return
	 */
	public boolean getBooleanValue() {
		
		if ( value.equals("true") )
			return true;
		else if ( value.equals("false") )
			return false;
		else throw new AssertionError(value + "cannot be converted to boolean");
		
	}

	@Override
	public Object clone() {
		return new TruthValue(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TruthValue))
			return false;
		TruthValue other = (TruthValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
