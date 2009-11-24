package eis;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentCommand;
import eis.iilang.EnvironmentEvent;
import eis.iilang.Parameter;
import eis.iilang.Percept;


/**
 * This class represents the general environment interface class.
 * <p/>
 * A special environment interface has to extend this class and implement he abstract methods.
 * <p/>
 * It supports the following functionalities:
 * <ul>
 * <li>attaching, detaching, and notifying listeners;</li>
 * <li>registering and unregistering agents;</li>
 * <li>adding and removing entities;</li>
 * <li>managing the agents-entities-relationship;</li>
 * <li>performing actions and retrieving percepts;</li>
 * <li>managing the environment; and</li>
 * <li>loading environment-interfaces from jar-files.</li>
 * </ul>
 * 
 * @author tristanbehrens
 *
 */
/**
 * @author tristanbehrens
 *
 */
/**
 * @author tristanbehrens
 *
 */
/**
 * @author tristanbehrens
 *
 */
public abstract class EIDefaultImpl implements EnvironmentInterfaceStandard,Serializable {
	

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
	 * This collection stores the listeners that are used to notify about certain events.
	 * <p/> 
	 * The collection can be changed by invoking the respective methods for attaching and
	 * detaching listeners.
	 */
	private Vector<EnvironmentListener> environmentListeners = null;
	
	/**
	 * Stores for each agent (represented by a string) a set of listeners.
	 */
	private ConcurrentHashMap<String,HashSet<AgentListener>> agentsToAgentListeners = null;

	/**
	 * Stores for each entity its respective type.
	 */
	private HashMap<String,String> entitiesToTypes = null;

	
	/**
	 * Instantiates the class.
	 */
	public EIDefaultImpl() {
		
		environmentListeners = new Vector<EnvironmentListener>();
		agentsToAgentListeners = new ConcurrentHashMap<String,HashSet<AgentListener>>();
		
		registeredAgents 	= new LinkedList<String>();
		entities 			= new LinkedList<String>();
		freeEntities 		= new LinkedList<String>();
		agentsToEntities 	= new ConcurrentHashMap<String,HashSet<String>>();
		entitiesToTypes		= new HashMap<String,String>();
		
	}

	
	
