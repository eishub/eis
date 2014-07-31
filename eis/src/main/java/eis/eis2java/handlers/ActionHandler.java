package eis.eis2java.handlers;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Percept;

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
	 * @return true iff the agent supports the given action.
	 */
	public abstract boolean isSupportedByEntity(Action action);

	/**
	 * Executes the given action on the agent.
	 * 
	 * @param action
	 *            to execute.
	 * @return a percept optionally containing the result of the action or null.
	 * @throws ActException
	 *             when the action could not be executed.
	 */
	public abstract Percept performAction(Action action) throws ActException;

}