package eis.iilang;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * A superclass for actions, events, et cetera.
 * 
 * @author tristanbehrens
 *
 */
public abstract class DataContainer extends IILElement implements Serializable {
	
	/** The name of the DataContainer. */
	protected String name = null;
	
	/** A list of parameters. */
	protected LinkedList<Parameter> params = new LinkedList<Parameter>();

	/** Time of creation */
	protected long timeStamp = System.currentTimeMillis();
	
	/** Source of the data-container */
	protected String source = null;
	
	protected DataContainer() {}
	
	
	/** 
	 * Contructs an DataContainer.
	 * 
	 * @param name
	 * @param parameters
	 */
	public DataContainer(String name, Parameter... parameters) {
		
		this.name = name;
		
		for( Parameter p : parameters )
			this.params.add(p);
		
	}
	
	/**
	 * Returns the name.
	 * 
	 * @return
	 */
	public String getName() {
		
		return name;
		
	}

	/** 
	 * Returns the parameters.
	 * 
	 * @return
	 */
	public LinkedList<Parameter> getParameters() {
		
		return params;
		
	}

	public void addParameter(Parameter p) {
		
		params.add(p);
		
	}
	
	/** 
	 * Converts a data container to a percept.
	 * 
	 * @param container
	 * @return
	 */
	public static Percept toPercept(DataContainer container) {
		
		Parameter[] parameters = new Parameter[container.params.size()];
		
		for(int a = 0 ; a < parameters.length ; a++ )
			parameters[a] = container.params.get(a);
		
		return new Percept(container.getName(), parameters);
		
	}

	/**
	 * Sets the source of the data-container.
	 * 
	 * @param source is the source of the data-container.
	 */
	public void setSource(String source) {
		
		this.source = source;
		
	}
	
	/**
	 * Returns the source of the data-container.
	 * 
	 * @return the source of the data-container.
	 */
	public String getSource() {
		
		return source;
		
	}
	
}
