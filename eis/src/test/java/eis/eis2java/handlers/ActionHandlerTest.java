package eis.eis2java.handlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eis.eis2java.entity.ValidActionEntity;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.iilang.Action;
import eis.iilang.Numeral;

/**
 * test the action handler
 */
public abstract class ActionHandlerTest {
	protected ActionHandler handler;

	/**
	 * @param entity the entity that produces percepts
	 * @return the action handle for this test
	 * @throws EntityException if something fails
	 */
	public abstract ActionHandler getHandler(Object entity) throws EntityException;

	/**
	 * @return a valid entity
	 */
	public abstract ValidActionEntity getValdidEntity();

	protected ValidActionEntity entity;

	/**
	 * @throws Exception if setup fails
	 */
	@Before
	public void setUp() throws Exception {
		this.entity = getValdidEntity();
		this.handler = getHandler(this.entity);
	}

	/**
	 *
	 */
	@Test
	public void testIsSupportedByEntityPostive() {
		assertTrue(this.handler.isSupportedByEntity(new Action("getX")));
		assertFalse(this.handler.isSupportedByEntity(new Action("getY")));

		assertTrue(this.handler.isSupportedByEntity(new Action("setX", new Numeral(1))));
		assertFalse(this.handler.isSupportedByEntity(new Action("setX")));
	}

	/**
	 * @throws ActException obviously
	 */
	@Test
	public void testPerformAction() throws ActException {
		this.handler.performAction(new Action("getX"));
		this.handler.performAction(new Action("setX", new Numeral(1)));
		this.handler.performAction(new Action("getX"));
	}
}