package eis;

import java.util.Collection;

import eis.iilang.EnvironmentState;

/**
 * This interface has to be implemented in order to notify about changes with
 * respect to entities and about so-called environment-events.
 * Environment-events inform about the change in the state of execution of the
 * environment.
 * 
 * @author tristanbehrens
 *
 */
public interface EnvironmentListener {

	/**
	 * Handles a new state.
	 * 
	 * @param newState
	 */
	void handleStateChange(EnvironmentState newState);

	/**
	 * Handles the event that an entity has been freed.
	 * 
	 * @param entity
	 *            is the entity that has been freed
	 * @param agents
	 *            is the list of entities that were associated
	 */
	void handleFreeEntity(String entity, Collection<String> agents);

	/**
	 * Handles the event that an entity has been deleted.
	 * 
	 * @param entity
	 *            is the entity that has been deleted
	 * @param agents
	 *            are the entities that were associated
	 */
	void handleDeletedEntity(String entity, Collection<String> agents);

	/**
	 * Handles the event that an entity has been newly created. Announce new
	 * entities only when the environment is ready to handle
	 * {@link #getAllPercepts(String, String...)}.
	 * 
	 * @param entity
	 *            is the entity that has been created.
	 */
	void handleNewEntity(String entity);

}
