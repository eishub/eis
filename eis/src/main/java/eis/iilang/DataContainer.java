package eis.iilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A superclass for actions, events, et cetera. Consists of a name and a
 * sequence of parameters. Additionally it stores a time-stamp that indicates
 * the time of creation, and also a string that indicates the source.
 * 
 * @author tristanbehrens
 * 
 */
public abstract class DataContainer extends IILElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1139628940335051005L;

	/** The name of the DataContainer. */
	protected String name = null;

	/** A list of parameters. */
	protected List<Parameter> params = null;

	/**
	 * Contructs an DataContainer.
	 * 
	 * @param name
	 *            the name of this datacontainer
	 * @param parameters
	 *            the parameter list
	 */
	public DataContainer(String name, Parameter... parameters) {
		this(name, new ArrayList<>(Arrays.asList(parameters)));
	}

	/**
	 * Contructs an DataContainer.
	 * 
	 * @param name
	 *            the name of this datacontainer
	 * @param parameters
	 *            the parameter list
	 */
	public DataContainer(String name, List<Parameter> parameters) {
		this.name = name;
		this.params = parameters;
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name of the data-container
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the parameters.
	 * 
	 * @return the parameters of the data-container
	 */
	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(params);
	}

	/**
	 * Returns the parameters. Returns the parameters as a clone.
	 * 
	 * @return the parameters of the data-container
	 */
	public List<Parameter> getClonedParameters() {
		List<Parameter> ret = new ArrayList<>(params.size());
		for (Parameter p : params) {
			ret.add((Parameter) p.clone());
		}

		return ret;
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
		if (this == obj) {
			return true;
		}
		if (obj == null|| !(obj instanceof DataContainer)) {
			return false;
		}
		DataContainer other = (DataContainer) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (params == null) {
			if (other.params != null) {
				return false;
			}
		} else if (!params.equals(other.params)) {
			return false;
		}
		return true;
	}
}
