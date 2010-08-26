package eis.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import eis.AgentListener;
import eis.EnvironmentInterfaceStandard;
import eis.EnvironmentListener;
import eis.examples.distributed.Server;
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

	protected EIServerRemote server = null;
	
	public static boolean debug = true;
	
	protected LinkedList<String> localRegisteredAgents = new LinkedList<String>();

	/**
	 * This collection stores the listeners that are used to notify about certain events.
	 * <p/> 
	 * The collection can be changed by invoking the respective methods for attaching and
	 * detaching listeners.
	 */
	private Vector<EnvironmentListener> environmentListeners = null;
	
	/**
	 * Stores for each agent (represented by a string) a set of listeners.
	 */
	private ConcurrentHashMap<String,HashSet<AgentListener>> agentsToAgentListeners = null;

	public EIClientDefaultImpl(String serverName) {

		// set up data structures
		environmentListeners = new Vector<EnvironmentListener>();
		agentsToAgentListeners = new ConcurrentHashMap<String,HashSet<AgentListener>>();

		// connect to server, if this fails instantiate one
		try {
			connect("EIServer");
		} catch (RemoteException e) {
		
			debugPrintln("Could not establish a connection. Creating server.");
		
			server = instantiateServer();
			
			try {
				
				connect("EIServer");
			
			} catch (RemoteException e1) {
			
				debugPrintln(e1);
				debugPrintln("Failed again.");
			
			} catch (NotBoundException e1) {

				debugPrintln("Could not! 2");

			}
			
			
		} catch (NotBoundException e) {
			
			debugPrintln("Could not! 3");
		
		}
		
	}
	
	public void debugPrintln(Object obj) {
		
		if( debug )
			System.out.println("[CLIENT]: " + obj);
		
	}

	public void connect(String serverName) throws RemoteException, NotBoundException {
		
		debugPrintln("Getting registry...");
	    Registry registry = LocateRegistry.getRegistry();

		debugPrintln("Getting interface...");
		EIServerRemote s = (EIServerRemote) registry.lookup( serverName ); 
	    
		debugPrintln( "Connected to server" ); 

		server = s;

		// add as listener
		s.attachClientListener((EIClientRemote)this);
		//UnicastRemoteObject.exportObject(this);

		debugPrintln( "Added as listener" ); 
		
	}
	
	@Override
	public void associateEntity(String agent, String entity)
			throws RelationException {

		try {

			server.associateEntity(agent, entity);
		
		} catch (RemoteException e) {
			
			e.printStackTrace();
		
		}
		
	}

	@Override
	public void attachAgentListener(String agent, AgentListener listener) {
		// TODO 
		
	}

	@Override
	public void attachEnvironmentListener(EnvironmentListener listener) {
		// TODO 
		
	}

	@Override
	public void detachAgentListener(String agent, AgentListener listener) {
		// TODO 
		
	}

	@Override
	public void detachEnvironmentListener(EnvironmentListener listener) {
		// TODO 
		
	}

	@Override
	public void freeAgent(String agent) throws RelationException {

		try {
			server.freeAgent(agent);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void freeEntity(String entity) throws RelationException {

		try {
			server.freeEntity(entity);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void freePair(String agent, String entity) throws RelationException {

		try {
			server.freePair(agent,entity);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	@Override
	public LinkedList<String> getAgents() {

		try {
			return server.getAgents();
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
			return server.getAssociatedAgents(entity);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public HashSet<String> getAssociatedEntities(String agent)
			throws AgentException {

		try {
			return server.getAssociatedEntities(agent);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public LinkedList<String> getEntities() {

		try {
			return server.getEntities();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public LinkedList<String> getFreeEntities() {

		try {
			return server.getFreeEntities();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public String getType(String entity) throws EntityException {

		try {
			return server.getType(entity);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
}

	@Override
	public boolean isConnected() {

		if( server == null) return false;
		
		try {
			return server.isConnected();
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	
	}

	@Override
	public void manageEnvironment(EnvironmentCommand command)
			throws ManagementException, NoEnvironmentException {
	
		try {
			server.manageEnvironment(command);
		} catch (RemoteException e) {
			System.out.println(e);
			throw new NoEnvironmentException("Failed");
		}

	}

	@Override
	public LinkedList<Percept> performAction(String agent, Action action,
			String... entities) throws ActException, NoEnvironmentException {

		try {
			return server.performAction(agent, action, entities);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	
	}

	@Override
	public void registerAgent(String agent) throws AgentException {

		debugPrintln("Registering agent " + agent);
		
		try {
			
			server.registerAgent(agent);
		
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
			
			server.registerAgent(agent);
		
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

		debugPrintln("Deleted entity " + entity);

	}

	@Override
	public void notifyEnvironmentEvent(EnvironmentEvent event)
			throws RemoteException {


	}

	@Override
	public void notifyFreeEntity(String entity) throws RemoteException {

		debugPrintln("Free entity " + entity);

	}

	@Override
	public void notifyNewEntity(String entity) throws RemoteException {

		debugPrintln("New entity " + entity);

	}

	public abstract EIServerRemote instantiateServer();
	
}
