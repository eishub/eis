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

public interface EnvironmentInterfaceStandardRemote extends Remote {

	void stubAttachEnvironmentListener(EnvironmentListener listener) throws RemoteException;

	void stubDetachEnvironmentListener(EnvironmentListener listener) throws RemoteException;

	void stubAttachAgentListener(String agent, AgentListener listener) throws RemoteException;

	void stubDetachAgentListener(String agent, AgentListener listener) throws RemoteException;

	void stubRegisterAgent(String agent) throws AgentException,RemoteException;

	void stubUnregisterAgent(String agent) throws AgentException,RemoteException;

	LinkedList<String> stubGetAgents() throws RemoteException,RemoteException;

	LinkedList<String> stubGetEntities() throws RemoteException,RemoteException;

	void stubAssociateEntity(String agent, String entity) throws RelationException,RemoteException;

	void stubFreeEntity(String entity) throws RelationException,RemoteException;

	void stubFreeAgent(String agent) throws RelationException,RemoteException;

	void stubFreePair(String agent, String entity) throws RelationException,RemoteException;

	HashSet<String> stubGetAssociatedEntities(String agent) throws AgentException,RemoteException;

	HashSet<String> stubGetAssociatedAgents(String entity) throws EntityException,RemoteException;

	LinkedList<String> stubGetFreeEntities() throws RemoteException;

	LinkedList<Percept> stubPerformAction(String agent, Action action,
			String... entities) throws ActException, NoEnvironmentException,RemoteException;

	LinkedList<Percept> stubGetAllPercepts(String agent, String... entities)
			throws PerceiveException, NoEnvironmentException,RemoteException;

	void stubManageEnvironment(EnvironmentCommand command, String... args)
			throws ManagementException, NoEnvironmentException,RemoteException;

	void stubRelease() throws RemoteException;

	boolean stubIsConnected() throws RemoteException;

	String stubGetType(String entity) throws EntityException,RemoteException;
	
}
