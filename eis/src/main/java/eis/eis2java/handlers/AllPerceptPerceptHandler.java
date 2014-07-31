package eis.eis2java.handlers;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eis.eis2java.environment.AbstractEnvironment;
import eis.eis2java.util.AllPerceptsModule;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

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
 * 
 * @author mpkorstanje
 * 
 */
public final class AllPerceptPerceptHandler extends AbstractPerceptHandler {

	/** Maps a Class to a map of percept method */
	private final AllPerceptsProvider allPercepProvider;

	public AllPerceptPerceptHandler(AllPerceptsProvider entity)
			throws EntityException {
		super(entity);
		this.allPercepProvider = entity;
	}

	@Override
	public final LinkedList<Percept> getAllPercepts() throws PerceiveException {

		LinkedList<Percept> percepts = new LinkedList<Percept>();
		Map<Method, Object> batchPerceptObjects;

		batchPerceptObjects = allPercepProvider.getAllPercepts();

		for (Entry<Method, Object> entry : batchPerceptObjects.entrySet()) {

			Method method = (Method) entry.getKey();
			Object perceptObject = entry.getValue();

			List<Object> perceptObjects = unpackPerceptObject(method,
					perceptObject);
			List<Percept> translatedPercepts = translatePercepts(method,
					perceptObjects);
			percepts.addAll(translatedPercepts);
		}

		return percepts;
	}

}
