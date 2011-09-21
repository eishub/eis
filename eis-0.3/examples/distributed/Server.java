package eis.examples.distributed;

import java.rmi.RemoteException;
import java.util.LinkedList;

import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eis.rmi.EIServerDefaultImpl;

public class Server extends EIServerDefaultImpl implements Runnable {

	private Environment env = new Environment();
	
	private InterfaceWindow window = new InterfaceWindow();
	
	private boolean running = true;
	
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

	public void release() {
	
		running = false;
		
		window.setVisible(false);
		
	}

	@Override
	public void run() {

		while(running) {
			
			try {
				
				window.update(this,env);
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public Percept actionshout(String entity, Identifier sentence) {
		
		window.logPrintln(entity + " says <i>" + sentence.toProlog() + "</i>");
		
		// todo notify others
		
		return new Percept("done");
		
	}

	public Percept actioncallMeBack(String entity) {

		System.out.println("TA");
		
		this.notifyDeletedEntity("entityDeleted");
		this.notifyNewEntity("entityNew");
		this.notifyFreeEntity("entityFree");
		
		return new Percept("done");
		
	}

	@Override
	public boolean isConnected() throws RemoteException {

		return true;
	
	}

	@Override
	public void manageEnvironment(EnvironmentCommand command)
			throws ManagementException, NoEnvironmentException, RemoteException {

		assert false : "Implement init!";
		
		window.logPrintln(command.toProlog());
		
	}

}
