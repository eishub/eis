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
 */
public abstract class PerceptHandlerTest {
	protected PerceptHandler handler;
	protected ValidPerceptEntity entity;

	public abstract PerceptHandler getHandler(Object entity) throws EntityException;

	public abstract ValidPerceptEntity getValidEntity() throws EntityException;

	@Before
	public void setUp() throws Exception {
		this.entity = getValidEntity();
		this.handler = getHandler(this.entity);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllPercepts() throws PerceiveException {
		PerceptUpdate percepts = this.handler.getPercepts();
		assertAllPerceptsReceived(percepts);

		percepts = this.handler.getPercepts();
		assertPartialPerceptsReceived(percepts);

		this.handler.reset();
		percepts = this.handler.getPercepts();
		assertAllPerceptsReceived(percepts);
	}

	/**
	 * Check that all percepts have been received
	 *
	 * @param percepts
	 */
	private void assertAllPerceptsReceived(final PerceptUpdate percepts) {
		final List<Percept> addList = percepts.getAddList(); // FIXME: deleteList??
		assertTrue(addList.contains(this.entity.getAlways()));
		assertTrue(addList.contains(this.entity.getOnce()));
		assertTrue(addList.contains(this.entity.getOnChange()));
		assertFalse(addList.contains(this.entity.getOnChanged()));
		assertTrue(addList.containsAll(this.entity.getMultipleAlways()));
		assertTrue(addList.containsAll(this.entity.getMultipleOnChange()));

		assertTrue(addList.contains(this.entity.getMultiArgs()));
		assertTrue(addList.containsAll(this.entity.getMultipleMultiArgs()));
	}

	/**
	 * Check that only part of percepts were received, since this is not the first
	 * call to getAllPercepts.
	 *
	 * @param percepts
	 */
	private void assertPartialPerceptsReceived(final PerceptUpdate percepts) {
		final List<Percept> addList = percepts.getAddList(); // FIXME: deleteList??
		assertFalse(addList.contains(this.entity.getAlways()));
		assertFalse(addList.contains(this.entity.getOnce()));
		assertFalse(addList.contains(this.entity.getOnChange()));
		assertTrue(addList.contains(this.entity.getOnChanged()));
		assertFalse(addList.containsAll(this.entity.getMultipleAlways()));
		assertFalse(addList.containsAll(this.entity.getMultipleOnChange()));
		assertFalse(addList.contains(this.entity.getMultiArgs()));
		assertFalse(addList.containsAll(this.entity.getMultipleMultiArgs()));
	}
}
