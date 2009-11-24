package eis.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashSet;
import java.util.LinkedList;

import eis.AgentListener;
import eis.EnvironmentInterfaceStandard;
import eis.EnvironmentListener;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentCommand;
import eis.iilang.EnvironmentEvent;
import eis.iilang.Percept;

// TODO throw exeptions

/**
 * @author tristanbehrens
 *
 */
public abstract class EIClientDefaultImpl implements EnvironmentInterfaceStandard,EIClientRemote {

	protected EnvironmentInterfaceStandardRemote server = null;
	
	public static boolean debug = true;
	
	protected LinkedList<String> localRegisteredAgents = new LinkedList<String>();
	
	public void debugPrintln(Object obj) {
		
		if( debug )
			System.out.println("[CLIENT]: " + obj);
		
	}

	public void connect(String className) throws RemoteException, NotBoundException {
		
		debugPrintln("Getting registry...");
	    Registry registry = LocateRegistry.getRegistry();

		debugPrintln("Getting interface...");
		EnvironmentInterfaceStandardRemote s = (EnvironmentInterfaceStandardRemote) registry.lookup( className ); 
	    
		debugPrintln( "Connected to server" ); 

		server = s;
		
	}
	
	@Override
	public void associateEntity(String agent, String entity)
			throws RelationException {

		try {

			server.stubAssociateEntity(agent, entity);
		
		} catch (RemoteException e) {
			
			e.printStackTrace();
		
		}
		
	}

	@Override
	public void attachAgentListener(String agent, AgentListener listener) {
		// TODO perform via RMI
		
	}

	@Override
	public void attachEnvironmentListener(EnvironmentListener listener) {
		// TODO perform via RMI
		
	}

	@Override
	public void detachAgentListener(String agent, AgentListener listener) {
		// TODO perform via RMI
		
	}

	@Override
	public void detachEnvironmentListener(EnvironmentListener listener) {
		// TODO perform via RMI
		
	}

	@Override
	public void freeAgent(String agent) throws RelationException {

		try {
			server.stubFreeAgent(agent);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void freeEntity(String entity) throws RelationException {

		try {
			server.stubFreeEntity(entity);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void freePair(String agent, String entity) throws RelationException {

		try {
			server.stubFreePair(agent,entity);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public LinkedList<String> getAgents() {

		try {
			return server.stubGetAgents();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public LinkedList<Percept> getAllPercepts(String agent, String... entities)
			throws PerceiveException, NoEnvironmentException {
		// TODO perform via RMI
		return null;
	}

	@Override
	public HashSet<String> getAssociatedAgents(String entity)
			throws EntityException {

		try {
			return server.stubGetAssociatedAgents(entity);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public HashSet<String> getAssociatedEntities(String agent)
			throws AgentException {

		try {
			return server.stubGetAssociatedEntities(agent);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public LinkedList<String> getEntities() {

		try {
			return server.stubGetEntities();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public LinkedList<String> getFreeEntities() {

		try {
			return server.stubGetFreeEntities();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public String getType(String entity) throws EntityException {

		try {
			return server.stubGetType(entity);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
}

	@Override
	public boolean isConnected() {

		return server!=null;
	
	}

	@Override
	public void manageEnvironment(EnvironmentCommand command, String... args)
			throws ManagementException, NoEnvironmentException {
		// TODO perform via RMI
		
	}

	@Override
	public LinkedList<Percept> performAction(String agent, Action action,
			String... entities) throws ActException, NoEnvironmentException {

		try {
			return server.stubPerformAction(agent, action, entities);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public void registerAgent(String agent) throws AgentException {

		debugPrintln("Registering agent " + agent);
		
		try {
			
			server.stubRegisterAgent(agent);
		
			localRegisteredAgents.add(agent); 
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void release() {

		server = null;
		
	}

	@Override
	public void unregisterAgent(String agent) throws AgentException {

		debugPrintln("Unregistering agent " + agent);
		
		try {
			
			server.stubUnregisterAgent(agent);
		
			localRegisteredAgents.remove(agent); 
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void notifyAgent(String agent, Percept percept)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyDeletedEntity(String entity) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyEnvironmentEvent(EnvironmentEvent event)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyFreeEntity(String entity) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyNewEntity(String entity) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
