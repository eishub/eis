package eis.iilang;

/**
 * This interface adheres to the visitor pattern (@see <a href=
 * "http://en.wikipedia.org/wiki/Visitor_pattern">http://en.wikipedia.org/wiki/Visitor_pattern</a>).
 * It should be used whenever an IIL-element is to be transformed into another
 * representation. For each such representation a specialized visitor is
 * expected.
 */
public interface IILVisitor {
	Object visit(Action element);

	Object visit(DataContainer element);

	Object visit(Function element);

	Object visit(Identifier element);

	Object visit(IILElement element);

	Object visit(Numeral element);

	Object visit(Parameter element);

	Object visit(ParameterList element);

	Object visit(Percept element);

	Object visit(TruthValue element);
}
