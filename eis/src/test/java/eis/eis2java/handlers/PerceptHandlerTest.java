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

		percepts = handler.getAllPercepts();

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
