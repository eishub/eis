package eis.iilang;

/**
 * An identifier is an arbitrary string.
 * 
 * @author tristanbehrens
 *
 */
/**
 * @author tristanbehrens
 *
 */
public class Identifier extends Parameter {

	/** The identifier itself. */
	private String value = null;

	/** 
	 * Constructs an identifier.
	 * 
	 * @param value
	 */
	public Identifier(String value) {
		
		this.value = value;
		
	}
	
	/**
	 * Returns the identifier.
	 * 
	 * @return the identifier as a string
	 */
	public String getValue() {
		
		return value;
		
	}
	
	@Override
	protected String toXML(int depth) {
		
		return indent(depth) + "<identifier value=\"" + value + "\"/>" + "\n";
	
	}
	
	@Override
	public String toProlog() {
		
		String ret = value;
		
		return ret;
	
	}

	@Override
	public Object clone() {

		return new Identifier(this.value);
	
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
		if (!(obj instanceof Identifier))
			return false;
		Identifier other = (Identifier) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
