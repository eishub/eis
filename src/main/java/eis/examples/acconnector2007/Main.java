package eis.examples.acconnector2007;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import eis.AgentListener;
import eis.EnvironmentListener;

import eis.EnvironmentInterfaceStandard;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Percept;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.EnvironmentEvent;

public class Main implements AgentListener,EnvironmentListener {

	/**
	 * @param args not used
	 */
	public static void main(String[] args) {
		
		new Main();
		
	}

	
	/**
	 * Instantiates the class.
	 */
	public Main() {

		EnvironmentInterfaceStandard ei = new EnvironmentInterface();

		ei.attachEnvironmentListener(this);

		try {
			ei.manageEnvironment(new EnvironmentCommand(EnvironmentCommand.START));
		} catch (ManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoEnvironmentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// constants
		String server = "localhost";
		int port = 12300;
		
		// registering and associating agents
		try {

			for( int a = 1 ; a <= 6 ; a++ ) {
				ei.registerAgent("agent" + a);
				ei.associateEntity("agent" + a, "connector" + a);
			}

		} catch (AgentException e) {

			e.printStackTrace();
			System.exit(0);
		
		} catch (RelationException e) {

			e.printStackTrace();
			System.exit(0);
		
		}
	
		LinkedList<Percept> ar = null;
		try {

			// connect all agents
			Action action = null;
			for( int a = 1 ; a <= 6 ; a++ ) {
				action = new Action(
						"connect", 
						new Identifier(server), 
						new Numeral(port),
						new Identifier("botagent" +a),
						new Identifier("" + a)
				);
				ar = (LinkedList) ei.performAction("agent" + a, action);
			}

			while( true ) {

				//System.out.println("Action " + action);

				Vector<String> actions = new Vector<String>();
				actions.add("up");
				actions.add("down");
				actions.add("left");
				actions.add("right");
				for( int a = 1 ; a <= 6 ; a++ ) { 
					
					Collections.shuffle(actions);

					action = new Action(
							actions.firstElement()
							);
					
					ar = (LinkedList) ei.performAction("agent" + a, action);
					
				}
			
				

				//System.out.println("Action-result:\n" + ar.toString());

			}
			
		} catch (ActException e) {

			e.printStackTrace();
			System.exit(0);
		
		} catch (NoEnvironmentException e) {

			e.printStackTrace();
			System.exit(0);

		}
	
	}

	@Override
	public void handlePercept(String agent, Percept event) {

		System.out.println("Event for agent " + agent + " " + event.toXML() );
		
	}

	@Override
	public void handleDeletedEntity(String entity) {
		
	}

	@Override
	public void handleEnvironmentEvent(EnvironmentEvent event) {
		
	}

	@Override
	public void handleFreeEntity(String entity) {
		
	}

	@Override
	public void handleNewEntity(String entity) {
		
	}

}
