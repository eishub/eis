package eis.iilang;

/**
 * An identifier is an arbitrary string.
 */
public class Identifier extends Parameter {
	private static final long serialVersionUID = -2932464954431024965L;
	/** The identifier itself. */
	private String value = null;

	/**
	 * Constructs an identifier.
	 *
	 * @param value the identifier
	 */
	public Identifier(final String value) {
		this.value = value;

	}

	/**
	 * Returns the identifier.
	 *
	 * @return the identifier as a string
	 */
	public String getValue() {
		return this.value;
	}

	@Override
	protected String toXML(final int depth) {
		return indent(depth) + "<identifier value=\"" + this.value + "\"/>" + "\n";
	}

	@Override
	public String toProlog() {
		return this.value;
	}

	@Override
	public Object clone() {
		return new Identifier(this.value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof Identifier)) {
			return false;
		}

		final Identifier other = (Identifier) obj;
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}

		return true;
	}

	@Override
	public Object accept(final IILObjectVisitor visitor, final Object object) {
		return visitor.visit(this, object);
	}

	@Override
	public void accept(final IILVisitor visitor) {
		visitor.visit(this);
	}
}
