package eis.examples.entitygenerator;

import java.util.LinkedList;

import eis.EIDefaultImpl;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.EnvironmentCommand;
import eis.iilang.Identifier;
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
		return true;
	}

	@Override
	public void manageEnvironment(EnvironmentCommand command)
			throws ManagementException {

		throw new ManagementException("No environment-commands supported.");
		
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
				
				addEntity(entity,type);
				
			} catch (EntityException e1) {
				
				System.out.println("This should not happen!");
			
			}
		
			System.out.println("EI: Added entity \"" + entity + "\" of type \"" + type + "\"");
			
			// sleep for a second
			try {
				
				Thread.sleep(1000);
			
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			
			}
			
		}
		
	}

	@Override
	public String requiredVersion() {
		return "0.2rc1";
	}
	
	public Percept actionshout(String entity, Identifier sentence) {
		
		System.out.println(entity + " says <i>" + sentence.toProlog() + "</i>");
		
		try {
			this.notifyAgents(new Percept("exclamation", new Identifier(sentence.getValue()), new Identifier(entity) ));
		} catch (EnvironmentInterfaceException e) {
			e.printStackTrace();
		}
		
		return new Percept("done");
		
	}

	public static void main(String[] args) {
		
		new GeneratorInterface();
		
	}

}
