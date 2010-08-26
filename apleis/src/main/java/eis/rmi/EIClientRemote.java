package eis.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import eis.iilang.EnvironmentEvent;
import eis.iilang.Percept;

public interface EIClientRemote extends Remote,Serializable {

	void notifyAgent(String agent, Percept percept) throws RemoteException;
	
	void notifyFreeEntity(String entity) throws RemoteException;
	
	void notifyNewEntity(String entity) throws RemoteException;
	
	void notifyDeletedEntity(String entity) throws RemoteException;

	void notifyEnvironmentEvent(EnvironmentEvent event) throws RemoteException;
	
}
