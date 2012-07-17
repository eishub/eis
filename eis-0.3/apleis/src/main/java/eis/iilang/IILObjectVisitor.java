package eis.iilang;

/**
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
