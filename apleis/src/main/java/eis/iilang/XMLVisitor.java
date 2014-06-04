package eis.iilang;

/**
 * Takes an IILElement and yields its XML-representation.
 * 
 * @author tristanbehrens
 *
 */
public class XMLVisitor implements IILObjectVisitor {

	private static String indent = "  ";
	private static String newline = "\n";

	@Override
	public Object visit(Action element, Object object) {

		String ret = "";
		
		ret += object.toString() + "<action name=\"" + element.name + "\">" + newline;
		for ( Parameter p : element.getParameters() ) {
			ret += object.toString() + indent + "<actionParameter>" + newline;
			ret += p.accept(this,object.toString() + indent + indent);
			ret += object.toString() + indent + "</actionParameter>" + newline;
		}
		ret += object.toString() + "</action>";

		return ret;
	
	}

	@Override
	public Object visit(DataContainer element, Object object) {
		assert false : "Not expected";
		return "";
	}

	@Override
	public Object visit(Function element, Object object) {
	
		String ret = "";
		
		ret += object.toString() + "<function name=\"" + element.getName() + "\">" + newline;
		for ( Parameter p : element.getParameters() ) {
			ret += p.accept(this,object.toString() + indent);
		}
		ret += object.toString() + "</function>"+ newline;

		return ret;

	}

	@Override
	public Object visit(Identifier element, Object object) {

		return object.toString() + "<identifier value=\"" + element.getValue() + "\"/>" + newline;

	}

	@Override
	public Object visit(IILElement element, Object object) {
		assert false : "Not expected";
		return "";
	}

	@Override
	public Object visit(Numeral element, Object object) {

		return object.toString() + "<number value=\"" + element.getValue() + "\"/>" + newline;
	
	}
	
	@Override
	public Object visit(Parameter element, Object object) {
		assert false : "Not expected";
		return "";
	}

	@Override
	public Object visit(ParameterList element, Object object) {
		String ret = "";
		
		ret += object.toString() + "<parameterList>" + newline;
		for ( Parameter p : element ) {
			ret += p.accept(this,object.toString() + indent);
		}
		ret += object.toString() + "</parameterList>" + newline;

		return ret;
	}

	@Override
	public Object visit(Percept element, Object object) {

		String ret = "";
		
		ret += object.toString() + "<percept name=\"" + element.name + "\">" + newline;
		for ( Parameter p : element.getParameters() ) {
			ret += object.toString() + indent + "<perceptParameter>" + newline;
			ret += p.accept(this,object.toString() + indent + indent);
			ret += object.toString() + indent + "</perceptParameter>" + newline;
		}
		ret += object.toString() + "</percept>" + newline;

		return ret;
	
	}

	@Override
	public Object visit(TruthValue element, Object object) {

		return object.toString() + "<truthvalue value=\"" + element.getValue() + "\"/>" + newline;

	}

	/**
	 * Can be used for convenience's sake. Instantiates a visitor and visits the element.
	 * @param element
	 * @return
	 */
	public static String staticVisit(IILElement element) {
		
		IILObjectVisitor visitor = (IILObjectVisitor) new XMLVisitor();
		return (String) element.accept(visitor,"");
		
	}
	
}
