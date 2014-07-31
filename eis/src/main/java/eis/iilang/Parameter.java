package eis.iilang;

/**
 * An abstract class for parameters. Parameters are identifiers, numerals,
 * functions, and parameter-lists.
 * 
 * @author tristanbehrens
 * 
 */
public abstract class Parameter extends IILElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5689680197270823728L;

	abstract public boolean equals(Object obj);

	abstract public int hashCode();

	abstract public Object clone();

}
