package eis.iilang;

/**
 * Encapsulates a number.
 * 
 * @author tristanbehrens
 * 
 */
public class Numeral extends Parameter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7361584275569246985L;
	
	/** The value of the numner. */
	private Number value;

	/**
	 * Contructs a number.
	 * 
	 * @param value
	 *            new number
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
		return ((value == null) ? 0 : value.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Numeral)) {
			return false;
		}
		Numeral other = (Numeral) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public Object accept(IILObjectVisitor visitor, Object object) {
		return visitor.visit(this, object);
	}

	@Override
	public void accept(IILVisitor visitor) {
		visitor.visit(this);
	}
}
