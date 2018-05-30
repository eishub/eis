package eis.eis2java.handlers;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.ActException;
import eis.iilang.Action;

/**
 * The {@link AbstractEnvironment} delegates the actual execution of an action
 * on an agent to the ActionHandler.
 * 
 * 
 * @author mpkorstanje
 * 
 */
public abstract class ActionHandler {

	/**
	 * True iff the agent supports the given action.
	 * 
	 * @param action
	 *            the action to be checked
	 * @return true iff the agent supports the given action.
	 */
	public abstract boolean isSupportedByEntity(Action action);

	/**
	 * Executes the given action on the agent.
	 * 
	 * @param action
	 *            to execute.
	 * @throws ActException
	 *             when the action could not be executed.
	 */
	public abstract void performAction(Action action) throws ActException;

	/**
	 * Called when the entity is reset.
	 */
	public abstract void reset();

}