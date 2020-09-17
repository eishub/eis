package eis;

import java.util.Collection;
import java.util.Map;

import eis.eis2java.environment.AbstractEnvironment;
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
 * This interface is the main-interface of EIS. All environment-interfaces have
 * to implement this interface and its methods.
 * <p>
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
 * <h1>Thread Safety</h1> This interface does not pose any requirements on
 * thread safety. Therefore it can not be assumed that an environment behind
 * this interface is thread safe.
 *
 * <h1>Initialization</h1> Implementors of this must have an empty default
 * constructor, so that an instance of the interface can be created without any
 * additional requirements. Your environment state can stay
 * {@link EnvironmentState#INITIALIZING} and will become usable only after
 * {@link #init(Map)} is called.
 */
public interface EnvironmentInterfaceStandard {
	/**
	 * Attaches an environment-listener.
	 * <p>
	 * If the listener is already attached, nothing will happen.
	 *
	 * @param listener is the listener
	 */
	void attachEnvironmentListener(EnvironmentListener listener);

	/**
	 * Detaches an environment-listener.
	 * <p>
	 * If the listener is not attached, nothing will happen.
	 *
	 * @param listener is the listener
	 */
	void detachEnvironmentListener(EnvironmentListener listener);

	/**
	 * Attaches an agent-listener.
	 * <p>
	 * If the agent has not been registered nothing will happen.
	 *
	 * @param agent    is the agent
	 * @param listener is the listener of the agent
	 */
	void attachAgentListener(String agent, AgentListener listener);

	/**
	 * Detaches an agent-listener.
	 * <p>
	 * If the agent has not been registered and/or the listener does not exist
	 * nothing will happen.
	 *
	 * @param agent    is the agent
	 * @param listener is the listener of the agent
	 */
	void detachAgentListener(String agent, AgentListener listener);

	/**
	 * Registers an agent to the environment.
	 *
	 * @param agent the identifier of the agent.
	 * @throws AgentException if the agent has already been registered.
	 */
	void registerAgent(String agent) throws AgentException;

	/**
	 * Unregisters an agent from the environment.
	 *
	 * @param agent the identifier of the agent.
	 * @throws AgentException if the agent has not registered before.
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
	 * @param agent  the id of the agent.
	 * @param entity the id of the entity.
	 * @throws RelationException if the entity is not free, and if it or the agent
	 *                           does not exist.
	 */
	void associateEntity(String agent, String entity) throws RelationException;

	/**
	 * Frees an entity from its associated agent(s). This will re=announce an entity
	 * as free and therefore can only be used when environment is
	 * {@link EnvironmentState#RUNNING} or {@link EnvironmentState#PAUSED}
	 *
	 * @param entity the id of the entity to be freed.
	 * @throws RelationException if the entity can't be freed
	 * @throws EntityException   is thrown if the entity does not exist, or if it is
	 *                           not associated.
	 */
	void freeEntity(String entity) throws RelationException, EntityException;

	/**
	 * Frees an agent from the agents-entities-relation. This will re=announce an
	 * entity as free and therefore should only be used when environment is
	 * {@link EnvironmentState#RUNNING} or {@link EnvironmentState#PAUSED}
	 *
	 * @param agent is the agent to be freed.
	 * @throws RelationException is thrown if the agent has not been registered.
	 * @throws EntityException   if the entity can't be freed
	 */
	void freeAgent(String agent) throws RelationException, EntityException;

	/**
	 * Removes a pair from the agents-entities-relation. This un-associates the
	 * agent from the entity, and is the reverse of
	 * {@link #associateEntity(String, String)}. This should re=announce an entity
	 * as free and therefore should only be used when environment is
	 * {@link EnvironmentState#RUNNING} or {@link EnvironmentState#PAUSED}.
	 *
	 * @param agent  the agent to be freed
	 * @param entity the entity to be freed
	 * @throws RelationException is thrown when the agent is not associated with the
	 *                           entity
	 * @throws EntityException   if the entity can't be freed
	 */
	void freePair(String agent, String entity) throws RelationException, EntityException;

	/**
	 * @param agent is the agent.
	 * @return the entities associated to a given agent.
	 * @throws AgentException if agent does not exist
	 */
	Collection<String> getAssociatedEntities(String agent) throws AgentException;

	/**
	 * Returns the agents associated to a given entity.
	 *
	 * @param entity is the entity.
	 * @return a set of agents.
	 * @throws EntityException if entity does not exist
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
	 * @param agent    is the agent whose associated entities are supposed to act.
	 * @param action   is the action. The action's name determines the name of the
	 *                 method that is called.
	 * @param entities is an array of entities through which an agent is supposed to
	 *                 act. If the array is empty, all entities are used.
	 * @return a map of action-results. Keys are the entity name that gave the
	 *         result percept. Only add items if the percept is not null. If an
	 *         action has a null result, just do not add the result to the map.
	 * @throws ActException           is thrown if the agent has not been
	 *                                registered, if the agent has no associated
	 *                                entities, if at least one of the given
	 *                                entities is not associated, or if at least one
	 *                                one the actions fails. If an agent should be
	 *                                notified of failure to execute the action but
	 *                                the entity and agent can proceed working, that
	 *                                should happen by means of a percept and not by
	 *                                throwing.
	 * @throws NoEnvironmentException if the interface is not connected to an
	 *                                environment.
	 */
	Map<String, Percept> performAction(String agent, Action action, String... entities) throws ActException;

	/**
	 * Gets all percepts.
	 * <p>
	 * Either returns the percepts of all associated entities of or a subset.
	 * <p>
	 * This function should be called only when {@link #getState()} is
	 * {@link EnvironmentState#RUNNING} or {@link EnvironmentState#PAUSED}.
	 * <p>
	 * <em>NOTE</em> In many environments the return value depends on previous calls
	 * to getAllPercepts. There may be special percepts that are provided only the
	 * first time getAllPercepts is called. There may be percepts that are sent only
	 * once. There may percepts that are sent if they were not sent the previous
	 * time already. The exact behaviour is to be defined in environment's
	 * documentation. Both {@link EIDefaultImpl} and {@link AbstractEnvironment}
	 * implements such a more detailed version of getAllPercepts.
	 *
	 *
	 * @param agent    the agent that requests the percepts.
	 * @param entities the eneities that this agent is associated with.
	 * @return a list of percepts
	 * @throws PerceiveException      if the agent is not registered or if the
	 *                                agents requests percepts from an entity that
	 *                                is not associated. If an agent should be
	 *                                notified of failure but the entity and agent
	 *                                can proceed working, that should happen by
	 *                                means of a percept and not by throwing.
	 * @throws NoEnvironmentException if an attempt to perform an action or to
	 *                                retrieve percepts has failed
	 */
	Map<String, Collection<Percept>> getAllPercepts(String agent, String... entities)
			throws PerceiveException, NoEnvironmentException;

	/**
	 * Checks whether a state transition between two states is possible or not.
	 *
	 * @param oldState is the old state.
	 * @param newState is the new state.
	 * @return true iff transition valid
	 */
	// TODO needs to go to the default implementation
	boolean isStateTransitionValid(EnvironmentState oldState, EnvironmentState newState);

	/**
	 * Initializes the environment(-interface) with a set of key-value-pairs.
	 * Normally, here an environment initializes its environment and moves to the
	 * {@link EnvironmentState#PAUSED} or {@link EnvironmentState#RUNNING} state.
	 * After that it then proceeds calling
	 * {@link EnvironmentListener#handleNewEntity(String)} for all entities.
	 *
	 * @param parameters the parameters to use to initialize the environment
	 * @throws ManagementException is thrown either when the initializing is not
	 *                             supported or the parameters are wrong.
	 */
	void init(Map<String, Parameter> parameters) throws ManagementException;

	/**
	 * Soft resset. Resets the environment(-interface) with a set of
	 * key-value-pairs. Resetting the environment means the following
	 * <ul>
	 * <li>set the environment state to {@link EnvironmentState#INITIALIZING}.
	 * <li>Reset environment to its initial state (initial map for instance)
	 * <li>Reset all entities to their initial state
	 * <li>Prepare to send the entities the initial percept (note: "initial
	 * percepts" is not a contract with EIS but might a contract with the specific
	 * environment)
	 * <li>keep all agents attached, do not remove entities or create new ones etc.
	 * </ul>
	 *
	 * @param parameters the parameters to use to initialize the environment
	 *
	 * @throws ManagementException is thrown either when the initializing is not
	 *                             supported or the parameters are wrong.
	 */
	void reset(Map<String, Parameter> parameters) throws ManagementException;

	/**
	 * Starts the environment(-interface). Should change the state to
	 * {@link EnvironmentState#RUNNING}.
	 *
	 * @throws ManagementException when starting is not supported
	 */
	void start() throws ManagementException;

	/**
	 * Pauses the environment(-interface). Should change the state to
	 * {@link EnvironmentState#PAUSED}
	 *
	 * @throws ManagementException when pausing is not supported
	 */
	void pause() throws ManagementException;

	/**
	 * Kills the environment. Environment should change to
	 * {@link EnvironmentState#KILLED}
	 *
	 * @throws ManagementException when killing is not supported.
	 */
	void kill() throws ManagementException;

	/**
	 * Retrieves the state of the environment-interface.
	 *
	 * @return current {@link EnvironmentState}.
	 */
	EnvironmentState getState();

	/**
	 * Returns true when the init-command is supported.
	 *
	 * @return true iff {@link #init(Map)} is supported.
	 */
	boolean isInitSupported();

	/**
	 * Returns true when the start-command is supported.
	 *
	 * @return true iff {@link #start()} is supported
	 */
	boolean isStartSupported();

	/**
	 * Returns true when the pause-command is supported.
	 *
	 * @return true iff {@link #pause()} is supported
	 */
	boolean isPauseSupported();

	/**
	 * Returns true when the kill-command is supported.
	 *
	 * @return iff {@link #kill()} is supported
	 */
	boolean isKillSupported();

	/**
	 * @return the EIS-runtime-version that is compatible with the implemented
	 *         environment
	 */
	String requiredVersion();

	/**
	 * Queries the interface of a certain property.
	 *
	 * @param property property to query
	 * @return String defining the propery, or null if no such property.
	 * @throws QueryException if property can't be queried
	 */
	String queryProperty(String property) throws QueryException;

	/**
	 * Queries an entity of a certain property.
	 *
	 * @param entity   the entity to query
	 * @param property the property to query
	 * @return String property value for given entity/property combination, or null
	 *         if no such property.
	 * @throws QueryException if query failed
	 */
	String queryEntityProperty(String entity, String property) throws QueryException;
}