package eis.iilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a function over parameters. It consits of a name and a list of
 * parameters.
 */
public class Function extends Parameter {
	private static final long serialVersionUID = -4047099586973739090L;

	/** The name of the function. */
	private String name = null;
	/** A list of parameters. */
	private List<Parameter> params = null;

	/**
	 * Instantiates a function.
	 *
	 * @param name       the name of the function.
	 * @param parameters the parameters.
	 */
	public Function(final String name, final Parameter... parameters) {
		this(name, new ArrayList<>(Arrays.asList(parameters)));
	}

	/**
	 * Instantiates a function.
	 *
	 * @param name       the name of the function.
	 * @param parameters the parameters.
	 */
	public Function(final String name, final List<Parameter> parameters) {
		setName(name);
		this.params = parameters;
	}

	/**
	 * Returns the name of the function.
	 *
	 * @return the name of the function.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the function
	 *
	 * @param name the name of the function
	 */
	public void setName(final String name) {
		assert Character.isLowerCase(name.charAt(0)) : name + " should start with a lowercase letter";
		this.name = name;
	}

	/**
	 * Returns the parameters of the function.
	 *
	 * @return the parameters of the function.
	 */
	public List<Parameter> getParameters() {
		return this.params;
	}

	/**
	 * Sets the parameters.
	 *
	 * @param parameters the new parameters
	 */
	public void setParameters(final List<Parameter> parameters) {
		this.params = parameters;
	}

	/**
	 * Returns the parameters. Returns the parameters as a clone.
	 *
	 * @return the parameters of the function
	 */
	public List<Parameter> getClonedParameters() {
		final List<Parameter> ret = new ArrayList<>(this.params.size());
		for (final Parameter p : this.params) {
			ret.add((Parameter) p.clone());
		}

		return ret;

	}

	@Override
	protected String toXML(final int depth) {
		String xml = indent(depth) + "<function name=\"" + this.name + "\">" + "\n";

		for (final Parameter p : this.params) {
			xml += p.toXML(depth + 1);
		}

		xml += indent(depth) + "</function>" + "\n";

		return xml;
	}

	@Override
	public String toProlog() {
		String ret = this.name;

		if (this.params.size() > 0) {
			ret += "(";
			ret += this.params.get(0).toProlog();
			for (int a = 1; a < this.params.size(); a++) {
				ret += "," + this.params.get(a).toProlog();
			}
			ret += ")";
		}

		return ret;
	}

	@Override
	public Object clone() {
		return new Function(this.name, getClonedParameters());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.params == null) ? 0 : this.params.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (!(obj instanceof Function)) {
			return false;
		}

		final Function other = (Function) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}

		if (this.params == null) {
			if (other.params != null) {
				return false;
			}
		} else if (!this.params.equals(other.params)) {
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
