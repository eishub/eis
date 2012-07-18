package eis.iilang;

/** 
 * This interface adheres to the visitor pattern 
 * (@see <a href="http://en.wikipedia.org/wiki/Visitor_pattern">http://en.wikipedia.org/wiki/Visitor_pattern</a>).
 * It should be used whenever an IIL-element is to be transformed into another representation.
 * For each such representation a specialized visitor is expected.
 * @author tristanbehrens
 *
 */
public interface IILVisitor {

	public Object visit(Action element);
	public Object visit(DataContainer element);
	public Object visit(Function element);
	public Object visit(Identifier element);
	public Object visit(IILElement element);
	public Object visit(Numeral element);
	public Object visit(Parameter element);
	public Object visit(ParameterList element);
	public Object visit(Percept element);
	public Object visit(TruthValue element);
	
}
