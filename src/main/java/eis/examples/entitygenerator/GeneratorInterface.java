package eis.examples.entitygenerator;

import java.util.LinkedList;

import eis.EIDefaultImpl;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Percept;

public class GeneratorInterface extends EIDefaultImpl implements Runnable {

	private String[] types = { "type1", "type2", "type3" };
	
	private String entityBaseName = "entity";
	
	private int nextNum = 0;
	
	private boolean run = true;
	
	public GeneratorInterface() {
		
		// start the generator-thread
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

		run = false;
		
	}

	@Override
	public void run() {

		while( run ) {
			
			// generate an entity name and randomly pick its type
			String entity = entityBaseName + nextNum;
			String type = types[(int) (Math.random() * types.length)];
			nextNum ++ ;
		
			// add the entity and set its type
			try {
				
				addEntity(entity);
				setType(entity,type);
			
			} catch (EntityException e1) {
				
				System.out.println("This should not happen!");
			
			}
		
			System.out.println("Added entity \"" + entity + "\" of type \"" + entity + "\"");
			
			// sleep for a second
			try {
				
				Thread.sleep(1000);
			
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			
			}
			
		}
		
	}

}