	/*
	 * Listener functionality. Attaching, detaching, notifying listeners.
	 */

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#attachEnvironmentListener(eis.EnvironmentListener)
	 */
	public void attachEnvironmentListener(EnvironmentListener listener) {
		
		if( environmentListeners.contains(listener) == false)
			environmentListeners.add(listener);
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#detachEnvironmentListener(eis.EnvironmentListener)
	 */
	public void detachEnvironmentListener(EnvironmentListener listener) {
		
		if( environmentListeners.contains(listener) == true)
			environmentListeners.remove(listener);
		
	}
	
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#attachAgentListener(java.lang.String, eis.AgentListener)
	 */
	public void attachAgentListener(String agent, AgentListener listener) {
		
		if( registeredAgents.contains(agent) == false )
			return;
		
		HashSet<AgentListener> listeners = agentsToAgentListeners.get(agent);
		
		if( listeners == null )
			listeners = new HashSet<AgentListener>();
		
		listeners.add(listener);
		
		agentsToAgentListeners.put(agent,listeners);
		
	}
	
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#detachAgentListener(java.lang.String, eis.AgentListener)
	 */
	public void detachAgentListener(String agent, AgentListener listener) {

		if( registeredAgents.contains(agent) == false )
			return;
		
		HashSet<AgentListener> listeners = agentsToAgentListeners.get(agent);
		
		if( listeners == null || listeners.contains(agent) == false )
			return;
		
		listeners.remove(listener);
		
		agentsToAgentListeners.put(agent,listeners);

	}
	
	/**
	 * Notifies agents about a percept.
	 * 
	 * @param percept is the percept
	 * @param agents is the array of agents that are to be notified about the event.
	 * If the array is empty, all registered agents will be notified. The array has to 
	 * contain only registered agents.
	 * @throws AgentException is thrown if at least one of the agents in the array is not
	 * registered.
	 */
     protected void notifyAgents(Percept percept, String...agents) throws EnvironmentInterfaceException {

		// no listeners, no notification
		// BUG
		//if (agentsListeners.isEmpty())
			//return;

		// send to all registered agents
		if (agents == null) {

			for (String agent : registeredAgents) {
				
				HashSet<AgentListener> agentListeners = agentsToAgentListeners.get(agent);

				if( agentListeners == null )
					continue;
				
				for (AgentListener listener : agentListeners) {

					listener.handlePercept(agent, percept);

				}

			}

			return;
		}

		// send to specified agents
		for (String agent : agents) {

			if (!registeredAgents.contains(agent))
				throw new EnvironmentInterfaceException("Agent " + agent
						+ " has not registered to the environment.");

			HashSet<AgentListener> agentListeners = agentsToAgentListeners.get(agent);

			if( agentListeners == null )
				continue;
			
			for (AgentListener listener : agentListeners) {

				listener.handlePercept(agent,percept);

			}

		}

	}
	

	/**
	 * Sends a percept to an agent/several agents via a given array of entities.
	 * 
	 * @param percept
	 * @param entity
	 * @throws EnvironmentInterfaceException
	 */
	protected void notifyAgentsViaEntity(Percept percept, String...pEntities) throws EnvironmentInterfaceException {
		
		// check
		for( String entity : pEntities)
			if( this.entities.contains(entity) == false)
				throw new EnvironmentInterfaceException("\"" + entity + "\" does not exist.");

		// use all entities
		if( pEntities.length == 0) {

			for ( String entity : entities ) {
				for (Entry<String, HashSet<String>> entry : agentsToEntities.entrySet()) {

					if (entry.getValue().contains(entity))
						this.notifyAgents(percept, entry.getKey());

				}
			} 
			
		}
		// use given array
		else {
			
			for ( String entity : pEntities ) {
				for (Entry<String, HashSet<String>> entry : agentsToEntities.entrySet()) {

					if (entry.getValue().contains(entity))
						this.notifyAgents(percept, entry.getKey());

				}
			} 
			
		}
		
	} 
	
	
	/**
	 * Notifies all listeners about an entity that is free.
	 * 
	 * @param entity is the free entity.
	 */
	protected void notifyFreeEntity(String entity) {
		
		for( EnvironmentListener listener : environmentListeners ) {
			
			listener.handleFreeEntity(entity);
			
		}
		
	}

	
	
	/**
	 * Notifies all listeners about an entity that has been newly created.
	 * 
	 * @param entity is the new entity.
	 */
	protected void notifyNewEntity(String entity) {
		
		for( EnvironmentListener listener : environmentListeners ) {
			
			listener.handleNewEntity(entity);
			
		}
		
	}

	
	
	/**
	 * Notifies all listeners about an entity that has been deleted.
	 * 
	 * @param entity is the deleted entity.
	 */
	protected void notifyDeletedEntity(String entity) {
		
		for( EnvironmentListener listener : environmentListeners ) {
			
			listener.handleDeletedEntity(entity);
			
		}
		
	}

	
	
	
	/**
	 * Notifies the listeners about an environment-event.
	 * 
	 * @param event
	 */
	protected void notifyEnvironmentEvent(EnvironmentEvent event) {
		
		for( EnvironmentListener listener : environmentListeners ) {
			
			listener.handleEnvironmentEvent(event);
			
		}
		
	}

	

	/*
	 * Registering functionality. Registering and unregistering agents.
	 */

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

	
		
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#getEntities()
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<String> getEntities() {
		
		return (LinkedList<String>)entities.clone();
		
	}


	
	/*
	 * Agents-entity-relation manipulation functionality.
	 */

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

	
	
	/*
	 * Management functionality.
	 */
	
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#manageEnvironment(eis.iilang.EnvironmentCommand, java.lang.String[])
	 */
	public abstract void manageEnvironment(EnvironmentCommand command, String... args) 
	throws ManagementException,NoEnvironmentException;

	
	
	/*
	 * Misc functionality.
	 */
	
	/**
	 * Loads a specific environment interface from a given jar-file and returns an instance of the respective class.
	 * 
	 * @param jarFile the jar-file from which the environment-interface is supposed to be loaded.
	 * @return an instance of the requested environment-interface.
	 * @throws IOException is thrown if an attempt to load fails.
	 * @deprecated use the eis.EILoader instead
	 */
	public static EIDefaultImpl fromJarFile(File jarFile) throws IOException {
		
		// 1. locate file, check for existence, check for being a jar
		if( jarFile.exists() == false )
			throw new IOException("\"" + jarFile.getAbsolutePath() + "\" does not exist.");
			
		if( jarFile.getName().endsWith(".jar") == false )
			throw new IOException("\"" + jarFile.getAbsolutePath() + "\" is not a jar-file.");
				
		// 2. add the jar file to the classpath
		URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		URL url = jarFile.toURI().toURL();
		
		try {
			Method method = sysclass.getDeclaredMethod("addURL",new Class[]{URL.class});
			method.setAccessible(true);
			method.invoke(sysloader,new Object[]{ url });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}
	
		// 3. retrieve class name = file-name withoud postfix + .EnvironmentInterface
		String jarName = jarFile.getName();
		String className = jarName.substring(0, jarName.length() -4 ) + ".EnvironmentInterface";
		URLClassLoader loader = new URLClassLoader(new URL[]{url});
		Class<?> envInterfaceClass = null;
		try {
			envInterfaceClass = loader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new IOException("Class \"" + className + "\" could not be loaded from \"" + jarFile + "\"");
		}
	
		// 3. TODO get an instance of the class
		Constructor<?> c = null;
		EIDefaultImpl ei = null;
		try {
			c = envInterfaceClass.getConstructor();
			ei = (EIDefaultImpl)(c.newInstance());
		} catch (Exception e) {
			throw new IOException("Class \"" + className + "\" could not be loaded from \"" + jarFile + "\"");
		} 
		
		return ei;
		
	}

	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#release()
	 */
	public abstract void release();
	
	/* (non-Javadoc)
	 * @see eis.EnvironmentInterfaceStandard#isConnected()
	 */
	public abstract boolean isConnected();
	
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

	
	/**
	 * Sets the type of an entity.
	 * 
	 * @param entity is the entity
	 * @param type is the respective type of the entity
	 * @throws EntityException is thrown if the entity doas nox exist or if it already has a type.
	 */
	public void setType(String entity, String type) throws EntityException {

		if( !entities.contains(entity) )
			throw new EntityException("Entity \"" + entity + "\" does not exist!");
		
		if( entitiesToTypes.get(entity) != null )
			throw new EntityException("Entity \"" + entity + "\" already has a type!");
	
		entitiesToTypes.put(entity, type);
		
	}
	
}