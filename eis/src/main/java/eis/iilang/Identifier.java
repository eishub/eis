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
	/**
	 * 
	 */
	private static final long serialVersionUID = -2932464954431024965L;

	/** The identifier itself. */
	private String value = null;

	/**
	 * Constructs an identifier.
	 * 
	 * @param value
	 *            the identifier
	 */
	public Identifier(String value) {
		// assert Character.isLowerCase(value.charAt(0)) : "Identifier '" +
		// value
		// + "' should start with a lowercase letter";
		// See #3581918
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
		return ((value == null) ? 0 : value.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Identifier)) {
			return false;
		}
		Identifier other = (Identifier) obj;
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
