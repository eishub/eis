package eis.eis2java.handlers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

	public abstract PerceptHandler getHandler(Object entity)
			throws EntityException;

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
		LinkedList<Percept> percepts = handler.getAllPercepts();
		assertAllPerceptsReceived(percepts);

		percepts = handler.getAllPercepts();
		assertPartialPerceptsReceived(percepts);

		// Check, third time we should still get same percepts?
		// percepts = handler.getAllPercepts();
		// assertPartialPerceptsReceived(percepts);

		handler.reset();
		percepts = handler.getAllPercepts();
		assertAllPerceptsReceived(percepts);

	}

	/**
	 * Check that all percepts have been received
	 * 
	 * @param percepts
	 */
	private void assertAllPerceptsReceived(LinkedList<Percept> percepts) {
		assertTrue(percepts.contains(entity.getAlways()));
		assertTrue(percepts.contains(entity.getOnce()));
		assertTrue(percepts.contains(entity.getOnChange()));
		assertFalse(percepts.contains(entity.getOnChanged()));
		assertTrue(percepts.containsAll(entity.getOnChangeNegation()));
		assertFalse(percepts.containsAll(entity.getOnChangedNegation()));
		assertTrue(percepts.containsAll(entity.getMultipleAlways()));
		assertTrue(percepts.containsAll(entity.getMultipleOnChange()));

		assertTrue(percepts.contains(entity.getMultiArgs()));
		assertTrue(percepts.containsAll(entity.getMultipleMultiArgs()));

	}

	/**
	 * Check that only part of percepts were received, since this is not the
	 * first call to getAllPercepts.
	 * 
	 * @param percepts
	 */
	private void assertPartialPerceptsReceived(LinkedList<Percept> percepts) {
		assertTrue(percepts.contains(entity.getAlways()));
		assertFalse(percepts.contains(entity.getOnce()));
		assertFalse(percepts.contains(entity.getOnChange()));
		assertTrue(percepts.contains(entity.getOnChanged()));
		assertFalse(percepts.containsAll(entity.getOnChangeNegation()));
		assertTrue(percepts.containsAll(entity.getOnChangedNegation()));
		assertTrue(percepts.containsAll(entity.getMultipleAlways()));
		assertTrue(percepts.containsAll(entity.getMultipleOnChange()));
		assertTrue(percepts.contains(entity.getMultiArgs()));
		assertTrue(percepts.containsAll(entity.getMultipleMultiArgs()));

	}
}
