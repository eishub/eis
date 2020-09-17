package eis.iilang;

/**
 * This interface adheres to the visitor pattern (@see <a href=
 * "http://en.wikipedia.org/wiki/Visitor_pattern">http://en.wikipedia.org/wiki/Visitor_pattern</a>).
 * It should be used whenever an IIL-element is to be transformed into another
 * representation. For each such representation a specialized visitor is
 * expected.
 *
 * Note that this is an object-visitor. That is, each object can take an
 * arbitrary object as its second parameter and can return another object.
 */
public interface IILObjectVisitor {
	Object visit(Action element, Object object);

	Object visit(DataContainer element, Object object);

	Object visit(Function element, Object object);

	Object visit(Identifier element, Object object);

	Object visit(IILElement element, Object object);

	Object visit(Numeral element, Object object);

	Object visit(Parameter element, Object object);

	Object visit(ParameterList element, Object object);

	Object visit(Percept element, Object object);

	Object visit(TruthValue element, Object object);
}
