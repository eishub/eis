package eis.examples.acconnector2007;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import eis.EIDefaultImpl;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.DataContainer;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Percept;


/**
 * Establishes connections to the MASSim-Server.
 * 
 * Entities are single connection to the server.
 * 
 * @author tristanbehrens
 */
@SuppressWarnings("serial")
public class EnvironmentInterface extends EIDefaultImpl implements ConnectionListener, Runnable {

	/** Used to facilitate the EIS-to-environment connection. Each entity is a connection. */
	private ConcurrentHashMap<String,Connection> entitiesToConnections = new ConcurrentHashMap<String,Connection>(); 
	
	/** Stores the percept-queue. getAllPercepts returns the first element. */
	private ConcurrentHashMap<String, LinkedList<LinkedList<Percept>>> entitiesToPercepts = new ConcurrentHashMap<String,LinkedList<LinkedList<Percept>>>();
	
	/** For checking connections. */
	private boolean running = true;
	
	/** For plotting percepts. */
	private Monitor monitor = null;
	
	/**
	 * Contructs an environment.
	 */
	public EnvironmentInterface() {
		
		// add all entities
		// we have 10 cowboys in 2009
		try {
		
			addEntity("connector1");
			addEntity("connector2");
			addEntity("connector3");
			addEntity("connector4");
			addEntity("connector5");
			addEntity("connector6");
			addEntity("connector7");
			addEntity("connector8");
			addEntity("connector9");
			addEntity("connector10");

		} catch (EntityException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Returns the percepts of an entity. That is first percept in the queue, 
	 * that contains the percepts sent by the server.
	 */
	@Override
	public LinkedList<Percept> getAllPerceptsFromEntity(String entity) {

		LinkedList<LinkedList<Percept>> queue = entitiesToPercepts.get(entity);
		
		if( queue == null )
			return new LinkedList<Percept>();
		
		if( queue.isEmpty() )
			return new LinkedList<Percept>();
		
		LinkedList<Percept> ret = queue.removeFirst();
		
		return ret;
		
	}

	@Override
	public boolean isConnected() {
		
		/*
		// no connection, no environment
		if( entitiesToConnections.values().size() == 0 ) 
			return false;

		// if there is an non-connected socket return false
		for(Connection c : entitiesToConnections.values()) {
			
			if( c.isConnected() == false) 
				return false;
			
		}*/
		
		return true;

	}

	@Override
	public void manageEnvironment(EnvironmentCommand command)
			throws ManagementException {
		
		if( command.getType() == EnvironmentCommand.START) {

			if( monitor != null )
				monitor.setVisible(false);
			
			// start monitor
			monitor = new Monitor();
			
			// start connections checker
			new Thread(this).start();

		}
		else if( command.getType() == EnvironmentCommand.KILL) {

			if( monitor != null )
				monitor.setVisible(false);
		}

	}

	@Override
	public void release() {

		// stop execution
		running = false;

		// take down all connections
		
		for( Connection c : this.entitiesToConnections.values() )
			try {
				c.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Connects to a MASSim-server.
	 * 
	 * @param entity the entity that is going to be associated to the connection.
	 * @param server the server.
	 * @param port the port
	 * @param user the username
	 * @param password the password
	 * @return the result of the action, either connected of failed.
	 * @throws ActException
	 */
	public Percept actionconnect(String entity, Identifier server, Numeral port, Identifier user, Identifier password) throws ActException {

		Connection c = this.entitiesToConnections.get(entity);
		
		// there is a connection; close ot
		if( c != null ) {
			
			try {
				c.close();
			} catch (IOException e) {
				throw new ActException(ActException.FAILURE, "Existing connection could not be closed", e);
			}

			c = null; 
		
		}
		
		assert c == null;
		
		// open socket
		try {

			c = new Connection(this, server.getValue(), (port.getValue()).intValue());

		} catch (UnknownHostException e) {
	
			throw new ActException(ActException.FAILURE, "Unknown host " + server.getValue());
		
		} catch (IOException e) {

			e.printStackTrace();
			throw new ActException(ActException.FAILURE, "IO Error");
		
		}
		
		assert c != null;
				
		// authent
		boolean result = c.authenticate(user.getValue(), password.getValue());
		
		if( result == false ) {

			LinkedList<Percept> percepts = new LinkedList<Percept>();
			Percept percept = new Percept("failed");
			percepts.add(percept);
			enqueuePercepts(entity,percepts);

			return percept;
		
		}
		
		// store
		entitiesToConnections.put(entity,c);

		// start thread
		new Thread(c).start();
		
		// return success
		LinkedList<Percept> percepts = new LinkedList<Percept>();
		Percept percept = new Percept("connected");
		percepts.add(percept);
		enqueuePercepts(entity, percepts);

		return percept;
	
	}

	/**
	 * Puts a list of percepts into the percept-queue.
	 * 
	 * @param entity
	 * @param percepts
	 */
	private void enqueuePercepts(String entity, LinkedList<Percept> percepts) {

		LinkedList<LinkedList<Percept>> queue = entitiesToPercepts.get(entity);
		
		if( queue == null ) {
			
			queue = new LinkedList<LinkedList<Percept>>();
			
		}

		queue.add(percepts);

		entitiesToPercepts.put(entity, queue);
		
	}

	// {skip,left,up,right,down,pick,drop,mark,unmark}. The attribute
	// "param" is optional and contains parameters of an action. It is only
	// required in combination with action type "mark", when it should contain
	// a string value of a mark. Value of a mark can be up to 5 characters
	// long. In the case of longer value, it will be cut to first 5 characters.
	
	public Percept actionskip(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("skip",null);
		
		return null;

	}
	
	public Percept actionleft(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("left",null);
		
		return null;

	}
	
	public Percept actionup(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("up",null);
		
		return null;

	}
	
	public Percept actionright(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("right",null);
		
		return null;

	}
	
	public Percept actiondown(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("down",null);
		
		return null;

	}
	
	public Percept actionpickGold(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("pick",null);
		
		return null;

	}
	
	public Percept actiondropGold(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("drop",null);
		
		return null;

	}
	
	public Percept actionmark(String entity, Identifier mark) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("mark",mark.getValue());
		
		return null;

	}
	
	public Percept actionunmark(String entity) throws ActException, NoEnvironmentException { 

		Connection c = this.entitiesToConnections.get(entity);
		
		if( c == null )
			throw new NoEnvironmentException("Not connected");
		
		c.act("unmark",null);
		
		return null;

	}
	
	/** 
	 * Handles an incoming message.
	 */
	public void handleMessage(Connection connection, LinkedList<Percept> percepts) {

		for( Entry<String,Connection> e : this.entitiesToConnections.entrySet() ) {
			
			// look up entity
			if( connection.equals(e.getValue())) {
				
				String entity = e.getKey();
				
				// store last percept
				enqueuePercepts(entity,percepts);
				
				// notify agents
				for( Percept p : percepts )
					try {
						this.notifyAgentsViaEntity(p, entity);
					} catch (EnvironmentInterfaceException e1) {
						e1.printStackTrace();
					}
				
			}
			
		}
		
		
		//System.out.println(container);
		
	}

	/**
	 * Prefixes a list of percepts. Returns a new list that contains the same
	 * percepts with a prefix added.
	 * 
	 * @param percepts the percepts to be prefixed
	 * @param prefix the prefix itself
	 * @return a new list with prefixed percepts
	 */
	private LinkedList<Percept> prefixPercepts(LinkedList<Percept> percepts,
			String prefix) {

		LinkedList<Percept> ret = new LinkedList<Percept>();
	
		for( Percept p : percepts ) {
			
			Percept prefixed = (Percept) p.clone();
			prefixed.setName(prefix + p.getName());
			ret.add(prefixed);
			
		}
		
		return ret;
		
	}

	/**
	 * Check all connections every couple of seconds.
	 */
	public void run() {

		while( running ) {

			// check the connections
			for( Entry<String, Connection> entry : this.entitiesToConnections.entrySet() ) {
				
				String e = entry.getKey();
				Connection c = entry.getValue();
				
				if( c.isBound() == false || c.isClosed() == true || c.isConnected() == false || c.isExecuting() == false ) {
					entitiesToConnections.remove(e);
				}
				
			}
			
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
			}
			
		}
		
	}

	@Override
	public String requiredVersion() {
		return "0.2";
	}

	/* (non-Javadoc)
	 * @see eis.EIDefaultImpl#notifyAgentsViaEntity(eis.iilang.Percept, java.lang.String[])
	 */
	@Override
	protected void notifyAgentsViaEntity(Percept percept, String... pEntities)
			throws EnvironmentInterfaceException {
		
		// update monitor
		if( monitor != null ) 
			monitor.processPercept(percept, pEntities[0]);
		
		// really send the percepts
		super.notifyAgentsViaEntity(percept, pEntities);
	}
	
}
