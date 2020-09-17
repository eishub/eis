package eis.iilang;

/**
 * Takes an IILElement and yields its Prolog-representation.
 */
public class PrologVisitor implements IILObjectVisitor {

	@Override
	public Object visit(final Action element, final Object object) {
		String ret = element.name + "(";
		for (final Parameter p : element.getParameters()) {
			ret += p.accept(this, null);
			if (element.getParameters().indexOf(p) != element.getParameters().size() - 1) {
				ret += ",";
			}
		}
		ret += ")";

		return ret;

	}

	@Override
	public Object visit(final DataContainer element, final Object object) {
		return "UNKNOWN";
	}

	@Override
	public Object visit(final Function element, final Object object) {
		String ret = element.getName() + "(";
		int count = 0;
		for (final Parameter p : element.getParameters()) {
			ret += p.accept(this, null);
			if (count < element.getParameters().size() - 1) {
				ret += ",";
			}
			count++;
		}
		ret += ")";

		return ret;
	}

	@Override
	public Object visit(final Identifier element, final Object object) {
		return element.getValue();
	}

	@Override
	public Object visit(final IILElement element, final Object object) {
		return "UNKNOWN";
	}

	@Override
	public Object visit(final Numeral element, final Object object) {
		return element.getValue();
	}

	@Override
	public Object visit(final Parameter element, final Object object) {
		return "UNKNOWN";
	}

	@Override
	public Object visit(final ParameterList element, final Object object) {
		String ret = "[";
		for (final Parameter p : element) {
			ret += p.accept(this, null);
			if (element.indexOf(p) != element.size() - 1) {
				ret += ",";
			}
		}
		ret += "]";

		return ret;

	}

	@Override
	public Object visit(final Percept element, final Object object) {
		String ret = element.name + "(";
		for (final Parameter p : element.getParameters()) {
			ret += p.accept(this, null);
			if (element.getParameters().indexOf(p) != element.getParameters().size() - 1) {
				ret += ",";
			}
		}
		ret += ")";

		return ret;

	}

	@Override
	public Object visit(final TruthValue element, final Object object) {
		return element.getValue();
	}

	/**
	 * Can be used for convenience's sake. Instantiates a visitor and visits the
	 * element.
	 *
	 * @param element the element to visit
	 * @return result of calling {@link IILElement#accept(IILObjectVisitor, Object)}
	 */
	public static String staticVisit(final IILElement element) {
		final PrologVisitor visitor = new PrologVisitor();
		return (String) element.accept(visitor, "");
	}
}
