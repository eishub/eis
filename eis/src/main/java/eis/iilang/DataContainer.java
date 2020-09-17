package eis.iilang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A superclass for actions, events, et cetera. Consists of a name and a
 * sequence of parameters. Additionally it stores a time-stamp that indicates
 * the time of creation, and also a string that indicates the source.
 */
public abstract class DataContainer extends IILElement {
	private static final long serialVersionUID = 1139628940335051005L;

	/** The name of the DataContainer. */
	protected String name = null;
	/** A list of parameters. */
	protected List<Parameter> params = null;

	/**
	 * Constructs a DataContainer.
	 *
	 * @param name       the name of this datacontainer
	 * @param parameters the parameter list
	 */
	public DataContainer(final String name, final Parameter... parameters) {
		this(name, new ArrayList<>(Arrays.asList(parameters)));
	}

	/**
	 * Constructs a DataContainer.
	 *
	 * @param name       the name of this datacontainer
	 * @param parameters the parameter list
	 */
	public DataContainer(final String name, final List<Parameter> parameters) {
		this.name = name;
		this.params = parameters;
	}

	/**
	 * Returns the name.
	 *
	 * @return the name of the data-container
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the parameters.
	 *
	 * @return the parameters of the data-container
	 */
	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(this.params);
	}

	/**
	 * Returns the parameters. Returns the parameters as a clone.
	 *
	 * @return the parameters of the data-container
	 */
	public List<Parameter> getClonedParameters() {
		final List<Parameter> ret = new ArrayList<>(this.params.size());
		for (final Parameter p : this.params) {
			ret.add((Parameter) p.clone());
		}

		return ret;
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
		} else if (obj == null || !(obj instanceof DataContainer)) {
			return false;
		}

		final DataContainer other = (DataContainer) obj;
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
}
