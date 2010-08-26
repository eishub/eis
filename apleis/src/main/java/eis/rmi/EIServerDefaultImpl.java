package eis.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import eis.AgentListener;
import eis.EnvironmentListener;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Parameter;
import eis.iilang.Percept;

// TODO remove redundancy between this class and EIDefaultImpl
public abstract class EIServerDefaultImpl implements EIServerRemote {

	/**
	 * This is a list of registered agents.
	 * <p/>
	 * Only registered agents can act and be associated with entities.
	 */
	private LinkedList<String> registeredAgents = null;

	/**
	 * This is a list of entities.
	 */
	private LinkedList<String> entities = null;
	
	/**
	 * This is a list of entities, that are not associated with any agent.
	 */
	private LinkedList<String> freeEntities = null;

	/**
	 * This map stores the agents-entities-relation.
	 */
	private ConcurrentHashMap<String,HashSet<String>> agentsToEntities = null;

	/**
	 * Stores for each entity its respective type.
	 */
	private HashMap<String,String> entitiesToTypes = null;

	public static boolean debug = true;
	
	private Vector<EIClientRemote> remoteClientListeners = new Vector<EIClientRemote>();
		
	public EIServerDefaultImpl() {
		super();
		
		registeredAgents 	= new LinkedList<String>();
		entities 			= new LinkedList<String>();
		freeEntities 		= new LinkedList<String>();
		agentsToEntities 	= new ConcurrentHashMap<String,HashSet<String>>();
		entitiesToTypes		= new HashMap<String,String>();

		// creating registry
		try { 
			debugPrintln("Creating registry...");

			LocateRegistry.createRegistry( Registry.REGISTRY_PORT ); 
		  
			debugPrintln("Exporting...");
			EIServerDefaultImpl server = this; 
			EIServerRemote stub = (EIServerRemote) UnicastRemoteObject.exportObject( server, 0 ); 
			//RemoteServer.setLog( System.out ); 
		 
			debugPrintln("Rebinding registry...");
			Registry registry = LocateRegistry.getRegistry(); 
			registry.rebind( "EIServer", stub ); 
		    
			debugPrintln("Server established!");
		    
		} 
		catch ( RemoteException e )  { 

			debugPrintln("Caught an exception");
			debugPrintln(e);
			
		}

	}
	
	public void debugPrintln(Object obj) {
		
		if( debug )
			System.out.println("[SERVER]: " + obj);
		
	}

	public void attachClientListener(EIClientRemote client) throws RemoteException {
		
		debugPrintln("Registered client: " + client);
		
		remoteClientListeners.add(client);
		
	}
	
	public void detachClientListener(EIClientRemote client) throws RemoteException {
		
		remoteClientListeners.remove(client);
		
	}

