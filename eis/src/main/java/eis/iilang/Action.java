package eis.iilang;

import java.util.List;

/**
 * An action that can be performed by an agent through its associated
 * entity/ies. An action consists of a name and a sequence of parameters.
 */
public class Action extends DataContainer {
	private static final long serialVersionUID = 2483470223360080046L;

	/**
	 * Constructs an action from a name.
	 *
	 * @param name the name for the action
	 */
	public Action(final String name) {
		super(name);
	}

	/**
	 * Constructs an action.
	 *
	 * @param name       the name for the action
	 * @param parameters action parameters
	 */
	public Action(final String name, final Parameter... parameters) {
		super(name, parameters);
	}

	/**
	 * Constructs an action.
	 *
	 * @param name       the name for the action
	 * @param parameters action parameters
	 */
	public Action(final String name, final List<Parameter> parameters) {
		super(name, parameters);
	}

	@Override
	protected String toXML(final int depth) {
		String xml = indent(depth) + "<action name=\"" + this.name + "\">" + "\n";

		for (final Parameter p : this.params) {
			xml += indent(depth + 1) + "<actionParameter>" + "\n";
			xml += p.toXML(depth + 2);
			xml += indent(depth + 1) + "</actionParameter>" + "\n";
		}

		xml += indent(depth) + "</action>" + "\n";

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
		final Action ret = new Action(this.name, getClonedParameters());
		ret.setSource(this.source);

		return ret;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (!(obj instanceof Action)) {
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