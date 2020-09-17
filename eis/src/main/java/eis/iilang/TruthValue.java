package eis.iilang;

/**
 * Encapsulates a truth value. That is either true or false or anything else
 * (multi-valued logic).
 */
public class TruthValue extends Parameter {
	private static final long serialVersionUID = 377656049621872537L;

	/** The string value of the boolean. */
	private String value;

	/**
	 * Constructs a truth-value.
	 *
	 * @param value new value for this
	 */
	public TruthValue(final String value) {
		this.value = value;
	}

	/**
	 * Constructs a truth-value from a boolean one.
	 *
	 * @param bool new value for this
	 */
	public TruthValue(final boolean bool) {
		if (bool == true) {
			this.value = "true";
		} else {
			this.value = "false";
		}
	}

	@Override
	protected String toXML(final int depth) {
		return indent(depth) + "<truthvalue value=\"" + this.value + "\"/>" + "\n";
	}

	@Override
	public String toProlog() {
		return this.value;
	}

	/**
	 * Returns the value.
	 *
	 * @return the value of this
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Gets the value as a boolean. Not that this only works iff the value itself is
	 * either true or false.
	 *
	 * @return the value of this
	 */
	public boolean getBooleanValue() {
		if (this.value.equals("true")) {
			return true;
		} else if (this.value.equals("false")) {
			return false;
		} else {
			throw new AssertionError(this.value + "cannot be converted to boolean");
		}
	}

	@Override
	public Object clone() {
		return new TruthValue(this.value);
	}

	@Override
	public int hashCode() {
		return ((this.value == null) ? 0 : this.value.hashCode());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof TruthValue)) {
			return false;
		}

		final TruthValue other = (TruthValue) obj;
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
