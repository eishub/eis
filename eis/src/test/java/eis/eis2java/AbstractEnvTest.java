package eis.eis2java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;

/**
 * Unit tests for AbstractEnv
 */
public class AbstractEnvTest {
	@SuppressWarnings("serial")
	class MyEnv extends AbstractEnvironment {
		public MyEnv() throws ManagementException {
			setState(EnvironmentState.PAUSED);
		}

		@Override
		protected boolean isSupportedByEnvironment(final Action action) {
			return true;
		}

		@Override
		protected boolean isSupportedByType(final Action action, final String type) {
			return true;
		}

		public void doAddEntity() throws EntityException {
			addEntity("entityname", "entity");
		}
	}

	/**
	 * @throws EntityException     obviously
	 * @throws ManagementException obviously
	 */
	public void testAddEntity() throws EntityException, ManagementException {
		final MyEnv env = new MyEnv();
		env.doAddEntity();

		assertEquals(1, env.getEntities().size());
		assertEquals("entity", env.getEntity("entityname"));
	}

	/**
	 * @throws EntityException     obviously
	 * @throws AgentException      obviously
	 * @throws RelationException   obviously
	 * @throws ManagementException obviously
	 */
	@Test
	public void testFreePair() throws EntityException, AgentException, RelationException, ManagementException {
		final MyEnv env = new MyEnv();
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