package eis.iilang;

import java.util.List;

/**
 * A percept. A percept consists of a name and some parameters.
 */
public class Percept extends DataContainer {
	private static final long serialVersionUID = -5929676291607949546L;

	/**
	 * Constructs a percept from a name.
	 *
	 * @param name the name of the percept (no arguments)
	 */
	public Percept(final String name) {
		super(name);
	}

	/**
	 * Constructs a percept from a name and an array of parameters.
	 *
	 * @param name       the name.
	 * @param parameters the parameters.
	 */
	public Percept(final String name, final Parameter... parameters) {
		super(name, parameters);
	}

	/**
	 * Constructs a percept from a name and an array of parameters.
	 *
	 * @param name       the name.
	 * @param parameters the parameters.
	 */
	public Percept(final String name, final List<Parameter> parameters) {
		super(name, parameters);
	}

	@Override
	protected String toXML(final int depth) {
		String xml = indent(depth) + "<percept name=\"" + this.name + "\">" + "\n";
		for (final Parameter p : this.params) {
			xml += indent(depth + 1) + "<perceptParameter>" + "\n";
			xml += p.toXML(depth + 2);
			xml += indent(depth + 1) + "</perceptParameter>" + "\n";
		}
		xml += indent(depth) + "</percept>" + "\n";

		return xml;
	}

	@Override
	public String toProlog() {
		String ret = this.name;

		if (this.params.isEmpty() == false) {
			ret += "(";
			ret += this.params.get(0).toProlog();
			for (int a = 1; a < this.params.size(); a++) {
				final Parameter p = this.params.get(a);
				ret += "," + p.toProlog();
			}
			ret += ")";
		}

		return ret;
	}

	@Override
	public Object clone() {
		final Percept ret = new Percept(this.name, getClonedParameters());
		ret.source = this.source;

		return ret;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (!(obj instanceof Percept)) {
			return false;
		} else {
			return super.equals(obj);
		}
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
