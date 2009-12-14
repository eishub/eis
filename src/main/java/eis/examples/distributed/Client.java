package eis.examples.distributed;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.rmi.EIClientDefaultImpl;
import eis.rmi.EIServerRemote;

public class Client extends EIClientDefaultImpl implements Runnable {

	public boolean running = true;
	
	public Client() {
		super("EIServer");
		
		new Thread(this).start();

		debugPrintln("Client started");

	}	
	
	@Override
	public void run() {

		try {
			
			this.registerAgent("agent1");
			this.registerAgent("agent2");
			debugPrintln("Agents: " + this.getAgents());
			debugPrintln("Entities: " + this.getEntities());
			debugPrintln("Free entities: " + this.getFreeEntities());
			
			debugPrintln("");
			this.associateEntity("agent1", "entity1");
			this.associateEntity("agent2", "entity2");
			debugPrintln("Free entities: " + this.getFreeEntities());
			debugPrintln("Associated entities (agent1): " + this.getAssociatedEntities("agent1"));
			debugPrintln("Associated entities (agent2): " + this.getAssociatedEntities("agent2"));
			debugPrintln("Associated agents (entity1): " + this.getAssociatedAgents("entity1"));
			debugPrintln("Associated agents (entity2): " + this.getAssociatedAgents("entity2"));

			debugPrintln("");
			this.freePair("agent1", "entity1");
			this.associateEntity("agent2", "entity1");
			debugPrintln("Free entities: " + this.getFreeEntities());
			debugPrintln("Associated entities (agent1): " + this.getAssociatedEntities("agent1"));
			debugPrintln("Associated entities (agent2): " + this.getAssociatedEntities("agent2"));

			debugPrintln("");
			this.freeAgent("agent2");
			debugPrintln("Free entities: " + this.getFreeEntities());
			debugPrintln("Associated entities (agent1): " + this.getAssociatedEntities("agent1"));
			debugPrintln("Associated entities (agent2): " + this.getAssociatedEntities("agent2"));

			debugPrintln("");
			this.associateEntity("agent1", "entity1");
			this.associateEntity("agent1", "entity2");
			debugPrintln("Free entities: " + this.getFreeEntities());
			debugPrintln("Associated entities (agent1): " + this.getAssociatedEntities("agent1"));
			debugPrintln("Associated entities (agent2): " + this.getAssociatedEntities("agent2"));

			debugPrintln("");
			this.freeEntity("entity1");
			debugPrintln("Free entities: " + this.getFreeEntities());
			debugPrintln("Associated entities (agent1): " + this.getAssociatedEntities("agent1"));
			debugPrintln("Associated entities (agent2): " + this.getAssociatedEntities("agent2"));

			debugPrintln("");
			debugPrintln("Type (entity1): " + this.getType("entity1"));
			debugPrintln("Type (entity2): " + this.getType("entity2"));
			
			debugPrintln("");
			debugPrintln("Action result: " + this.performAction("agent1", new Action("shout", new Identifier("can you hear me?"))));

			debugPrintln("");
			debugPrintln("Action result: " + this.performAction("agent1", new Action("callMeBack")));

			debugPrintln("");
			this.manageEnvironment(
					new EnvironmentCommand(
							EnvironmentCommand.INIT, 
							(Parameter)new Function("gridWidth", new Numeral(10)),
							(Parameter)new Function("gridHeight", new Numeral(10)),
							(Parameter)new Function("entities", new Numeral(4))
							)
					);

		} catch (AgentException e) {

			e.printStackTrace();
		} catch (RelationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoEnvironmentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while( running = true ) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			
		}
		
	}

	@Override
	public EIServerRemote instantiateServer() {

		return new Server();
	
	}

	public static void main(String[] args) {
		
		new Client();
		
	}

	@Override
	public String requiredVersion() {
		return "0.2";
	}
	
}
