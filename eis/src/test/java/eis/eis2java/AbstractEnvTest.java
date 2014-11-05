package eis.eis2java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.RelationException;
import eis.iilang.Action;

/**
 * Unit tests for AbstractEnv
 * 
 * @author W.Pasman 5nov14
 *
 */
public class AbstractEnvTest {

	@SuppressWarnings("serial")
	class MyEnv extends AbstractEnvironment {
		@Override
		protected boolean isSupportedByEnvironment(Action action) {
			return true;
		}

		@Override
		protected boolean isSupportedByType(Action action, String type) {
			return true;
		}

		public void doAddEntity() throws EntityException {
			addEntity("entityname", "entity");
		}

	}

	@Test
	public void testAddEntity() throws EntityException {
		MyEnv env = new MyEnv();
		env.doAddEntity();

		assertEquals(1, env.getEntities().size());
		assertEquals("entity", env.getEntity("entityname"));
	}

	@Test
	public void testFreePair() throws EntityException, AgentException,
			RelationException {
		MyEnv env = new MyEnv();
		env.doAddEntity();

		assertEquals(1, env.getEntities().size());

		env.registerAgent("agentname");
		assertEquals(1, env.getAgents().size());

		env.associateEntity("agentname", "entityname");
		assertEquals(1, env.getAssociatedEntities("agentname").size());

		env.freePair("agentname", "entityname");

		// check that entity was not deleted
		assertEquals(1, env.getEntities().size());

		// and that the association was deleted
		assertEquals(0, env.getAssociatedEntities("agentname").size());
	}
}