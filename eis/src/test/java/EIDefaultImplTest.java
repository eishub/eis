import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eis.exceptions.AgentException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.RelationException;

/**
 * Unit tests for EIDefaultImpl
 */
public class EIDefaultImplTest {
	@Test
	public void testAddEntity() throws EntityException, ManagementException {
		final TestEnvironmentInterface env = new TestEnvironmentInterface();
		env.doAddEntity();

		assertEquals(1, env.getEntities().size());
		assertEquals("entityname", env.getEntities().get(0));
	}

	@Test
	public void testFreePair() throws EntityException, AgentException, RelationException, ManagementException {
		final TestEnvironmentInterface env = new TestEnvironmentInterface();
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