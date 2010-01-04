package eis.iilang;

import java.util.LinkedList;

/**
 * Represents a function over parameters. It consits of a name and
 * a list of parameters.
 * 
 * @author tristanbehrens
 */
public class Function extends Parameter {
	
	/** The name of the function. */
	private String name = null;
	
	/** A list of parameters. */
	private LinkedList<Parameter> params = new LinkedList<Parameter>();

	/**
	 * Instantiates a function.
	 * 
	 * @param name the name of the function.
	 * @param parameters the parameters.
	 */
	public Function(String name, Parameter... parameters) {
		
		this.name = name;
		
		for( Parameter p : parameters )
			this.params.add(p);
		
	}

	/**
	 * Instantiates a function.
	 * 
	 * @param name the name of the function.
	 * @param parameters the parameters.
	 */
	public Function(String name, LinkedList<Parameter> parameters) {
		
		this.name = name;

		this.params = parameters;
		
	}

	/**
	 * Returns the name of the function.
	 * 
	 * @return the name of the function.
	 */
	public String getName() {
		
		return name;
		
	}

	/**
	 * Sets the name of the function
	 * 
	 * @param name the name of the function
	 */
	public void setName(String name) {
		
		this.name = name;

	}
	
	/**
	 * Returns the parameters of the function.
	 * 
	 * @return the parameters of the function.
	 */
	public LinkedList<Parameter> getParameters() {
		
		return params;
		
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters
	 */
	public void setParameters(LinkedList<Parameter> parameters) {
		
		this.params = parameters;
		
	}
	
	/** 
	 * Returns the parameters. Returns the parameters as a clone.
	 * 
	 * @return the parameters of the function
	 */
	public LinkedList<Parameter> getClonedParameters() {
		
		LinkedList<Parameter> ret = new LinkedList<Parameter>();
		
		for( Parameter p : params ) {
			
			ret.add((Parameter) p.clone());
			
		}
		
		return ret;
		
	}

	@Override
	protected String toXML(int depth) {

		String xml = "";
		
		xml += indent(depth) + "<function name=\"" + name +"\">" + "\n";

		for( Parameter p : params ) {
			
			xml += p.toXML(depth+1);
			
		}
		
		xml += indent(depth) + "</function>" + "\n";
		
		return xml;

	}
	
	@Override
	public String toProlog() {
		
		String ret = name;
				
		if( params.size() > 0 ) {
			ret += "(";

			ret += params.getFirst().toProlog();
		
			for( int a = 1 ; a < params.size() ; a++ )
				ret += "," + params.get(a).toProlog();
		
			ret+=")";
		}
		
		return ret;
	
	}

	@Override
	public Object clone() {

		return new Function(this.name, this.getClonedParameters());
		
	}

}
