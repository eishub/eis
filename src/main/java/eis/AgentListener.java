package eis;

import eis.iilang.Percept;

/**
 * The environment interface can inform connected objects about changes using
 * this interface.
 * 
 * @author tristanbehrens
 *
 */
public interface AgentListener {

	/**
	 * Handles a percept that is sent to a specific agent-
	 * 
	 * @param agent the recipient of the percept
	 * @param percept the percept itself
	 */
	void handlePercept(String agent, Percept percept);

}
