package eis.iilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A list of parameters.
 * 
 * @author tristanbehrens
 * 
 */
public class ParameterList extends Parameter implements Iterable<Parameter> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5330751271570276475L;
	/** The list of parameters. */
	private List<Parameter> list = null;

	/**
	 * Constructs an empty list of parameters.
	 */
	public ParameterList() {
		this.list = new ArrayList<>(0);
	}

	/**
	 * Contructs a list of parameters from an array.
	 * 
	 * @param parameters
	 *            the parameters for this list
	 */
	public ParameterList(Parameter... parameters) {
		this.list = Arrays.asList(parameters);
	}

	/**
	 * Constructs a list of parameters from a collection.
	 * 
	 * @param parameters
	 *            the parameters for this list
	 */
	public ParameterList(List<Parameter> parameters) {
		this.list = parameters;
	}

	/**
	 * Returns an iterator.
	 */
	public Iterator<Parameter> iterator() {
		return list.iterator();
	}

	/**
	 * @param p
	 *            the element needed
	 * @return the index of an element
	 */
	public int indexOf(Parameter p) {
		return list.indexOf(p);
	}

	/**
	 * 
	 * @return the size of the list
	 */
	public int size() {
		return list.size();
	}

	/**
	 * get the ith parameter of the list.
	 * 
	 * @param i
	 *            element number, starting at 0.
	 * @return ith parameter in the list.
	 */
	public Parameter get(int i) {
		return list.get(i);
	}

	/**
	 * Checks for emptyness.
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	protected String toXML(int depth) {
		String xml = "";

		xml += indent(depth) + "<parameterList>" + "\n";

		for (Parameter p : list) {
			xml += p.toXML(depth + 1);
		}

		xml += indent(depth) + "</parameterList>" + "\n";

		return xml;
	}

	public void add(Parameter parameter) {
		list.add(parameter);
	}

	@Override
	public String toProlog() {
		String ret = "";

		ret += "[";
		if (list.isEmpty() == false) {
			ret += list.get(0).toProlog();

			for (int a = 1; a < list.size(); a++)
				ret += "," + list.get(a).toProlog();
		}
		ret += "]";

		return ret;
	}

	@Override
	public Object clone() {
		ParameterList ret = new ParameterList();

		for (Parameter p : list) {
			ret.add((Parameter) p.clone());
		}

		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ParameterList))
			return false;
		ParameterList other = (ParameterList) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
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
