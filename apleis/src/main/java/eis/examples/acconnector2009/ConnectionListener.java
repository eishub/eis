package eis.examples.acconnector2009;

import java.util.LinkedList;

import eis.iilang.Percept;

/**
 * Handles incoming messages from the MASSim-Server.
 * 
 * @author tristanbehrens
 *
 */
public interface ConnectionListener {

	/**
	 * Handles a message. A message is a list of percepts.
	 * 
	 * @param connection the connection from where the percepts came
	 * @param percepts the percepts sent by the server
	 */
	public void handleMessage(Connection connection, LinkedList<Percept> percepts);
	
}
