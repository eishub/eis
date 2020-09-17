package eis.iilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A list of parameters.
 */
public class ParameterList extends Parameter implements Iterable<Parameter> {
	private static final long serialVersionUID = 5330751271570276475L;

	/** The list of parameters. */
	private List<Parameter> list = null;

	/**
	 * Constructs a list of parameters from an array.
	 *
	 * @param parameters the parameters for this list
	 */
	public ParameterList(final Parameter... parameters) {
		this(new ArrayList<>(Arrays.asList(parameters)));
	}

	/**
	 * Constructs a list of parameters from a collection.
	 *
	 * @param parameters the parameters for this list
	 */
	public ParameterList(final List<Parameter> parameters) {
		this.list = parameters;
	}

	/**
	 * Returns an iterator.
	 */
	@Override
	public Iterator<Parameter> iterator() {
		return this.list.iterator();
	}

	/**
	 * @param p the element needed
	 * @return the index of an element
	 */
	public int indexOf(final Parameter p) {
		return this.list.indexOf(p);
	}

	/**
	 *
	 * @return the size of the list
	 */
	public int size() {
		return this.list.size();
	}

	/**
	 * get the ith parameter of the list.
	 *
	 * @param i element number, starting at 0.
	 * @return ith parameter in the list.
	 */
	public Parameter get(final int i) {
		return this.list.get(i);
	}

	/**
	 * Checks for emptyness.
	 *
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	protected String toXML(final int depth) {
		String xml = indent(depth) + "<parameterList>" + "\n";
		for (final Parameter p : this.list) {
			xml += p.toXML(depth + 1);
		}
		xml += indent(depth) + "</parameterList>" + "\n";

		return xml;
	}

	public void add(final Parameter parameter) {
		this.list.add(parameter);
	}

	@Override
	public String toProlog() {
		String ret = "[";
		if (this.list.isEmpty() == false) {
			ret += this.list.get(0).toProlog();
			for (int a = 1; a < this.list.size(); a++) {
				ret += "," + this.list.get(a).toProlog();
			}
		}
		ret += "]";

		return ret;
	}

	@Override
	public Object clone() {
		final ParameterList ret = new ParameterList();
		for (final Parameter p : this.list) {
			ret.add((Parameter) p.clone());
		}

		return ret;
	}

	@Override
	public int hashCode() {
		return ((this.list == null) ? 0 : this.list.hashCode());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof ParameterList)) {
			return false;
		}

		final ParameterList other = (ParameterList) obj;
		if (this.list == null) {
			if (other.list != null) {
				return false;
			}
		} else if (!this.list.equals(other.list)) {
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
