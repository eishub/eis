package eis;

import java.util.HashSet;
import java.util.LinkedList;

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
import eis.iilang.Percept;

public interface EnvironmentInterfaceStandard {

	/**
	 * 
	 */
	static String version = "0.2rc1";

	/**
	 * Attaches an environment-listener.
	 * <p/> 
	 * If the listener is already attached, nothing will happen.
	 * 
	 * @param listener
	 */
	void attachEnvironmentListener(EnvironmentListener listener);

	/**
	 * Detaches an environment-listener.
	 * <p/>
	 * If the listener is not attached, nothing will happen.
	 * 
	 * @param listener
	 */
	void detachEnvironmentListener(EnvironmentListener listener);

	/**
	 * Attaches an agent-listener.
	 * <p/>
	 * If the agent has not been registered nothing will happen.
	 * 
	 * @param agent
	 * @param listener
	 */
	void attachAgentListener(String agent, AgentListener listener);

	/**
	 * Detaches an agent-listener.
	 * <p/>
	 * If the agent has not been registered and/or the listener does not exist nothing will happen.
	 * @param agent
	 * @param listener
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
	@SuppressWarnings("unchecked")
	LinkedList<String> getAgents();

	/**
	 * Retrieves the list of entities.
	 * 
	 * @return a list of entity-ids.
	 */
	LinkedList<String> getEntities();

	/**
	 * Associates an entity with an agent.
	 * 
	 * @param agent the id of the agent.
	 * @param entity the id of the entity.
	 * @throws PlatformException if the entity is not free, and if it or the agent does not exist.
	 */
	void associateEntity(String agent, String entity) throws RelationException;

	/**
	 * Frees an entity from its associated agent.
	 * 
	 * @param entity the id of the entity to be freed.
	 * @throws PlatformException is thrown if the entity does not exist, or if it is not associated.
	 */
	void freeEntity(String entity) throws RelationException;

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
	 * @param agent
	 * @param entity
	 * @throws RelationException
	 */
	void freePair(String agent, String entity) throws RelationException;

	/**
	 * Returns the entities associated to a given agent.
	 * 
	 * @param agent is the agent.
	 * @return a set of entities.
	 * @throws AgentException 
	 */
	HashSet<String> getAssociatedEntities(String agent) throws AgentException;

	/**
	 * Returns the agents associated to a given entity.
	 * 
	 * @param entity is the entity.
	 * @return a set of agents.
	 * @throws AgentException 
	 */
	HashSet<String> getAssociatedAgents(String entity) throws EntityException;

	/**
	 * Retrieves the list of free entities.
	 * 
	 * @return a list of entity-ids.
	 */
	LinkedList<String> getFreeEntities();

	/**
	 * Lets an agent perform an agent.
	 * <p/>
	 * This method firstly determines the entities through which the agent should act.
	 * Secondly Java-reflection is used to determine the methods that belong to the
	 * given action. Finally, those methods are invoked and the return-values are gathered.
	 * 
	 * @param agent is the agent that is supposed to act.
	 * @param action is the action. The action's name determines the name of the method that is called.
	 * @param entities is an array of entities through which an agent is supposed to act. If the 
	 * array is empty, all entities are used.
	 * @return a list of action-results.
	 * @throws PerceiveOrActFailureException is thrown if the agent has not been registered,
	 * if the agent has no associated entities, if at least one of the given entities is not 
	 * associated, or if at least one one the actions fails.
	 */
	LinkedList<Percept> performAction(String agent, Action action,
			String... entities) throws ActException, NoEnvironmentException;

	/** 
	 * Gets all percepts.
	 * <p/>
	 * Either returns the percepts of all associated entities of or a subset.
	 * 
	 * @param agent the agent that requests the percepts.
	 * @return a list of percepts
	 * @throws PerceiveException if the agent is not registered or if the agents requests percepts from an entity that is not associated.
	 */
	LinkedList<Percept> getAllPercepts(String agent, String... entities)
			throws PerceiveException, NoEnvironmentException;

	/**
	 * Invoked to manage the environment and/or its execution.
	 * 
	 * @param command is the command that is to be executed.
	 */
	void manageEnvironment(EnvironmentCommand command)
			throws ManagementException, NoEnvironmentException;

	/**
	 * Releases the environment interface.
	 */
	void release();

	/**
	 * Returns true if the interface is connected to the environment
	 * @return true or false
	 */
	boolean isConnected();

	/**
	 * Returns the type of an entity.
	 * 
	 * @param entity is the entity
	 * @return either the type of the entity
	 * @throws EntityException is thrown if the entity does not exist.
	 */
	String getType(String entity) throws EntityException;

	String requiredVersion();
	
}