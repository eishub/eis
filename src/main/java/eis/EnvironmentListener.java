package eis;

import eis.iilang.EnvironmentEvent;

/**
 * This interface has to be implemented in order to notify 
 * about changes with respect to entities and about so-called
 * environment-events. Environment-events inform about the
 * change in the state of execution of the environment.
 * 
 * @author tristanbehrens
 *
 */
public interface EnvironmentListener {

	/**
	 * Handles an environment-event.
	 * 
	 * @param event is the specific event
	 */
	void handleEnvironmentEvent(EnvironmentEvent event);

	/**
	 * Handles the event that an entity has been freed.
	 * @param entity is the entity that has been freed
	 */
	void handleFreeEntity(String entity);
	
	/**
	 * Handles the event that an entity has been deleted.
	 * @param entity is the entity that has been deleted
	 */
	void handleDeletedEntity(String entity);

	/**
	 * Handles the event that an entity has been newly created.
	 * @param entity is the entity that has been created.
	 */
	void handleNewEntity(String entity);
	
}
