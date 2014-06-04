package eis.iilang;

/**
 * Takes an IILElement and yields its Prolog-representation.
 * 
 * @author tristanbehrens
 *
 */
public class PrologVisitor implements IILObjectVisitor {

	@Override
	public Object visit(Action element, Object object) {
		
		String ret = "";
		
		ret += element.name + "(";
		for ( Parameter p : element.getParameters() ) {
			ret += p.accept(this,null);
			if ( element.getParameters().indexOf(p) != element.getParameters().size() - 1 )
				ret += ",";
		}
		ret += ")";

		return ret;

	}

	@Override
	public Object visit(DataContainer element, Object object) {
		return "UNKNOWN";
	}

	@Override
	public Object visit(Function element, Object object) {

		String ret = "";
		
		ret += element.getName() + "(";
		int count = 0;
		for ( Parameter p : element.getParameters() ) {
			ret += p.accept(this,null);
			//if ( element.getParameters().indexOf(p) != element.getParameters().size() - 1 )
			//	ret += ",";
			if ( count < element.getParameters().size() - 1 ) ret += ",";
			count ++;
		}
		ret += ")";

		return ret;
	}

	@Override
	public Object visit(Identifier element, Object object) {

		return element.getValue();
	
	}

	@Override
	public Object visit(IILElement element, Object object) {
		return "UNKNOWN";
	}

	@Override
	public Object visit(Numeral element, Object object) {

		return element.getValue();

	}

	@Override
	public Object visit(Parameter element, Object object) {
		return "UNKNOWN";
	}

	@Override
	public Object visit(ParameterList element, Object object) {

		String ret = "";
		
		ret += "[";
		for ( Parameter p : element ) {
			ret += p.accept(this,null);
			if ( element.indexOf(p) != element.size() - 1 )
				ret += ",";
		}
		ret += "]";

		return ret;
	
	}

	@Override
	public Object visit(Percept element, Object object) {

		String ret = "";
		
		ret += element.name + "(";
		for ( Parameter p : element.getParameters() ) {
			ret += p.accept(this,null);
			if ( element.getParameters().indexOf(p) != element.getParameters().size() - 1 )
				ret += ",";
		}
		ret += ")";

		return ret;
	
	}

	@Override
	public Object visit(TruthValue element, Object object) {

		return element.getValue();

	}

	/**
	 * Can be used for convenience's sake. Instantiates a visitor and visits the element.
	 * @param element
	 * @return
	 */
	public static String staticVisit(IILElement element) {
		
		PrologVisitor visitor = new PrologVisitor();
		return (String) element.accept(visitor,"");
		
	}
}