	protected void notifyDeletedEntity(String entity) {
	
		for(EIClientRemote client : remoteClientListeners ) {
			
			try {
				client.notifyDeletedEntity(entity);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}

	protected void notifyFreeEntity(String entity) {

		for(EIClientRemote client : remoteClientListeners ) {
			
			try {
				client.notifyFreeEntity(entity);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	protected void notifyNewEntity(String entity) {

		for(EIClientRemote client : remoteClientListeners ) {
			
			try {
				client.notifyNewEntity(entity);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#associateEntity(java.lang.String, java.lang.String)
	 */
	public void associateEntity(String agent, String entity) throws RelationException {
		
		// check if exists
		if( !entities.contains(entity) )
			throw new RelationException("Entity \"" + entity + "\" does not exist!");

		if( !registeredAgents.contains(agent) )
			throw new RelationException("Agent \"" + entity + "\" has not been registered!");

		// check if associated
		if( !freeEntities.contains(entity) )
			throw new RelationException("Entity \"" + entity + "\" has already been associated!");
	
		// remove
		freeEntities.remove(entity);
		
		// associate
		HashSet<String> ens = agentsToEntities.get(agent);
		if( ens == null ) {
			
			ens = new HashSet<String>();
		}
		ens.add(entity);
		agentsToEntities.put(agent, ens);
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#freeEntity(java.lang.String)
	 */
	public void freeEntity(String entity) throws RelationException {

		// check if exists
		if( !entities.contains(entity) )
			throw new RelationException("Entity \"" + entity + "\" does not exist!");

		// find the association and remove
		boolean associated = false;
		for( Entry<String,HashSet<String>> entry : agentsToEntities.entrySet()) {
			
			String agent = entry.getKey();
			HashSet<String> ens = entry.getValue();
			
			if( ens.contains(entity) ) {
				
				ens.remove(entity);
				
				agentsToEntities.put(agent, ens);
				
				associated = true;
				
				break;
			}
			
		}
		
		// fail if entity has not been associated
		if( associated == false)
			throw new RelationException("Entity \"" + entity + "\" has not been associated!");
	
		// add to free entites
		freeEntities.add(entity);
		
		// notify
		notifyFreeEntity(entity);
		
	}
	
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#freeAgent(java.lang.String)
	 */
	public void freeAgent(String agent) throws RelationException {
		
		// check if exists
		if( !registeredAgents.contains(agent) )
			throw new RelationException("Agent \"" + agent + "\" does not exist!");
		
		HashSet<String> ens = agentsToEntities.get(agent);
	
		this.freeEntities.addAll(ens);

		// notify
		for( String en : ens )
			notifyFreeEntity(en);

		agentsToEntities.remove(agent);
		
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#freePair(java.lang.String, java.lang.String)
	 */
	public void freePair(String agent, String entity) throws RelationException {

		// check if exists
		if( !registeredAgents.contains(agent) )
			throw new RelationException("Agent \"" + agent + "\" does not exist!");

		// check if exists
		if( !entities.contains(entity) )
			throw new RelationException("Entity \"" + entity + "\" does not exist!");
	
		HashSet<String> ens = agentsToEntities.get(agent);
		
		if ( ens == null || ens.contains(entity) == false)
			throw new RelationException("Agent \"" + agent + " is not associated with entity \"" + entity + "\"!");

		// update mapping
		ens.remove(entity);
		agentsToEntities.put(agent,ens);
		
		// store as free entity
		this.freeEntities.add(entity);

		// notify
		for( String en : ens )
			notifyFreeEntity(en);
	
	}
	
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getAssociatedEntities(java.lang.String)
	 */
	public HashSet<String> getAssociatedEntities(String agent) throws AgentException {
		
		if( registeredAgents.contains(agent) == false )
			throw new AgentException("Agent \"" + agent + "\" has not been registered.");
		
		HashSet<String> ret = this.agentsToEntities.get(agent);
		
		if( ret == null)
			ret = new HashSet<String>();
		
		return ret;
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getAssociatedAgents(java.lang.String)
	 */
	public HashSet<String> getAssociatedAgents(String entity) throws EntityException {
		
		if( entities.contains(entity) == false )
			throw new EntityException("Entity \"" + entity + "\" has not been registered.");
		
		HashSet<String> ret = new HashSet<String>();
		
		for( Entry<String, HashSet<String>> entry : agentsToEntities.entrySet() ) {
			
			if( entry.getValue().contains(entity) )
				ret.add(entry.getKey());
			
		}
		
		return ret;
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getFreeEntities()
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<String> getFreeEntities() {
		
		return (LinkedList<String>)freeEntities.clone();
		
	}

	
	/*
	 * Acting/perceiving functionality.
	 */

	
	// TODO use freeAgent here
	// TODO maybe use isConnencted here
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#performAction(java.lang.String, eis.iilang.Action, java.lang.String[])
	 */
	public LinkedList<Percept> performAction(String agent, Action action, String...entities)
	throws ActException, NoEnvironmentException {

		// unregistered agents cannot act
		if( registeredAgents.contains(agent) == false )
			throw new ActException("Agent \"" + agent + "\" is not registered." );
		
		// get the associated entities
		HashSet<String> associatedEntities = agentsToEntities.get(agent);
		
		// no associated entity/ies -> trivial reject
		if( associatedEntities == null || associatedEntities.size() == 0 )
			throw new ActException("Agent \"" + agent + "\" has no associated entities." );

		// entities that should perform the action
		HashSet<String> targetEntities = null;
		if( entities.length == 0 ) {
			
			targetEntities = associatedEntities;
		
		}
		else {
			
			targetEntities = new HashSet<String>();
			
			for( String entity : entities ) {
				
				if( associatedEntities.contains(entity) == false)
					throw new ActException("Entity \"" + entity + "\" is not associated to agent \"" + agent + "\"." );
			
				targetEntities.add(entity);
				
			} 
			
		}
		
		// get the parameters
		LinkedList<Parameter> params = action.getParameters();
		
		// targetEntities contains all entities that should perform the action
		// params contains all parameters

		// determine class parameters for finding the method
		// and store the parameters as objects
		Class<?>[] classParams = new Class[params.size()+1];
		classParams[0] = String.class; // entity name
		for( int a = 0 ; a < params.size() ; a++ )
			classParams[a+1] = params.get(a).getClass();

		// return value
		LinkedList<Percept> rets = new LinkedList<Percept>();
		
		try {

			// lookup the method
			Method m = this.getClass().getMethod("action" + action.getName(),classParams);

			if( Class.forName("eis.iilang.Percept").isAssignableFrom(m.getReturnType()) == false)
				throw new ActException("Wrong return-type");

			// invoke
			for( String entity : targetEntities ) {

				Object[] objParams = new Object[params.size()+1];
				objParams[0] = entity; // entity name
				for( int a = 0 ; a < params.size() ; a++ )
					objParams[a+1] = params.get(a);
				
				Percept ret = (Percept) m.invoke(this, objParams );
				
				rets.add( ret );
				
			}

		} catch (ClassNotFoundException e) {

			throw new ActException("Class not found", e);
			
		} catch (SecurityException e) {

			throw new ActException("Security exception", e);

		} catch (NoSuchMethodException e) {

			throw new ActException("No such method", e);
			
		} catch (IllegalArgumentException e) {

			throw new ActException("Illegal argument", e);
		
		} catch (IllegalAccessException e) {

			throw new ActException("Illegal access", e);

		} catch (InvocationTargetException e) {

			// action has failed -> let fail
			if(e.getCause() instanceof ActException )
				throw (ActException) e.getCause(); // rethrow
			else if(e.getCause() instanceof NoEnvironmentException)
				throw (NoEnvironmentException) e.getCause(); // rethrow
	
			throw new ActException("Invocation target exception", e);
		
		}
		
		return rets;

	}
	
	// TODO maybe use isConnencted here
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getAllPercepts(java.lang.String, java.lang.String[])
	 */
	public LinkedList<Percept> getAllPercepts(String agent, String...entities) 
	throws PerceiveException, NoEnvironmentException {
		
		// fail if ther agent is not registered
		if( registeredAgents.contains(agent) == false)
			throw new PerceiveException("Agent \"" + agent + "\" is not registered.");
		
		// get the associated entities
		HashSet<String> associatedEntities = agentsToEntities.get(agent);

		// fail if there are no associated entities
		if( associatedEntities == null || associatedEntities.size() == 0 )
			throw new PerceiveException("Agent \"" + agent + "\" has no associated entities.");

		// return value
		LinkedList<Percept> ret = new LinkedList<Percept>();

		// gather all percepts
		if( entities.length == 0 ) {

			for( String entity : associatedEntities )
				ret.addAll( getAllPerceptsFromEntity(entity) );

		}
		// only from specified entities
		else {

			for( String entity : entities) {
				
				if( associatedEntities.contains(entity) == false)
					throw new PerceiveException("Entity \"" + entity + "\" has not been associated with the agent \"" + agent + "\".");
				
				ret.addAll( getAllPerceptsFromEntity(entity) );
				
			}
			
		}
	
		return ret;
		
	}
	
	/**
	 * Gets all percepts of an entity.
	 * <p/>
	 * This method must be overridden.
	 * 
	 * @param entity is the entity whose percepts should be retrieved.
	 * @return a list of percepts.
	 */
	protected abstract LinkedList<Percept> getAllPerceptsFromEntity(String entity) throws PerceiveException, NoEnvironmentException;

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getEntities()
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<String> getEntities() {
		
		return (LinkedList<String>)entities.clone();
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getType(java.lang.String)
	 */
	public String getType(String entity) throws EntityException {
		
		if( !this.entities.contains(entity) )
			throw new EntityException("Entity \"" + entity + "\" does not exist!");
		
		String type = entitiesToTypes.get(entity);
		
		if( type == null )
			type = "unknown";
			
		return type;
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#registerAgent(java.lang.String)
	 */
	public void registerAgent(String agent) throws AgentException {

		if (registeredAgents.contains(agent))
			throw new AgentException("Agent " + agent
					+ " has already registered to the environment.");

		registeredAgents.add(agent);

	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#unregisterAgent(java.lang.String)
	 */
	public void unregisterAgent(String agent) throws AgentException {

		// fail if agents is not registered
		if (!registeredAgents.contains(agent))
			throw new AgentException("Agent " + agent
					+ " has not registered to the environment.");

		// remove from mapping, might be null
		agentsToEntities.remove(agent);
		
		// finally remove from registered list
		registeredAgents.remove(agent);

	}

	
	
	/*
	 * Entity functionality. Adding and removing entities.
	 */
	
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getAgents()
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<String> getAgents() {
		
		return (LinkedList<String>)registeredAgents.clone();
		
	}

	/** 
	 * Adds an entity to the environment.
	 * 
	 * @param entity is the identifier of the entity that is to be added.
	 * @throws PlatformException is thrown if the entity already exists.
	 */
	protected void addEntity(String entity) throws EntityException {

		// fail if entity does exist
		if( entities.contains(entity) )
			throw new EntityException("Entity \"" + entity + "\" does already exist");
		
		// add
		entities.add(entity);
		freeEntities.add(entity);
		
		// notify
		notifyNewEntity(entity);
		
	}

	/**
	 * Deletes an entity, by removing its id from the internal list, and disassociating 
	 * it from the respective agent.
	 * 
	 * @param entity the id of the entity that is to be removed.
	 * @throws PlatformException if the agent does not exist.
	 */
	// TODO use freeEntity here
	protected void deleteEntity(String entity) throws EntityException {
	
		// fail if entity does not exist
		if( !entities.contains(entity) )
			throw new EntityException("Entity \"" + entity + "\" does not exist");

		// find the association and remove
		for( Entry<String,HashSet<String>> entry : agentsToEntities.entrySet()) {
			
			String agent = entry.getKey();
			HashSet<String> ens = entry.getValue();
			
			if( ens.contains(entity) ) {
				
				ens.remove(entity);
				
				agentsToEntities.put(agent, ens);
				
				break;
			}
			
		}

		// finally delete
		entities.remove(entity);
		freeEntities.remove(entity);

		// notify 
		notifyDeletedEntity(entity);
		
	}

	
}
