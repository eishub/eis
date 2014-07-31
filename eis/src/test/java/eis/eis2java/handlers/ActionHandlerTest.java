package eis.eis2java.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eis.eis2java.entity.ValidActionEntity;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Percept;

public abstract class ActionHandlerTest {

	protected ActionHandler handler;

	public abstract ActionHandler getHandler(Object entity)
			throws EntityException;

	public abstract ValidActionEntity getValdidEntity();

	protected ValidActionEntity entity;

	public ActionHandlerTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		entity = getValdidEntity();
		handler = getHandler(entity);
	}

	@Test
	public void testIsSupportedByEntityPostive() {
		assertTrue(handler.isSupportedByEntity(new Action("getX")));
		assertFalse(handler.isSupportedByEntity(new Action("getY")));

		assertTrue(handler.isSupportedByEntity(new Action("setX",
				new Numeral(1))));
		assertFalse(handler.isSupportedByEntity(new Action("setX")));

	}

	@Test
	public void testPerformAction() throws ActException {
		Percept percept = handler.performAction(new Action("getX"));
		assertEquals(new Percept("getX", new Numeral(0)), percept);

		percept = handler.performAction(new Action("setX", new Numeral(1)));
		assertEquals(null, percept);

		percept = handler.performAction(new Action("getX"));
		assertEquals(new Percept("getX", new Numeral(1)), percept);
	}

}