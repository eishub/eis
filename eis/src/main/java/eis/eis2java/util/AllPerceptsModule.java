package eis.eis2java.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eis.eis2java.annotation.AsPercept;
import eis.eis2java.handlers.DefaultPerceptHandler;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;

/**
 * A module that prepares a batch of percepts. Percepts are gathered based on
 * {@link AsPercept} annotations. A batch can be collected using
 * {@link AllPerceptsModule#getAllPercepts()}.
 * 
 * This class simplifies the implementation of the {@link AllPerceptsProvider}
 * by providing the functionality normally provided by the
 * {@link DefaultPerceptHandler}.
 * 
 * Whenever the agent is ready to provide a new batch of percepts the agent
 * should call {@link #updatePercepts}.
 * 
 * @author mpkorstanje
 * 
 * 
 */
public class AllPerceptsModule {

	/**
	 * Methods that provide the percepts.
	 */
	private final Set<Method> perceptProviders;

	/**
	 * Percepts pulled from bot.
	 */
	private final Map<Method, Object> perceptBatch = new HashMap<Method, Object>();

	/**
	 * Event percepts pulled from bot.
	 */
	private final Map<Method, List<Object>> eventPerceptBatch = new HashMap<Method, List<Object>>();

	/**
	 * Entity which we pull percepts from.
	 */
	private final Object entity;

	public AllPerceptsModule(Object entity) throws EntityException {

		this.entity = entity;

		// Setup percept providers.
		Class<?> clazz = entity.getClass();
		perceptProviders = EIS2JavaUtil.processPerceptAnnotations(clazz);

	}

	/**
	 * Calls all percept providers and puts the result in a new batch.
	 * 
	 * @throws PerceiveException
	 */
	public synchronized void updatePercepts() throws PerceiveException {

		perceptBatch.clear();

		for (Method method : perceptProviders) {
			AsPercept annotation = method.getAnnotation(AsPercept.class);
			String perceptName = annotation.name();

			Object percept;
			try {
				percept = method.invoke(entity);
			} catch (IllegalArgumentException e) {
				throw new PerceiveException("Unable to update " + perceptName,
						e);
			} catch (IllegalAccessException e) {
				throw new PerceiveException("Unable to update " + perceptName,
						e);
			} catch (InvocationTargetException e) {
				throw new PerceiveException("Unable to update " + perceptName,
						e);
			}

			// Percept is event based, needs special handling.
			if (annotation.event()) {
				if (!eventPerceptBatch.containsKey(method)) {
					eventPerceptBatch.put(method, new ArrayList<Object>());
				}

				List<Object> events = eventPerceptBatch.get(method);

				// Percept provides multiple items, add them independently.
				if (!annotation.multiplePercepts()) {
					throw new PerceiveException("Unable to update "
							+ perceptName
							+ " event percept must have multiplePercepts.");
				}

				if (!(percept instanceof Collection<?>)) {
					throw new PerceiveException("Unable to update "
							+ perceptName
							+ " return value must be a collection.");
				}

				events.addAll((Collection<?>) percept);

			}
			// Regular percept
			else {
				perceptBatch.put(method, percept);
			}
		}

		perceptBatch.putAll(eventPerceptBatch);
	}

	public synchronized Map<Method, Object> getAllPercepts() {
		// We can clear outstanding events. They'll be exported now.
		eventPerceptBatch.clear();
		// Copy precept batch, we don't want to modify this one any more.
		return new HashMap<Method, Object>(perceptBatch);
	}
}
