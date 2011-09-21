package eis.iilang;

/**
 * An abstract class for parameters. Parameters are identifiers, numerals, functions,
 * and parameter-lists.
 * 
 * @author tristanbehrens
 *
 */
public abstract class Parameter extends IILElement {

	abstract public boolean equals(Object obj);
	abstract public int hashCode();
	abstract public Object clone();
	
}
