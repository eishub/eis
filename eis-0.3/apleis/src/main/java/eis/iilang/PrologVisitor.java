package eis.iilang;

public class PrologVisitor implements IILObjectVisitor {

	@Override
	public Object visit(Action element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(DataContainer element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(Function element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(Identifier element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(IILElement element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(Numeral element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(Parameter element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(ParameterList element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(Percept element, Object object) {
		assert false : "Implement!";
		return null;
	}

	@Override
	public Object visit(TruthValue element, Object object) {
		assert false : "Implement!";
		return null;
	}

	public static String staticVisit(IILElement element) {
		
		String ret = "";
		
		PrologVisitor visitor = new PrologVisitor();
		ret += visitor.visit(element,null);
		
		return ret;
		
	}
}
