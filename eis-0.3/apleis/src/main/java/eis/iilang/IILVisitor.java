package eis.iilang;

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
