package eis.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.LinkedList;

import eis.AgentListener;
import eis.EIDefaultImpl;
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
import eis.iilang.Percept;

// TODO implement EIS instead of extending the Default-Impl
public abstract class EIServerDefaultImpl extends EIDefaultImpl implements EnvironmentInterfaceStandardRemote {

	public static boolean debug = true;
	
	public void debugPrintln(Object obj) {
		
		if( debug )
			System.out.println("[SERVER]: " + obj);
		
	}
	
	public EIServerDefaultImpl() {
		super();
		
		// creating registry
		try { 
			debugPrintln("Creating registry...");

			LocateRegistry.createRegistry( Registry.REGISTRY_PORT ); 
		  
			debugPrintln("Exporting...");
			EIServerDefaultImpl server = this; 
			EnvironmentInterfaceStandardRemote stub = (EnvironmentInterfaceStandardRemote) UnicastRemoteObject.exportObject( server, 0 ); 
			//RemoteServer.setLog( System.out ); 
		 
			debugPrintln("Rebinding registry...");
			Registry registry = LocateRegistry.getRegistry(); 
			registry.rebind( "EIServer", stub ); 
		    
			debugPrintln("Server established!");
		    
		} 
		catch ( RemoteException e )  { 

			debugPrintln("Caught an exception");
			debugPrintln(e);
			
		}

	}
	
	public static void main(String args[]) {
		
		//new EIServerDefaultImpl();
		
	}

	@Override
	protected LinkedList<Percept> getAllPerceptsFromEntity(String entity)
			throws PerceiveException, NoEnvironmentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void manageEnvironment(EnvironmentCommand command, String... args)
			throws ManagementException, NoEnvironmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stubAssociateEntity(String agent, String entity)
			throws RelationException, RemoteException {

		this.associateEntity(agent, entity);
		
	}

	@Override
	public void stubAttachAgentListener(String agent, AgentListener listener)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stubAttachEnvironmentListener(EnvironmentListener listener)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stubDetachAgentListener(String agent, AgentListener listener)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stubDetachEnvironmentListener(EnvironmentListener listener)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stubFreeAgent(String agent) throws RelationException,
			RemoteException {

		freeAgent(agent);
	
	}

	@Override
	public void stubFreeEntity(String entity) throws RelationException,
			RemoteException {

		freeEntity(entity);
		
	}

	@Override
	public void stubFreePair(String agent, String entity)
			throws RelationException, RemoteException {

		freePair(agent,entity);
		
	}

	@Override
	public LinkedList<String> stubGetAgents() throws RemoteException,
			RemoteException {

		return getAgents();
		
	}

	@Override
	public LinkedList<Percept> stubGetAllPercepts(String agent,
			String... entities) throws PerceiveException,
			NoEnvironmentException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashSet<String> stubGetAssociatedAgents(String entity)
			throws EntityException, RemoteException {

		return getAssociatedAgents(entity);
	
	}

	@Override
	public HashSet<String> stubGetAssociatedEntities(String agent)
			throws AgentException, RemoteException {
	
		return getAssociatedEntities(agent);
	
	}

	@Override
	public LinkedList<String> stubGetEntities() throws RemoteException,
			RemoteException {
		
		return getEntities();
		
	}

	@Override
	public LinkedList<String> stubGetFreeEntities() {

		return getFreeEntities();
	
	}

	@Override
	public String stubGetType(String entity) throws EntityException,
			RemoteException {

		return getType(entity);
	
	}

	@Override
	public boolean stubIsConnected() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stubManageEnvironment(EnvironmentCommand command,
			String... args) throws ManagementException, NoEnvironmentException,
			RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedList<Percept> stubPerformAction(String agent, Action action,
			String... entities) throws ActException, NoEnvironmentException,
			RemoteException {

		return performAction(agent,action,entities);
	
	}

	@Override
	public void stubRegisterAgent(String agent) throws AgentException,
			RemoteException {

		registerAgent(agent);
		
	}

	@Override
	public void stubRelease() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stubUnregisterAgent(String agent) throws AgentException,
			RemoteException {
		// TODO Auto-generated method stub
		
	}
	
}
