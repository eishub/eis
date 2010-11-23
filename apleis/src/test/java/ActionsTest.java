import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import eis.EIDefaultImpl;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.EnvironmentInterfaceException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;

class EnvironmentInterface extends EIDefaultImpl {

	public EnvironmentInterface() {
		
		try {
			this.addEntity("entity1");
			this.addEntity("entity2");
			this.setType("entity1", "type1");
			this.setType("entity2", "type2");
		} catch (EntityException e) {
		}
		
	}
	
	@Override
	protected LinkedList<Percept> getAllPerceptsFromEntity(String entity)
			throws PerceiveException, NoEnvironmentException {
		return null;
	}

	@Override
	public String requiredVersion() {
		return null;
	}
	
	public Percept actionmove(String entity, Identifier dir) throws ActException, NoEnvironmentException {
		
		try {
			if( getType(entity).equals("type1") == false)
				throw new ActException(ActException.NOTSUPPORTEDBYTYPE);
			
		} catch (EntityException e) {
		}

		return new Percept("done");

	}

	@Override
	public boolean areParametersCorrect(Action action) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupportedByEntity(Action action, String entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupportedByEnvironment(Action action) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupportedByType(Action action, String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Percept performAction(String entity, Action action)
			throws ActException {
		// TODO Auto-generated method stub
		return null;
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

public class ActionsTest {

	@Test
	public void test() {
		
		
	}
	
	//@Test
	public void test1() {
		
		EnvironmentInterface ei = new EnvironmentInterface();
		
		try {
			ei.registerAgent("agent1");
			ei.registerAgent("agent2");
			ei.associateEntity("agent1", "entity1");
			ei.associateEntity("agent1", "entity2");
		} catch (EnvironmentInterfaceException e) {
			System.out.println("Failed to set up.");
			fail();
		}
		
		// test: agent1 is not registered
		try {
			ei.performAction("agentNotRegistered", new Action("move",new Identifier("up")));
			fail();
		} catch (ActException e) {
			if( e.getType() != ActException.NOTREGISTERED )
				fail();
		} /*catch (NoEnvironmentException e) {
			fail();
		}*/

		// test: agent2 has no entities
		try {
			ei.performAction("agent2", new Action("move",new Identifier("up")));
			fail();
		} catch (ActException e) {
			if( e.getType() != ActException.NOENTITIES )
				fail();
		} /*catch (NoEnvironmentException e) {
			fail();
		}*/

		// test: wrong entity for agent 1
		try {
			ei.performAction("agent1", new Action("move",new Identifier("up")),"entity3");
			fail();
		} catch (ActException e) {
			if( e.getType() != ActException.WRONGENTITY )
				fail();
		} /*catch (NoEnvironmentException e) {
			fail();
		}*/

		// test: wrong action-name for agent 1
		/*try {
			ei.performAction("agent1", new Action("skip"));
			fail();
		} catch (ActException e) {
			if( e.getType() != ActException.WRONGSYNTAX )
				fail();
		} catch (NoEnvironmentException e) {
			fail();
		}

		// test: wrong parameter for agent1
		try {
			ei.performAction("agent1", new Action("move",new Numeral(3)));
			fail();
		} catch (ActException e) {
			if( e.getType() != ActException.WRONGSYNTAX )
				fail();
		} /*catch (NoEnvironmentException e) {
			fail();
		}*/

		// test: action not available for entity2
		try {
			ei.performAction("agent1", new Action("move",new Identifier("up")),"entity2");
			fail();
		} catch (ActException e) {
			if( e.getType() != ActException.NOTSUPPORTEDBYTYPE )
				fail();
		} /* catch (NoEnvironmentException e) {
			fail();
		}*/

		// test: should work
		try {
			ei.performAction("agent1", new Action("move",new Identifier("up")),"entity1");
		} catch (ActException e) {
			System.out.println(e.getType());
			fail();
		} /*catch (NoEnvironmentException e) {
			fail();
		}*/

	}
	
}
