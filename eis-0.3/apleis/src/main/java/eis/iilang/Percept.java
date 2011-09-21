package eis.iilang;

import java.util.LinkedList;

/**
 * A percept.
 * A percept consists of a name and some parameters.
 * 
 * @author tristanbehrens
 *
 */
public class Percept extends DataContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5929676291607949546L;

	/**
	 * Constructs a percept from a name.
	 * 
	 * @param name
	 */
	public Percept(String name) {
		super(name);
	}
	
	/** 
	 * Contructs a percept from a name and an array of parameters.
	 * 
	 * @param name the name.
	 * @param parameters the parameters.
	 */
	public Percept(String name, Parameter...parameters) {
		super(name, parameters);
	}

	/** 
	 * Contructs a percept from a name and an array of parameters.
	 * 
	 * @param name the name.
	 * @param parameters the parameters.
	 */
	public Percept(String name, LinkedList<Parameter> parameters) {
		super(name, parameters);
	}

	@Override
	protected String toXML(int depth) {

		String xml = "";
		
		xml += indent(depth) + "<percept name=\"" + name + "\">" + "\n";
		
		for( Parameter p : params ) {
			
			xml += indent(depth+1) + "<perceptParameter>" + "\n";
			xml += p.toXML(depth+2);
			xml += indent(depth+1) + "</perceptParameter>" + "\n";
			
		}

		xml += indent(depth) + "</percept>" + "\n";

		return xml;

	}

	@Override
	public String toProlog() {
		
		String ret = "";
		
		ret+=name;

		if( params.isEmpty() == false) {
			ret += "(";
			
			ret += params.getFirst().toProlog();
			
			for( int a = 1 ; a < params.size(); a++ ) {
				Parameter p = params.get(a);
				ret += "," + p.toProlog();
			} 
			
			ret += ")";
		}
		
		return ret;
	
	}

	/*	public String toProlog() {
		
		String ret = "percept";
		
		ret+="(";
		
		ret+=name;
		
		for( Parameter p : params ) 
			ret += "," + p.toProlog();
		
		ret+=")";
		
		return ret;
	
	}*/

	@Override
	public Object clone() {

		Percept ret = new Percept(this.name, this.getClonedParameters());
		
		ret.source = this.source;
		
		return ret;
	
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if ( obj == null )
			return false;
		if ( obj == this )
			return true;
		
		if( !(obj instanceof Percept) )
			return false;
		
		return super.equals(obj);

	}

}
