package eis.iilang;

/**
 * Encapsulates a number.
 */
public class Numeral extends Parameter {
	private static final long serialVersionUID = 7361584275569246985L;

	/** The value of the number. */
	private final Number value;

	/**
	 * Constructs a number.
	 *
	 * @param value new number
	 */
	public Numeral(final Number value) {
		this.value = value;
	}

	@Override
	protected String toXML(final int depth) {
		return indent(depth) + "<number value=\"" + this.value + "\"/>" + "\n";

	}

	@Override
	public String toProlog() {
		return this.value.toString();
	}

	public Number getValue() {
		return this.value;
	}

	@Override
	public Object clone() {
		return new Numeral(this.value);
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
		} else if (!(obj instanceof Numeral)) {
			return false;
		}

		final Numeral other = (Numeral) obj;
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
