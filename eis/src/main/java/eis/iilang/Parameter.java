package eis.iilang;

/**
 * An abstract class for parameters. Parameters are identifiers, numerals,
 * functions, and parameter-lists.
 */
public abstract class Parameter extends IILElement {
	private static final long serialVersionUID = -5689680197270823728L;

	@Override
	abstract public boolean equals(Object obj);

	@Override
	abstract public int hashCode();

	@Override
	abstract public Object clone();
}
