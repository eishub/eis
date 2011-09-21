package eis.carriage;

import java.util.LinkedList;

import eis.*;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Percept;
import eis.iilang.Numeral;

public class EnvironmentInterface extends EIDefaultImpl implements Runnable {
	
	private Environment env;
	
	public EnvironmentInterface() {
		
		env = new Environment();
		
		try {

			this.addEntity("robot1","robot");
			this.addEntity("robot2","robot");

		} catch (EntityException e) {
			e.printStackTrace();
		}
		
		Thread t = new Thread( this ); 
		//t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	
		try {
			this.setState(EnvironmentState.PAUSED);
		} catch (ManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run() {

		while(true) {

			// notify about free entities
			//for( String free : this.getFreeEntities() )
				//this.notifyFreeEntity(free);
						
			// tell current step
			long step = env.getStepNumber();
			Percept p = new Percept("step", new Numeral(step) );
			for( String entity : this.getEntities() )
				try {
					this.notifyAgentsViaEntity(p, entity);
				} catch (EnvironmentInterfaceException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			
			// block for 1 second
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e);

			}
			
		}
		
	}

	@Override
	public LinkedList<Percept> getAllPerceptsFromEntity(String entity) {

		LinkedList<Percept> ret = new LinkedList<Percept>();
		
		if( entity.equals("robot1"))
			ret.add(
					new Percept(
							"carriagePos", 
							new Numeral(env.getRobotPercepts1())
							)
					);
		else if( entity.equals("robot2"))
			ret.add(
					new Percept(
							"carriagePos", 
							new Numeral(env.getRobotPercepts2())
							)
					);

		return ret;
	}
	
	public Percept actionpush(String entity) {
		
		// push
		if( entity.equals("robot1") )
			env.robotPush1();
		if( entity.equals("robot2") )
			env.robotPush2();

		return new Percept("success");

	}

	public Percept actionwait(String entity) {
		
		// push
		if( entity.equals("robot1") )
			env.robotWait1();
		if( entity.equals("robot2") )
			env.robotWait2();

		return new Percept("success");

	}

	@Override
	public String requiredVersion() {
		return "0.3";
	}

	/*@Override
	public boolean areParametersCorrect(Action action) {

		if ( action.getName().equals("wait") && action.getParameters().size() == 0 )
			return true;
		if ( action.getName().equals("push") && action.getParameters().size() == 0 )
			return true;
		
		return false;
	}*/

	@Override
	public boolean isSupportedByEntity(Action action, String entity) {
		
		if ( action.getName().equals("push") && getEntities().contains(entity) )
			return true;
		
		if ( action.getName().equals("wait") && getEntities().contains(entity) )
			return true;

		return false;
	}

	@Override
	public boolean isSupportedByEnvironment(Action action) {

		if ( action.getName().equals("push") )
			return true;
		
		if ( action.getName().equals("wait") )
			return true;

		return false;
	}

	@Override
	public boolean isSupportedByType(Action action, String type) {

		if ( type.equals("robot") && action.getName().equals("push") )
			return true;
		if ( type.equals("robot") && action.getName().equals("wait") )
			return true;		
		
		return false;

	}

	@Override
	public Percept performEntityAction(String entity, Action action)
			throws ActException {

		if ( action.getName().equals("wait") ) {
			return actionwait(entity);
		}
		else if ( action.getName().equals("push") ) {
			return actionpush(entity);
		}
		else {
			throw new AssertionError("action " + action.getName() + "not recognized");			
		}

	}

	@Override
	public String queryEntityProperty(String entity, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryProperty(String property) {
		// TODO Auto-generated method stub
		return null;
	}

}
