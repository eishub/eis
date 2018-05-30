package eis.eis2java.handlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eis.PerceptUpdate;
import eis.eis2java.entity.ValidPerceptEntity;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

/**
 * Generic test procedure for percept handlers (
 * {@link AllPerceptPerceptHandler} and {@link DefaultPerceptHandler}).
 * 
 * @author Lennard de Rijk
 * 
 */
public abstract class PerceptHandlerTest {

	protected PerceptHandler handler;
	protected ValidPerceptEntity entity;

	public abstract PerceptHandler getHandler(Object entity) throws EntityException;

	public abstract ValidPerceptEntity getValidEntity() throws EntityException;

	@Before
	public void setUp() throws Exception {
		entity = getValidEntity();
		handler = getHandler(entity);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllPercepts() throws PerceiveException {
		PerceptUpdate percepts = handler.getPercepts();
		assertAllPerceptsReceived(percepts);

		percepts = handler.getPercepts();
		assertPartialPerceptsReceived(percepts);

		// Check, third time we should still get same percepts?
		// percepts = handler.getAllPercepts();
		// assertPartialPerceptsReceived(percepts);

		handler.reset();
		percepts = handler.getPercepts();
		assertAllPerceptsReceived(percepts);
	}

	/**
	 * Check that all percepts have been received
	 * 
	 * @param percepts
	 */
	private void assertAllPerceptsReceived(PerceptUpdate percepts) {
		List<Percept> addList = percepts.getAddList(); // FIXME: deleteList??
		assertTrue(addList.contains(entity.getAlways()));
		assertTrue(addList.contains(entity.getOnce()));
		assertTrue(addList.contains(entity.getOnChange()));
		assertFalse(addList.contains(entity.getOnChanged()));
		assertTrue(addList.containsAll(entity.getMultipleAlways()));
		assertTrue(addList.containsAll(entity.getMultipleOnChange()));

		assertTrue(addList.contains(entity.getMultiArgs()));
		assertTrue(addList.containsAll(entity.getMultipleMultiArgs()));
	}

	/**
	 * Check that only part of percepts were received, since this is not the
	 * first call to getAllPercepts.
	 * 
	 * @param percepts
	 */
	private void assertPartialPerceptsReceived(PerceptUpdate percepts) {
		List<Percept> addList = percepts.getAddList(); // FIXME: deleteList??
		assertTrue(addList.contains(entity.getAlways()));
		assertFalse(addList.contains(entity.getOnce()));
		assertFalse(addList.contains(entity.getOnChange()));
		assertTrue(addList.contains(entity.getOnChanged()));
		assertTrue(addList.containsAll(entity.getMultipleAlways()));
		assertTrue(addList.containsAll(entity.getMultipleOnChange()));
		assertTrue(addList.contains(entity.getMultiArgs()));
		assertTrue(addList.containsAll(entity.getMultipleMultiArgs()));
	}
}
