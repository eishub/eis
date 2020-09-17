package eis.eis2java.handlers;

import eis.eis2java.entity.ValidPerceptEntity;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;

/**
 * Apply the test getAllPercept test to the {@link AllPerceptPerceptHandler}.
 */
public class AllPerceptPerceptHandlerTest extends PerceptHandlerTest {
	@Override
	public PerceptHandler getHandler(final Object entity) throws EntityException {
		return new AllPerceptPerceptHandler((AllPerceptsProvider) entity);
	}

	@Override
	public ValidPerceptEntity getValidEntity() throws EntityException {
		return new ValidPerceptEntity();
	}
}
