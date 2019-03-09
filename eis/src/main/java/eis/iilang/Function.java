package eis.iilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a function over parameters. It consits of a name and a list of
 * parameters.
 * 
 * @author tristanbehrens
 */
public class Function extends Parameter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4047099586973739090L;

	/** The name of the function. */
	private String name = null;

	/** A list of parameters. */
	private List<Parameter> params = null;

	/**
	 * Instantiates a function.
	 * 
	 * @param name
	 *            the name of the function.
	 * @param parameters
	 *            the parameters.
	 */
	public Function(String name, Parameter... parameters) {
		this(name, new ArrayList<>(Arrays.asList(parameters)));
	}

	/**
	 * Instantiates a function.
	 * 
	 * @param name
	 *            the name of the function.
	 * @param parameters
	 *            the parameters.
	 */
	public Function(String name, List<Parameter> parameters) {
		setName(name);
		this.params = parameters;
	}

	/**
	 * Returns the name of the function.
	 * 
	 * @return the name of the function.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the function
	 * 
	 * @param name
	 *            the name of the function
	 */
	public void setName(String name) {
		assert Character.isLowerCase(name.charAt(0)) : name + " should start with a lowercase letter";
		this.name = name;
	}

	/**
	 * Returns the parameters of the function.
	 * 
	 * @return the parameters of the function.
	 */
	public List<Parameter> getParameters() {
		return params;
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters
	 *            the new parameters
	 */
	public void setParameters(List<Parameter> parameters) {
		this.params = parameters;
	}

	/**
	 * Returns the parameters. Returns the parameters as a clone.
	 * 
	 * @return the parameters of the function
	 */
	public List<Parameter> getClonedParameters() {
		List<Parameter> ret = new ArrayList<>(params.size());

		for (Parameter p : params) {
			ret.add((Parameter) p.clone());
		}

		return ret;

	}

	@Override
	protected String toXML(int depth) {
		String xml = "";

		xml += indent(depth) + "<function name=\"" + name + "\">" + "\n";

		for (Parameter p : params) {
			xml += p.toXML(depth + 1);
		}

		xml += indent(depth) + "</function>" + "\n";

		return xml;
	}

	@Override
	public String toProlog() {
		String ret = name;

		if (params.size() > 0) {
			ret += "(";

			ret += params.get(0).toProlog();

			for (int a = 1; a < params.size(); a++)
				ret += "," + params.get(a).toProlog();

			ret += ")";
		}

		return ret;
	}

	@Override
	public Object clone() {
		return new Function(this.name, this.getClonedParameters());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Function))
			return false;
		Function other = (Function) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
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
