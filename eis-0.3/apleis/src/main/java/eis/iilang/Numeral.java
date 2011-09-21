package eis.iilang;

/**
 * Encapsulates a number.
 * 
 * @author tristanbehrens
 *
 */
public class Numeral extends Parameter {

	/** The value of the numner. */
	private Number value;

	/**
	 * Contructs a number.
	 * 
	 * @param value
	 */
	public Numeral(Number value) {
		
		this.value = value;
		
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
	public Number getValue() {
		
		return value;
		
	}

	@Override
	public Object clone() {
		return new Numeral(value);
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
		if (!(obj instanceof Numeral))
			return false;
		Numeral other = (Numeral) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
