package eis.iilang;

/** 
 * This interface adheres to the visitor pattern 
 * (@see <a href="http://en.wikipedia.org/wiki/Visitor_pattern">http://en.wikipedia.org/wiki/Visitor_pattern</a>).
 * It should be used whenever an IIL-element is to be transformed into another representation.
 * For each such representation a specialized visitor is expected.
 * 
 * Note that this is an object-visitor. That is, each object can take an arbitrary
 * object as its second parameter and can return another object.
 * 
 * @author tristanbehrens
 *
 */
public interface IILObjectVisitor {

	public Object visit(Action element, Object object);
	public Object visit(DataContainer element, Object object);
	public Object visit(Function element, Object object);
	public Object visit(Identifier element, Object object);
	public Object visit(IILElement element, Object object);
	public Object visit(Numeral element, Object object);
	public Object visit(Parameter element, Object object);
	public Object visit(ParameterList element, Object object);
	public Object visit(Percept element, Object object);
	public Object visit(TruthValue element, Object object);

}
