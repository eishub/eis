package eis.iilang;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Environments are sent by the environment-interface to notify about changes
 * of the environment's state of execution.
 * 
 * @author tristanbehrens
 *
 */
public class EnvironmentEvent extends DataContainer {

	/** the environment has been started */
	public static final int STARTED = 1;
	
	/** the environment has been killed */
	public static final int KILLED = 2;
	
	/** the environment has been paused */
	public static final int PAUSED = 3;
	
	/** the environment has been reset */
	public static final int RESET = 4;
	
	/** the environment has been initialized */
	public static final int INITED = 5; 
	
	/** an event that is not defined yet uses name and parameters. */
	public static final int MISC = 6;

	/** the type of the event */
	private int type = 0;
	
	/**
	 * Constructs an environment-event that is not of type MISC.
	 * 
	 * @param type the type of the event.
	 * @param parameters the event's parameters.
	 */
	public EnvironmentEvent(int type, Parameter...parameters) {

		super(); // no name, no params
		
		this.type = type;
		
		// do not get fooled by the caller
		if( this.type < 1 || this.type > 6)
			this.type = 0;

		if( this.type == MISC ) {

			this.params.addAll(Arrays.asList(parameters));
			
		}
	
	}

	/**
	 * Constructs an environment-event that is not of type MISC.
	 * 
	 * @param type the type of the event.
	 * @param parameters the event's parameters.
	 */
	public EnvironmentEvent(int type, LinkedList<Parameter> parameters) {

		super(); // no name, no params
		
		this.type = type;
		
		// do not get fooled by the caller
		if( this.type < 1 || this.type > 6)
			this.type = 0;

		if( this.type == MISC ) {

			this.params = parameters;
			
		}
	
	}

	/**
	 * Constructs an environment-event that is of type MISC.
	 * 
	 * @param name
	 * @param params
	 */
	public EnvironmentEvent(String name, Parameter...params) {
		
		super(name,params);
		
		this.type = MISC;
		
	}

	@Override
	protected String toXML(int depth) {

		String xml = "";
		
		if( type != MISC) {
			
			// the name 
			xml += indent(depth) + "<environmentEvent ";

			// the type
			xml += "type=\"";
			if( type == STARTED)
				xml += "started";
			else if( type == KILLED)
				xml += "killed";
			else if( type == PAUSED)
				xml += "paused";
			else if( type == RESET)
				xml += "reset";
			else if( type == INITED)
				xml += "inited";
			xml += "\">" + "\n";

			xml += indent(depth) + "</environmentEvent>" + "\n";
	
		}
		else {
		
			xml += indent(depth) + "<environmentEvent name=\"" + name + "\" type=\"misc\">" + "\n";
			
			for( Parameter p : params ) {
				
				xml += indent(depth+1) + "<environmentEventParameter>" + "\n";
				xml += p.toXML(depth+2);
				xml += indent(depth+1) + "</environmentEventParameter>" + "\n";
				
			}

			xml += indent(depth) + "</environmentEvent>" + "\n";
			
		}
		
		return xml;

	}

	@Override
	public String toProlog() {
		
		String ret = "environmentevent";
		
		ret+="(";
		
		if( type == STARTED)
			ret += "started";
		else if( type == KILLED)
			ret += "killed";
		else if( type == PAUSED)
			ret += "paused";
		else if( type == RESET)
			ret += "reset";
		else if( type == INITED)
			ret += "inited";
		else if( type == MISC) {
			
			ret += "misc";
			ret+= "," + name;
		
		}
		
		for( Parameter p : params ) 
			ret += "," + p.toProlog();
		
		ret+=")";
		
		return ret;

		// TODO TEST
		
	}
	
	/**
	 * Returns the type of the event.
	 * @return the type
	 */
	public int getType() {
		
		return type;
	
	}

	@Override
	public Object clone() {

		EnvironmentEvent ret = new EnvironmentEvent(this.type,this.getClonedParameters());
		
		ret.source = this.source;
		
		return ret;
	
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if( !(obj instanceof EnvironmentEvent) )
			return false;
		
		return super.equals(obj);

	}


}
