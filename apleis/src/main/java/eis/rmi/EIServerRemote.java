package eis.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.LinkedList;

import eis.AgentListener;
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

public interface EIServerRemote extends Remote {


	void registerAgent(String agent) throws AgentException,RemoteException;

	void unregisterAgent(String agent) throws AgentException,RemoteException;

	LinkedList<String> getAgents() throws RemoteException,RemoteException;

	LinkedList<String> getEntities() throws RemoteException,RemoteException;

	void associateEntity(String agent, String entity) throws RelationException,RemoteException;

	void freeEntity(String entity) throws RelationException,RemoteException;

	void freeAgent(String agent) throws RelationException,RemoteException;

	void freePair(String agent, String entity) throws RelationException,RemoteException;

	HashSet<String> getAssociatedEntities(String agent) throws AgentException,RemoteException;

	HashSet<String> getAssociatedAgents(String entity) throws EntityException,RemoteException;

	LinkedList<String> getFreeEntities() throws RemoteException;

	LinkedList<Percept> performAction(String agent, Action action,
			String... entities) throws ActException, NoEnvironmentException,RemoteException;

	LinkedList<Percept> getAllPercepts(String agent, String... entities)
			throws PerceiveException, NoEnvironmentException,RemoteException;

	void manageEnvironment(EnvironmentCommand command)
			throws ManagementException, NoEnvironmentException,RemoteException;

	void release() throws RemoteException;

	boolean isConnected() throws RemoteException;

	String getType(String entity) throws EntityException,RemoteException;
	
	void attachClientListener(EIClientRemote client) throws RemoteException;
	
	void detachClientListener(EIClientRemote client) throws RemoteException;

}
