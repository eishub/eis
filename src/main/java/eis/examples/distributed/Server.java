package eis.examples.distributed;

import java.rmi.RemoteException;
import java.util.LinkedList;

import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Percept;
import eis.rmi.EIServerDefaultImpl;

public class Server extends EIServerDefaultImpl implements Runnable {

	boolean running = true;
	
	public Server() {
		super();
		
		try {
			this.addEntity("entity1");
			this.addEntity("entity2");
		} catch (EntityException e) {
		}
		
		new Thread(this).start();
		
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
	
		running = false;
		
	}

	@Override
	public void run() {

		while(running) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public Percept actiondo(String entity) {
		
		return new Percept("done");
		
	}
	
}
