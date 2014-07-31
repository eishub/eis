package eis;

import eis.iilang.Percept;

/**
 * In order to receive percepts-as-notifications connected objects
 * have to implement this interface.
 * 
 * @author tristanbehrens
 *
 */
public interface AgentListener {

	/**
	 * Handles a percept that is sent to a specific agent.
	 * 
	 * @param agent the recipient of the percept
	 * @param percept the percept itself
	 */
	void handlePercept(String agent, Percept percept);

}
