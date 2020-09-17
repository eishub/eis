package eis.iilang;

import java.io.Serializable;

/**
 * Represents an element of the <i>Interface Immediate Language</i>.
 */
public abstract class IILElement implements Serializable, Cloneable {
	private static final long serialVersionUID = 3469843696191307769L;
	public static boolean toProlog = false;

	/**
	 * Returns a string-representation.
	 *
	 * @deprecated use IIL-Visitors instead.
	 */
	@Override
	@Deprecated
	public final String toString() {
		if (toProlog) {
			return toProlog();
		} else {
			return toXML();
		}
	}

	/**
	 * Returns an XML-representation encoded in a string.
	 *
	 * @param depth is the depth of indendation.
	 * @return an XML-string.
	 * @deprecated use IIL-Visitors instead.
	 */
	@Deprecated
	protected abstract String toXML(int depth);

	/**
	 * Returns an XML-string including the header.
	 *
	 * @return an XML-string including the header.
	 * @deprecated use IIL-Visitors instead.
	 */
	@Deprecated
	public final String toXMLWithHeader() {
		final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
		return header + toXML();
	}

	/**
	 * Returns an XML-representation encoded in a string.
	 *
	 * @return an XML-string.
	 * @deprecated use IIL-Visitors instead.
	 */
	@Deprecated
	public final String toXML() {
		return toXML(0);
	}

	/**
	 * Returns a Prolog-representation encoded in a string.
	 *
	 * @return a Prolog-string
	 * @deprecated use IIL-Visitors instead.
	 */
	@Deprecated
	public abstract String toProlog();

	/**
	 * Returns an indentation.
	 *
	 * @param depth is the depth of the indentation.
	 * @return the string-representation with indentation
	 */
	protected String indent(final int depth) {
		String ret = "";
		for (int a = 0; a < depth; a++) {
			ret += "  ";
		}

		return ret;
	}

	/**
	 * Creates a clone of the iilang-element.
	 */
	@Override
	public abstract Object clone();

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object obj);

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();

	/**
	 * Is called by a visitor.
	 *
	 * @param visitor the visitor to accept
	 * @see eis.iilang.IILVisitor
	 */
	public abstract void accept(IILVisitor visitor);

	/**
	 * Is called by an object visitor.
	 *
	 * @param visitor the visitor to accept
	 * @param object  object to visit ?
	 * @return unknown
	 * @see eis.iilang.IILObjectVisitor
	 */
	public abstract Object accept(IILObjectVisitor visitor, Object object);
}
