package eis.eis2java.handlers;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eis.PerceptUpdate;
import eis.eis2java.environment.AbstractEnvironment;
import eis.eis2java.util.AllPerceptsModule;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;

/**
 * The {@link AllPerceptPerceptHandler} assumes the agent can not provide all
 * its percepts one at a time but that instead the agent prepares a batch of
 * percepts ahead of time.
 *
 * This is useful when using agents that run in their own thread. The time to
 * acquire such an agent and request percepts without causing racing conditions
 * would cause a significant slow down.
 *
 * This class requires the agent to implement the {@link AllPerceptsProvider}
 * interface. Implementing this interface is simplified by using the
 * {@link AllPerceptsModule}.
 *
 * This handler should be passed to the {@link AbstractEnvironment} while
 * registering the agent.
 *
 * @see AllPerceptsModule
 * @see AllPerceptsProvider
 */
public final class AllPerceptPerceptHandler extends AbstractPerceptHandler {
	/** Maps a Class to a map of percept method */
	private final AllPerceptsProvider allPerceptProvider;

	public AllPerceptPerceptHandler(final AllPerceptsProvider entity) throws EntityException {
		super(entity);
		this.allPerceptProvider = entity;
	}

	@Override
	public PerceptUpdate getPercepts() throws PerceiveException {
		final Map<Method, Object> batchPerceptObjects = this.allPerceptProvider.getPercepts();
		final PerceptUpdate percepts = new PerceptUpdate();

		for (final Entry<Method, Object> entry : batchPerceptObjects.entrySet()) {
			final Method method = entry.getKey();
			final Object perceptObject = entry.getValue();

			final List<Object> perceptObjects = unpackPerceptObject(method, perceptObject);
			final PerceptUpdate translatedPercepts = translatePercepts(method, perceptObjects);
			percepts.merge(translatedPercepts);
		}

		return percepts;
	}
}
