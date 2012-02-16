package eis;

import java.util.Collection;
import java.util.Map;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
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
 * This interface is the main-interface of EIS. 
 * All environment-interfaces have to implement this interface and its methods.
 * <p/>
 * Each environment interface should implement these functionalities:
 * <ul>
 * <li>attaching and detaching listeners;</li>
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
public interface EnvironmentInterfaceStandard {

	/**
	 * Attaches an environment-listener.
	 * <p/> 
	 * If the listener is already attached, nothing will happen.
	 * 
	 * @param listener is the listener
	 */
	void attachEnvironmentListener(EnvironmentListener listener);

	/**
	 * Detaches an environment-listener.
	 * <p/>
	 * If the listener is not attached, nothing will happen.
	 * 
	 * @param listener is the listener
	 */
	void detachEnvironmentListener(EnvironmentListener listener);

	/**
	 * Attaches an agent-listener.
	 * <p/>
	 * If the agent has not been registered nothing will happen.
	 * 
	 * @param agent is the agent
	 * @param listener is the listener of the agent
	 */
	void attachAgentListener(String agent, AgentListener listener);

	/**
	 * Detaches an agent-listener.
	 * <p/>
	 * If the agent has not been registered and/or the listener does not exist nothing will happen.
	 * @param agent is the agent
	 * @param listener is the listener of the agent
	 */
	void detachAgentListener(String agent, AgentListener listener);

	/**
	 * Registers an agent to the environment.
	 * 
	 * @param agent the identifier of the agent.
	 * @throws PlatformException if the agent has already been registered.
	 */
	void registerAgent(String agent) throws AgentException;

	/**
	 * Unregisters an agent from the environment.
	 * 
	 * @param agent the identifier of the agent.
	 * @throws AbstractException if the agent has not registered before.
	 */
	void unregisterAgent(String agent) throws AgentException;

	/**
	 * Retrieves the list of registered agents.
	 * 
	 * @return a list of agent-ids.
	 */
	Collection<String> getAgents();

	/**
	 * Retrieves the list of entities.
	 * 
	 * @return a list of entity-ids.
	 */
	Collection<String> getEntities();

	/**
	 * Associates an entity with an agent.
	 * 
	 * @param agent the id of the agent.
	 * @param entity the id of the entity.
	 * @throws PlatformException if the entity is not free, and if it or the agent does not exist.
	 */
	void associateEntity(String agent, String entity) throws RelationException;

	/**
	 * Frees an entity from its associated agent(s).
	 * 
	 * @param entity the id of the entity to be freed.
	 * @throws EntityException 
	 * @throws PlatformException is thrown if the entity does not exist, or if it is not associated.
	 */
	void freeEntity(String entity) throws RelationException, EntityException;

	/**
	 * Frees an agent from the agents-entities-relation.
	 * 
	 * @param agent is the agent to be freed.
	 * @throws RelationException is thrown if the agent has not been registered.
	 */
	void freeAgent(String agent) throws RelationException;

	/**
	 * Removes a pair from the agents-entities-relation.
	 * 
	 * @param agent the agent to be freed
	 * @param entity the entity to be freed 
	 * @throws RelationException is thrown when the agent is not associated with the entity
	 */
	void freePair(String agent, String entity) throws RelationException;

	/**
	 * Returns the entities associated to a given agent.
	 * 
	 * @param agent is the agent.
	 * @return a set of entities.
	 * @throws AgentException 
	 */
	Collection<String> getAssociatedEntities(String agent) throws AgentException;

	/**
	 * Returns the agents associated to a given entity.
	 * 
	 * @param entity is the entity.
	 * @return a set of agents.
	 * @throws AgentException 
	 */
	Collection<String> getAssociatedAgents(String entity) throws EntityException;

	/**
	 * Retrieves the list of free entities.
	 * 
	 * @return a list of entity-ids.
	 */
	Collection<String> getFreeEntities();

	/**
	 * Returns the type of an entity.
	 * 
	 * @param entity is the entity
	 * @return either the type of the entity
	 * @throws EntityException is thrown if the entity does not exist.
	 */
	String getType(String entity) throws EntityException;

	/**
	 * Lets an agent perform an action.
     * 
	 * @param agent is the agent whose associated entities are supposed to act.
	 * @param action is the action. The action's name determines the name of the method that is called.
	 * @param entities is an array of entities through which an agent is supposed to act. If the 
	 * array is empty, all entities are used.
	 * @return a map of action-results.
	 * @throws ActException is thrown if the agent has not been registered,
	 * if the agent has no associated entities, if at least one of the given entities is not 
	 * associated, or if at least one one the actions fails.
	 * @throws NoEnvironmentException if the interface is not connected to an environment.
	 */
	Map<String,Percept> performAction(String agent, Action action,
			String... entities) throws ActException;
	
	/** 
	 * Gets all percepts.
	 * <p/>
	 * Either returns the percepts of all associated entities of or a subset.
	 * 
	 * @param agent the agent that requests the percepts.
	 * @return a list of percepts
	 * @throws PerceiveException if the agent is not registered or if the agents requests percepts from an entity that is not associated.
	 * @throws NoEnvironmentException 
	 */
	Map<String,Collection<Percept>> getAllPercepts(String agent, String... entities)
			throws PerceiveException, NoEnvironmentException;

	/**
	 * Checks whether a state transition between two states is possible or not.
	 * 
	 * @param oldState is the old state.
	 * @param newState is the new state.
	 * @return
	 */
	// TODO needs to go to the default implementation
	boolean isStateTransitionValid(EnvironmentState oldState, EnvironmentState newState);
	
	/**
	 * Initializes the environment(-interface) with a set of key-value-pairs.
	 * @param parameters
	 * @throws ManagementException is thrown either when the initializing is not supported or the parameters are wrong.
	 */
	void init(Map<String,Parameter> parameters) throws ManagementException;

	/**
	 * Resets the environment(-interface) with a set of key-value-pairs.
	 * @param parameters
	 * @throws ManagementException is thrown either when the initializing is not supported or the parameters are wrong.
	 */
	void reset(Map<String,Parameter> parameters) throws ManagementException;
	
	/**
	 * Starts the environment(-interface).
	 * @throws ManagementException when starting is not supported
	 */
	void start() throws ManagementException;
	
	/**
	 * Pauses the environment(-interface).
	 * @throws ManagementException when pausing is not supported
	 */
	void pause() throws ManagementException;
	
	/**
	 * Kills the environment.
	 * @throws ManagementException when killing is not supported.
	 */
	void kill() throws ManagementException;

	/**
	 * Retrieves the state of the environment-interface.
	 * @return
	 */
	EnvironmentState getState();

	/**
	 * Returns true when the init-command is supported.
	 * @return
	 */
	boolean isInitSupported();
	
	/**
	 * Returns true when the start-command is supported.
	 * @return
	 */
	boolean isStartSupported();
	
	/**
	 * Returns true when the pause-command is supported.
	 * @return
	 */
	boolean isPauseSupported();	
	
	/**
	 * Returns true when the kill-command is supported.
	 * @return
	 */
	boolean isKillSupported();
	
	/**
	 * Returns the EIS-runtime-version that is compatible.
	 */
	String requiredVersion();
	
	/**
	 * Queries the interface of a certain property.
	 * @param property
	 * @return
	 */
	String queryProperty(String property) throws QueryException;

	/**
	 * Queries an entity of a certain property.
	 * @param property
	 * @return
	 */
	String queryEntityProperty(String entity, String property) throws QueryException;
	
}