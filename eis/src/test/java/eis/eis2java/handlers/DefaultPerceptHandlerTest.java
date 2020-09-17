package eis.eis2java.handlers;

import eis.eis2java.entity.ValidPerceptEntity;
import eis.exceptions.EntityException;

/**
 * * Apply the test getAllPercept test to the {@link DefaultPerceptHandler}.
 */
public class DefaultPerceptHandlerTest extends PerceptHandlerTest {
	@Override
	public PerceptHandler getHandler(final Object entity) throws EntityException {
		return new DefaultPerceptHandler(entity);
	}

	@Override
	public ValidPerceptEntity getValidEntity() throws EntityException {
		return new ValidPerceptEntity();
	}
}
