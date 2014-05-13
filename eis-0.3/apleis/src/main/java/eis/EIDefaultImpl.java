package eis;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.QueryException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;
import eis.iilang.Percept;

/**
 * This class represents the default-implementation for
 * <code>EnvironmentInterfaceStandard</code>.
 * <p/>
 * This class implements these functionalities of
 * <code>EnvironmentInterfaceStandard</code>:
 * <ul>
 * <li>attaching and detaching listeners;</li>
 * <li>registering and unregistering agents;</li>
 * <li>managing the agents-entities-relationship;</li>
 * <li>performing actions and retrieving percepts;</li>
 * </ul>
 * <p/>
 * It also implements these:
 * <ul>
 * <li>notifying listeners;</li>
 * <li>adding and removing entities.</li>
 * </ul>
 * 
 * @author tristanbehrens
 * 
 */
public abstract class EIDefaultImpl implements EnvironmentInterfaceStandard,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3728998641924854414L;

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
	 * This map stores the agents-entities-relation.
	 */
	private ConcurrentHashMap<String, HashSet<String>> agentsToEntities = null;

	/**
	 * This collection stores the listeners that are used to notify about
	 * certain events.
	 * <p/>
	 * The collection can be changed by invoking the respective methods for
	 * attaching and detaching listeners.
	 */
	private Vector<EnvironmentListener> environmentListeners = null;

	/**
	 * Stores for each agent (represented by a string) a set of listeners.
	 */
	private ConcurrentHashMap<String, HashSet<AgentListener>> agentsToAgentListeners = null;

	/**
	 * Stores for each entity its respective type.
	 */
	private HashMap<String, String> entitiesToTypes = null;

	/**
	 * The state of the environment-interface
	 */
	private EnvironmentState state = null;

	/**
	 * Instantiates the class.
	 */
	public EIDefaultImpl() {

		environmentListeners = new Vector<EnvironmentListener>();
		agentsToAgentListeners = new ConcurrentHashMap<String, HashSet<AgentListener>>();

		registeredAgents = new LinkedList<String>();
		entities = new LinkedList<String>();
		agentsToEntities = new ConcurrentHashMap<String, HashSet<String>>();
		entitiesToTypes = new HashMap<String, String>();

		state = EnvironmentState.INITIALIZING;

	}

	/**
	 * Resets the environment(-interface) with a set of key-value-pairs.
	 * 
	 * @param parameters
	 * @throws ManagementException
	 *             is thrown either when the initializing is not supported or
	 *             the parameters are wrong.
	 */
	public void reset(Map<String, Parameter> parameters)
			throws ManagementException {

		state = EnvironmentState.PAUSED;

	}

	/*
	 * Listener functionality. Attaching, detaching, notifying listeners.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#attachEnvironmentListener(eis.
	 * EnvironmentListener)
	 */
	public void attachEnvironmentListener(EnvironmentListener listener) {

		if (environmentListeners.contains(listener) == false)
			environmentListeners.add(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#detachEnvironmentListener(eis.
	 * EnvironmentListener)
	 */
	public void detachEnvironmentListener(EnvironmentListener listener) {

		if (environmentListeners.contains(listener) == true)
			environmentListeners.remove(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eis.EnvironmentInterfaceStandard#attachAgentListener(java.lang.String,
	 * eis.AgentListener)
	 */
	public void attachAgentListener(String agent, AgentListener listener) {

		if (registeredAgents.contains(agent) == false)
			return;

		HashSet<AgentListener> listeners = agentsToAgentListeners.get(agent);

		if (listeners == null)
			listeners = new HashSet<AgentListener>();

		listeners.add(listener);

		agentsToAgentListeners.put(agent, listeners);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eis.EnvironmentInterfaceStandard#detachAgentListener(java.lang.String,
	 * eis.AgentListener)
	 */
	public void detachAgentListener(String agent, AgentListener listener) {

		if (registeredAgents.contains(agent) == false)
			return;

		HashSet<AgentListener> listeners = agentsToAgentListeners.get(agent);

		if (listeners == null || listeners.contains(agent) == false)
			return;

		listeners.remove(listener);

		// agentsToAgentListeners.put(agent,listeners);

	}

	/**
	 * Notifies agents about a percept.
	 * 
	 * @param percept
	 *            is the percept
	 * @param agents
	 *            is the array of agents that are to be notified about the
	 *            event. If the array is empty, all registered agents will be
	 *            notified. The array has to contain only registered agents.
	 * @throws AgentException
	 *             is thrown if at least one of the agents in the array is not
	 *             registered.
	 */
	protected void notifyAgents(Percept percept, String... agents)
			throws EnvironmentInterfaceException {

		// no listeners, no notification
		// BUG
		// if (agentsListeners.isEmpty())
		// return;

		// send to all registered agents
		if (agents == null) {

			for (String agent : registeredAgents) {

				HashSet<AgentListener> agentListeners = agentsToAgentListeners
						.get(agent);

				if (agentListeners == null)
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

			HashSet<AgentListener> agentListeners = agentsToAgentListeners
					.get(agent);

			if (agentListeners == null)
				continue;

			for (AgentListener listener : agentListeners) {

				listener.handlePercept(agent, percept);

			}

		}

	}

	/**
	 * Sends a percept to an agent/several agents via a given array of entities.
	 * 
	 * @param percept
	 * @param pEntities
	 *            an array of entities
	 * @throws EnvironmentInterfaceException
	 */
	protected void notifyAgentsViaEntity(Percept percept, String... pEntities)
			throws EnvironmentInterfaceException {

		// check
		for (String entity : pEntities)
			if (this.entities.contains(entity) == false)
				throw new EnvironmentInterfaceException("entity \"" + entity
						+ "\" does not exist.");

		// use all entities
		if (pEntities.length == 0) {

			for (String entity : entities) {
				for (Entry<String, HashSet<String>> entry : agentsToEntities
						.entrySet()) {

					if (entry.getValue().contains(entity))
						this.notifyAgents(percept, entry.getKey());

				}
			}

		}
		// use given array
		else {

			for (String entity : pEntities) {
				for (Entry<String, HashSet<String>> entry : agentsToEntities
						.entrySet()) {

					if (entry.getValue().contains(entity))
						this.notifyAgents(percept, entry.getKey());

				}
			}

		}

	}

	/**
	 * Notifies all listeners about an entity that is free.
	 * 
	 * @param entity
	 *            is the free entity.
	 * @param agents
	 *            is the list of agents that were associated
	 */
	protected void notifyFreeEntity(String entity, Collection<String> agents) {

		for (EnvironmentListener listener : environmentListeners) {

			listener.handleFreeEntity(entity, agents);

		}

	}

	/**
	 * Check all given entities and notify agents of free entities. If an entity
	 * is free, {@link notifyFreeEntity} is called.
	 * 
	 * @param entities
	 *            is list of entities to be checked.
	 * @param agents
	 *            is list of agents that were associated with the entity.
	 */
	private void notifyIfFree(Set<String> entities, List<String> agents) {
		List<String> free = getFreeEntities();
		for (String en : entities) {
			if (free.contains(en)) {
				notifyFreeEntity(en, agents);
			}
		}
	}

	/**
	 * Notifies all listeners about an entity that has been newly created.
	 * 
	 * @param entity
	 *            is the new entity.
	 */
	protected void notifyNewEntity(String entity) {

		for (EnvironmentListener listener : environmentListeners) {

			listener.handleNewEntity(entity);

		}

	}

	/**
	 * Notifies all listeners about an entity that has been deleted.
	 * 
	 * @param entity
	 *            is the deleted entity.
	 */
	protected void notifyDeletedEntity(String entity, Collection<String> agents) {

		for (EnvironmentListener listener : environmentListeners) {

			listener.handleDeletedEntity(entity, agents);

		}

	}

	/*
	 * Registering functionality. Registering and unregistering agents.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#registerAgent(java.lang.String)
	 */
	public void registerAgent(String agent) throws AgentException {

		if (registeredAgents.contains(agent))
			throw new AgentException("Agent " + agent
					+ " has already registered to the environment.");

		registeredAgents.add(agent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#unregisterAgent(java.lang.String)
	 */
	public void unregisterAgent(String agent) throws AgentException {

		// fail if agents is not registered
		if (!registeredAgents.contains(agent))
			throw new AgentException("Agent " + agent
					+ " has not registered to the environment.");

		// remove from mapping, might be null
		agentsToEntities.remove(agent);

		// remove listeners
		agentsToAgentListeners.remove(agent);

		// finally remove from registered list
		registeredAgents.remove(agent);

	}

	/*
	 * Entity functionality. Adding and removing entities.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#getAgents()
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<String> getAgents() {

		return (LinkedList<String>) registeredAgents.clone();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#getEntities()
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<String> getEntities() {

		return (LinkedList<String>) entities.clone();

	}

	/*
	 * Agents-entity-relation manipulation functionality.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#associateEntity(java.lang.String,
	 * java.lang.String)
	 */
	public void associateEntity(String agent, String entity)
			throws RelationException {

		// check if exists
		if (!entities.contains(entity))
			throw new RelationException("Entity \"" + entity
					+ "\" does not exist!");

		if (!registeredAgents.contains(agent))
			throw new RelationException("Agent \"" + agent
					+ "\" has not been registered!");

		// associate
		HashSet<String> ens = agentsToEntities.get(agent);
		if (ens == null) {

			ens = new HashSet<String>();
		}
		ens.add(entity);
		agentsToEntities.put(agent, ens);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#freeEntity(java.lang.String)
	 */
	public void freeEntity(String entity) throws RelationException,
			EntityException {

		// check if exists
		if (!entities.contains(entity))
			throw new EntityException("Entity \"" + entity
					+ "\" does not exist!");

		LinkedList<String> agents = new LinkedList<String>();

		// find the association and remove
		boolean associated = false;
		for (Entry<String, HashSet<String>> entry : agentsToEntities.entrySet()) {

			String agent = entry.getKey();
			HashSet<String> ens = entry.getValue();

			if (ens.contains(entity)) {

				ens.remove(entity);

				agentsToEntities.put(agent, ens);

				associated = true;

				if (agents.contains(agent) == false)
					agents.add(agent);

				break;
			}

		}

		// fail if entity has not been associated
		if (associated == false)
			throw new RelationException("Entity \"" + entity
					+ "\" has not been associated!");

		// notify
		notifyFreeEntity(entity, agents);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#freeAgent(java.lang.String)
	 */
	public void freeAgent(String agent) throws RelationException {

		// check if exists
		if (!registeredAgents.contains(agent))
			throw new RelationException("Agent \"" + agent
					+ "\" does not exist!");

		HashSet<String> ens = agentsToEntities.get(agent);

		LinkedList<String> agents = new LinkedList<String>();
		agents.add(agent);

		notifyIfFree(ens, agents);

		agentsToEntities.remove(agent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#freePair(java.lang.String,
	 * java.lang.String)
	 */
	public void freePair(String agent, String entity) throws RelationException {

		// check if exists
		if (!registeredAgents.contains(agent))
			throw new RelationException("Agent \"" + agent
					+ "\" does not exist!");

		// check if exists
		if (!entities.contains(entity))
			throw new RelationException("Entity \"" + entity
					+ "\" does not exist!");

		HashSet<String> ens = agentsToEntities.get(agent);

		if (ens == null || ens.contains(entity) == false)
			throw new RelationException("Agent \"" + agent
					+ " is not associated with entity \"" + entity + "\"!");

		// update mapping
		ens.remove(entity);
		agentsToEntities.put(agent, ens);

		LinkedList<String> agents = new LinkedList<String>();
		agents.add(agent);

		notifyIfFree(ens, agents);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eis.EnvironmentInterfaceStandard#getAssociatedEntities(java.lang.String)
	 */
	public HashSet<String> getAssociatedEntities(String agent)
			throws AgentException {

		if (registeredAgents.contains(agent) == false)
			throw new AgentException("Agent \"" + agent
					+ "\" has not been registered.");

		HashSet<String> ret = this.agentsToEntities.get(agent);

		if (ret == null)
			ret = new HashSet<String>();

		return ret;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eis.EnvironmentInterfaceStandard#getAssociatedAgents(java.lang.String)
	 */
	public HashSet<String> getAssociatedAgents(String entity)
			throws EntityException {

		if (entities.contains(entity) == false)
			throw new EntityException("Entity \"" + entity
					+ "\" has not been registered.");

		HashSet<String> ret = new HashSet<String>();

		for (Entry<String, HashSet<String>> entry : agentsToEntities.entrySet()) {

			if (entry.getValue().contains(entity))
				ret.add(entry.getKey());

		}

		return ret;

	}

	/**
	 * {@inheritDoc}
	 */
	public LinkedList<String> getFreeEntities() {
		LinkedList<String> free = getEntities();
		for (String agent : agentsToEntities.keySet()) {
			free.removeAll(agentsToEntities.get(agent));
		}
		return free;
	}

	/*
	 * Acting/perceiving functionality.
	 */
	public final Map<String, Percept> performAction(String agent,
			Action action, String... entities) throws ActException {

		// 1. unregistered agents cannot act
		if (registeredAgents.contains(agent) == false)
			throw new ActException(ActException.NOTREGISTERED);

		// get the associated entities
		HashSet<String> associatedEntities = agentsToEntities.get(agent);

		// 2. no associated entity/ies -> trivial reject
		if (associatedEntities == null || associatedEntities.size() == 0)
			throw new ActException(ActException.NOENTITIES);

		// entities that should perform the action
		HashSet<String> targetEntities = null;
		if (entities.length == 0) {

			targetEntities = associatedEntities;

		} else {

			targetEntities = new HashSet<String>();

			for (String entity : entities) {

				// 3. provided wrong entity
				if (associatedEntities.contains(entity) == false)
					throw new ActException(ActException.WRONGENTITY);

				targetEntities.add(entity);

			}

		}

		// 4. action could be not supported by the environment
		if (isSupportedByEnvironment(action) == false)
			throw new ActException(ActException.NOTSUPPORTEDBYENVIRONMENT);

		// 5. action could be not supported by the type of the entities
		for (String entity : entities) {

			String type;
			try {
				type = getType(entity);
			} catch (EntityException e) {
				e.printStackTrace();
				continue;
			}

			if (isSupportedByType(action, type) == false)
				throw new ActException(ActException.NOTSUPPORTEDBYTYPE);

		}

		// 6. action could be not supported by the entities themselves
		for (String entity : entities) {

			String type;
			try {
				type = getType(entity);
			} catch (EntityException e) {
				e.printStackTrace();
				continue;
			}

			if (isSupportedByEntity(action, entity) == false)
				throw new ActException(ActException.NOTSUPPORTEDBYENTITY);

		}

		Map<String, Percept> ret = new HashMap<String, Percept>();

		// 6. action could be not supported by the entities themselves
		for (String entity : targetEntities) {

			// TODO how is ensured that this method is called? ambiguity?

			try {
				Percept p = this.performEntityAction(entity, action);
				if (p != null)
					ret.put(entity, p);
			} catch (Exception e) {
				if (!(e instanceof ActException)) {
					throw new ActException(ActException.FAILURE,
							"failure performing action", e);
				}

				// exception was ok, rethrow
				throw (ActException) e;
			}

		}

		return ret;

	}

	// TODO maybe use isConnencted here
	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#getAllPercepts(java.lang.String,
	 * java.lang.String[])
	 */
	public Map<String, Collection<Percept>> getAllPercepts(String agent,
			String... entities) throws PerceiveException,
			NoEnvironmentException {

		// fail if the environment does not run
		if (state != EnvironmentState.RUNNING)
			throw new PerceiveException("Environment does not run but is "
					+ state);

		// fail if ther agent is not registered
		if (registeredAgents.contains(agent) == false)
			throw new PerceiveException("Agent \"" + agent
					+ "\" is not registered.");

		// get the associated entities
		HashSet<String> associatedEntities = agentsToEntities.get(agent);

		// fail if there are no associated entities
		if (associatedEntities == null || associatedEntities.size() == 0)
			throw new PerceiveException("Agent \"" + agent
					+ "\" has no associated entities.");

		// return value
		Map<String, Collection<Percept>> ret = new HashMap<String, Collection<Percept>>();

		// gather all percepts
		if (entities.length == 0) {

			for (String entity : associatedEntities) {

				// get all percepts
				LinkedList<Percept> all = getAllPerceptsFromEntity(entity);

				// add annonation
				for (Percept p : all)
					p.setSource(entity);

				// done
				ret.put(entity, all);

			}

		}
		// only from specified entities
		else {

			for (String entity : entities) {

				if (associatedEntities.contains(entity) == false)
					throw new PerceiveException("Entity \"" + entity
							+ "\" has not been associated with the agent \""
							+ agent + "\".");

				// get all percepts
				LinkedList<Percept> all = getAllPerceptsFromEntity(entity);

				// add annonation
				for (Percept p : all)
					p.setSource(entity);

				// done
				ret.put(entity, all);

			}

		}

		return ret;

	}

	/**
	 * Gets all percepts of an entity.
	 * <p/>
	 * This method must be overridden.
	 * 
	 * @param entity
	 *            is the entity whose percepts should be retrieved.
	 * @return a list of percepts.
	 * @throws NoEnvironmentException
	 */
	protected abstract LinkedList<Percept> getAllPerceptsFromEntity(
			String entity) throws PerceiveException, NoEnvironmentException;

	/**
	 * Returns true if the action is supported by the environment.
	 * 
	 * @param action
	 * @return true if the action is supported by the environment
	 */
	protected abstract boolean isSupportedByEnvironment(Action action);

	/**
	 * Returns true if the action is supported by the type.
	 * 
	 * @param action
	 * @return Returns true if the action is supported by the type
	 */
	protected abstract boolean isSupportedByType(Action action, String type);

	/**
	 * Returns true if the action is supported by the entity.
	 * 
	 * @param action
	 * @return true if action supported by entity.
	 */
	protected abstract boolean isSupportedByEntity(Action action, String entity);

	/**
	 * @param action
	 * @return
	 */
	// protected abstract boolean areParametersCorrect(Action action);

	/**
	 * @param entity
	 * @param action
	 * @return Percept that is result of the action.
	 * @throws ActException
	 */
	protected abstract Percept performEntityAction(String entity, Action action)
			throws ActException;

	/*
	 * Misc functionality.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#getType(java.lang.String)
	 */
	public String getType(String entity) throws EntityException {

		if (!this.entities.contains(entity))
			throw new EntityException("Entity \"" + entity
					+ "\" does not exist!");

		String type = entitiesToTypes.get(entity);

		if (type == null)
			type = "unknown";

		return type;

	}

	/**
	 * Adds an entity to the environment.
	 * 
	 * @param entity
	 *            is the identifier of the entity that is to be added.
	 * @throws PlatformException
	 *             is thrown if the entity already exists.
	 */
	protected void addEntity(String entity) throws EntityException {

		// fail if entity does exist
		if (entities.contains(entity))
			throw new EntityException("Entity \"" + entity
					+ "\" does already exist");

		// add
		entities.add(entity);

		// notify
		notifyNewEntity(entity);

	}

	/**
	 * Adds an entity to the environment.
	 * 
	 * @param entity
	 *            is the identifier of the entity that is to be added.
	 * @param type
	 *            is the type of the entity.
	 * @throws PlatformException
	 *             is thrown if the entity already exists.
	 */
	protected void addEntity(String entity, String type) throws EntityException {

		// fail if entity does exist
		if (entities.contains(entity))
			throw new EntityException("Entity \"" + entity
					+ "\" does already exist");

		// add
		entities.add(entity);

		// set type
		this.setType(entity, type);

		// notify
		notifyNewEntity(entity);

	}

	/**
	 * Deletes an entity, by removing its id from the internal list, and
	 * disassociating it from the respective agent.
	 * 
	 * @param entity
	 *            the id of the entity that is to be removed.
	 * @throws PlatformException
	 *             if the agent does not exist.
	 */
	protected void deleteEntity(String entity) throws EntityException,
			RelationException {

		// check if exists
		if (!entities.contains(entity))
			throw new EntityException("Entity \"" + entity
					+ "\" does not exist!");

		LinkedList<String> agents = new LinkedList<String>();

		// find the association and remove
		// boolean associated = false;
		for (Entry<String, HashSet<String>> entry : agentsToEntities.entrySet()) {

			String agent = entry.getKey();
			HashSet<String> ens = entry.getValue();

			if (ens.contains(entity)) {

				ens.remove(entity);

				agentsToEntities.put(agent, ens);

				// associated = true;

				if (agents.contains(agent) == false)
					agents.add(agent);

				break;
			}

		}

		// fail if entity has not been associated
		/*
		 * if( associated == false) try { throw new
		 * RelationException("Entity \"" + entity +
		 * "\" has not been associated!"); } catch (RelationException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */

		// finally delete
		entities.remove(entity);
		if (this.entitiesToTypes.containsKey(entity))
			this.entitiesToTypes.remove(entity);

		// notify
		notifyDeletedEntity(entity, agents);

	}

	/**
	 * Sets the type of an entity.
	 * 
	 * @param entity
	 *            is the entity
	 * @param type
	 *            is the respective type of the entity
	 * @throws EntityException
	 *             is thrown if the entity doas nox exist or if it already has a
	 *             type.
	 */
	public void setType(String entity, String type) throws EntityException {

		if (!entities.contains(entity))
			throw new EntityException("Entity \"" + entity
					+ "\" does not exist!");

		if (entitiesToTypes.get(entity) != null)
			throw new EntityException("Entity \"" + entity
					+ "\" already has a type!");

		entitiesToTypes.put(entity, type);

	}

	/*
	 * Management functionality.
	 */

	/**
	 * Sets the state of the environment-interface. Firstly the state-transition
	 * is tested if it is valid. If so, the state will be changed and all
	 * environment-listeners will be notified.
	 * 
	 * @param state
	 *            the new state
	 * @throws ManagementException
	 *             if thrown if the state transition is not valid
	 */
	protected void setState(EnvironmentState state) throws ManagementException {

		// TODO is state transition valid?
		if (isStateTransitionValid(this.state, state) == false)
			throw new ManagementException("Invalid state transition from "
					+ this.state.toString() + " to  " + state.toString());

		// set the state
		this.state = state;

		// notify listeners
		for (EnvironmentListener listener : environmentListeners)
			listener.handleStateChange(state);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#isStateTransitionValid(eis.iilang.
	 * EnvironmentState, eis.iilang.EnvironmentState)
	 */
	@Override
	public boolean isStateTransitionValid(EnvironmentState oldState,
			EnvironmentState newState) {

		if (oldState == EnvironmentState.INITIALIZING
				&& newState == EnvironmentState.INITIALIZING)
			return true;
		if (oldState == EnvironmentState.INITIALIZING
				&& newState == EnvironmentState.PAUSED)
			return true;
		if (oldState == EnvironmentState.INITIALIZING
				&& newState == EnvironmentState.KILLED)
			return true;
		if (oldState == EnvironmentState.PAUSED
				&& newState == EnvironmentState.RUNNING)
			return true;
		if (oldState == EnvironmentState.RUNNING
				&& newState == EnvironmentState.PAUSED)
			return true;
		if (oldState == EnvironmentState.PAUSED
				&& newState == EnvironmentState.KILLED)
			return true;
		if (oldState == EnvironmentState.RUNNING
				&& newState == EnvironmentState.KILLED)
			return true;

		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#getState()
	 */
	@Override
	public EnvironmentState getState() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#isInitSupported()
	 */
	@Override
	public boolean isInitSupported() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#isKillSupported()
	 */
	@Override
	public boolean isKillSupported() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#isPauseSupported()
	 */
	@Override
	public boolean isPauseSupported() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#isStartSupported()
	 */
	@Override
	public boolean isStartSupported() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#init(java.util.Map)
	 */
	@Override
	public void init(Map<String, Parameter> parameters)
			throws ManagementException {
		if (isInitSupported() == false)
			throw new ManagementException("init is not supported");
		setState(EnvironmentState.INITIALIZING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#kill()
	 */
	@Override
	public void kill() throws ManagementException {
		if (isKillSupported() == false)
			throw new ManagementException("kill is not supported");
		setState(EnvironmentState.KILLED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#pause()
	 */
	@Override
	public void pause() throws ManagementException {
		if (isPauseSupported() == false)
			throw new ManagementException("pause is not supported");
		setState(EnvironmentState.PAUSED);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#start()
	 */
	@Override
	public void start() throws ManagementException {
		if (isStartSupported() == false)
			throw new ManagementException("start is not supported");
		setState(EnvironmentState.RUNNING);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#queryProperty()
	 */
	@Override
	public String queryProperty(String property) throws QueryException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eis.EnvironmentInterfaceStandard#queryEntityProperty()
	 */
	@Override
	public String queryEntityProperty(String entity, String property) {

		return null;

	}

}