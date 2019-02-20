package eis.iilang;

import java.util.List;

/**
 * An action that can be performed by an agent through its associated
 * entity/ies. An action consists of a name and a sequence of parameters.
 * 
 * @author tristanbehrens
 */
public class Action extends DataContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2483470223360080046L;

	/**
	 * Constructs an action from a name.
	 * 
	 * @param name
	 *            the name for the action
	 */
	public Action(String name) {
		super(name);
	}

	/**
	 * Constructs an action.
	 * 
	 * @param name
	 *            the name for the action
	 * @param parameters
	 *            action parameters
	 */
	public Action(String name, Parameter... parameters) {
		super(name, parameters);
	}

	/**
	 * Constructs an action.
	 * 
	 * @param name
	 *            the name for the action
	 * @param parameters
	 *            action parameters
	 */
	public Action(String name, List<Parameter> parameters) {
		super(name, parameters);
	}

	@Override
	protected String toXML(int depth) {
		String xml = "";

		xml += indent(depth) + "<action name=\"" + name + "\">" + "\n";

		for (Parameter p : params) {
			xml += indent(depth + 1) + "<actionParameter>" + "\n";
			xml += p.toXML(depth + 2);
			xml += indent(depth + 1) + "</actionParameter>" + "\n";
		}

		xml += indent(depth) + "</action>" + "\n";

		return xml;
	}

	@Override
	public String toProlog() {
		String ret = "";

		ret += name;

		if (params.isEmpty() == false) {
			ret += "(";

			ret += params.get(0).toProlog();

			for (int a = 1; a < params.size(); a++) {
				Parameter p = params.get(a);
				ret += "," + p.toProlog();
			}

			ret += ")";
		}

		return ret;
	}

	@Override
	public Object clone() {
		Action ret = new Action(this.name, this.getClonedParameters());

		ret.setSource(this.source);

		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;

		if (!(obj instanceof Action))
			return false;

		return super.equals(obj);
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