package eis.iilang;

import java.util.List;

/**
 * A percept. A percept consists of a name and some parameters.
 * 
 * @author tristanbehrens
 *
 */
public class Percept extends DataContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5929676291607949546L;

	/**
	 * Constructs a percept from a name.
	 * 
	 * @param name
	 *            the name of the percept (no arguments)
	 */
	public Percept(String name) {
		super(name);
	}

	/**
	 * Contructs a percept from a name and an array of parameters.
	 * 
	 * @param name
	 *            the name.
	 * @param parameters
	 *            the parameters.
	 */
	public Percept(String name, Parameter... parameters) {
		super(name, parameters);
	}

	/**
	 * Contructs a percept from a name and an array of parameters.
	 * 
	 * @param name
	 *            the name.
	 * @param parameters
	 *            the parameters.
	 */
	public Percept(String name, List<Parameter> parameters) {
		super(name, parameters);
	}

	@Override
	protected String toXML(int depth) {
		String xml = "";

		xml += indent(depth) + "<percept name=\"" + name + "\">" + "\n";

		for (Parameter p : params) {
			xml += indent(depth + 1) + "<perceptParameter>" + "\n";
			xml += p.toXML(depth + 2);
			xml += indent(depth + 1) + "</perceptParameter>" + "\n";
		}

		xml += indent(depth) + "</percept>" + "\n";

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

	/*
	 * public String toProlog() {
	 * 
	 * String ret = "percept";
	 * 
	 * ret+="(";
	 * 
	 * ret+=name;
	 * 
	 * for( Parameter p : params ) ret += "," + p.toProlog();
	 * 
	 * ret+=")";
	 * 
	 * return ret;
	 * 
	 * }
	 */

	@Override
	public Object clone() {
		return new Percept(this.name, this.getClonedParameters());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;

		if (!(obj instanceof Percept))
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
