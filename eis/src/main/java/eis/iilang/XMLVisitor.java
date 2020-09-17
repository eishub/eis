package eis.iilang;

/**
 * Takes an IILElement and yields its XML-representation.
 */
public class XMLVisitor implements IILObjectVisitor {
	private static String indent = "  ";
	private static String newline = "\n";

	@Override
	public Object visit(final Action element, final Object object) {
		String ret = object + "<action name=\"" + element.name + "\">" + newline;
		for (final Parameter p : element.getParameters()) {
			ret += object + indent + "<actionParameter>" + newline;
			ret += p.accept(this, object + indent + indent);
			ret += object + indent + "</actionParameter>" + newline;
		}
		ret += object + "</action>";

		return ret;
	}

	@Override
	public Object visit(final DataContainer element, final Object object) {
		assert false : "Not expected";
		return "";
	}

	@Override
	public Object visit(final Function element, final Object object) {
		String ret = object + "<function name=\"" + element.getName() + "\">" + newline;
		for (final Parameter p : element.getParameters()) {
			ret += p.accept(this, object + indent);
		}
		ret += object + "</function>" + newline;

		return ret;
	}

	@Override
	public Object visit(final Identifier element, final Object object) {
		return object + "<identifier value=\"" + element.getValue() + "\"/>" + newline;
	}

	@Override
	public Object visit(final IILElement element, final Object object) {
		assert false : "Not expected";
		return "";
	}

	@Override
	public Object visit(final Numeral element, final Object object) {
		return object + "<number value=\"" + element.getValue() + "\"/>" + newline;
	}

	@Override
	public Object visit(final Parameter element, final Object object) {
		assert false : "Not expected";
		return "";
	}

	@Override
	public Object visit(final ParameterList element, final Object object) {
		String ret = object + "<parameterList>" + newline;
		for (final Parameter p : element) {
			ret += p.accept(this, object + indent);
		}
		ret += object + "</parameterList>" + newline;

		return ret;
	}

	@Override
	public Object visit(final Percept element, final Object object) {
		String ret = object + "<percept name=\"" + element.name + "\">" + newline;
		for (final Parameter p : element.getParameters()) {
			ret += object + indent + "<perceptParameter>" + newline;
			ret += p.accept(this, object + indent + indent);
			ret += object + indent + "</perceptParameter>" + newline;
		}
		ret += object + "</percept>" + newline;

		return ret;
	}

	@Override
	public Object visit(final TruthValue element, final Object object) {
		return object + "<truthvalue value=\"" + element.getValue() + "\"/>" + newline;
	}

	/**
	 * Can be used for convenience's sake. Instantiates a visitor and visits the
	 * element.
	 *
	 * @param element the element to visit
	 * @return result of calling {@link IILElement#accept(IILVisitor)}
	 */
	public static String staticVisit(final IILElement element) {
		final IILObjectVisitor visitor = new XMLVisitor();
		return (String) element.accept(visitor, "");
	}
}
