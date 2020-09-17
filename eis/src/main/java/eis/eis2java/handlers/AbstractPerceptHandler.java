package eis.eis2java.handlers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eis.PerceptUpdate;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Filter;
import eis.eis2java.translation.Translator;
import eis.exceptions.PerceiveException;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import eis.iilang.Percept;

/**
 * Abstract handler for percepts. Provides access to the entity, the translation
 * and unpacking phases of EIS2java.
 */
public abstract class AbstractPerceptHandler implements PerceptHandler {
	/**
	 * The entity associated with this handler.
	 */
	protected final Object entity;

	/**
	 * @param entity the entity which produced the percepts
	 */
	public AbstractPerceptHandler(final Object entity) {
		assert entity != null;
		this.entity = entity;
	}

	/**
	 * map of previous percepts that we got for each method.
	 */
	protected final Map<Method, List<Percept>> previousPercepts = new HashMap<>();
	/**
	 * a shadow copy for on-change percepts only
	 */
	protected final Map<Method, List<Percept>> shadow = new HashMap<>();

	/**
	 * Translates the percept objects and applies filtering as described by
	 * {@link AsPercept#filter()}.
	 *
	 * @param method         the method which produced the percepts
	 * @param perceptObjects the java percept objects that need to be translated
	 *                       into {@link Percept}s. Each object is converted into
	 *                       one percept.
	 * @return list of {@link Percept} objects.
	 * @throws PerceiveException if an attempt to perform an action or to retrieve
	 *                           percepts has failed
	 */
	protected final PerceptUpdate translatePercepts(final Method method, final List<Object> perceptObjects)
			throws PerceiveException {
		final AsPercept annotation = method.getAnnotation(AsPercept.class);
		final Filter.Type filter = annotation.filter();
		final String perceptName = annotation.name();

		List<Percept> previous = this.previousPercepts.get(method);

		// Avoid translating objects that don't need to be translated.
		if (filter == Filter.Type.ONCE && previous != null) {
			final PerceptUpdate returned = new PerceptUpdate(new ArrayList<>(0), previous);
			this.previousPercepts.put(method, new ArrayList<>(0));
			return returned;
		}

		if (previous == null) {
			previous = new ArrayList<>(0);
		}

		// Translate objects.
		final List<Percept> percepts = new ArrayList<>(perceptObjects.size());
		for (final Object javaObject : perceptObjects) {
			Parameter[] parameters;
			try {
				parameters = Translator.getInstance().translate2Parameter(javaObject);
				if (annotation.multipleArguments()) {
					parameters = extractMultipleParameters(parameters);
				}
			} catch (final TranslationException e) {
				throw new PerceiveException("Unable to translate percept " + perceptName, e);
			}
			percepts.add(new Percept(perceptName, parameters));
		}

		// do the proper filtering.
		List<Percept> addList = new ArrayList<>(0);
		List<Percept> delList = new ArrayList<>(0);
		switch (filter) {
		case ONCE:
			addList = percepts;
			break;
		case ALWAYS:
			addList = percepts;
			addList.removeAll(previous);
			delList = previous;
			delList.removeAll(percepts);
			break;
		case ON_CHANGE: // FIXME: how to do this?!
			addList = percepts;
			final List<Percept> shadow = this.shadow.get(method);
			if (shadow == null) {
				this.shadow.put(method, percepts);
			} else {
				addList.removeAll(previous);
				final Iterator<Percept> iterator = shadow.iterator();
				while (iterator.hasNext()) {
					final Percept next = iterator.next();
					if (!addList.contains(next)) {
						delList.add(next);
						iterator.remove();
					}
				}
			}
			break;
		}

		this.previousPercepts.put(method, percepts);
		return new PerceptUpdate(addList, delList);
	}

	/**
	 * Handle multiple arguments. The parameter list must be a list containing just
	 * one {@link ParameterList}. Returns a list with all elements in that
	 * {@link ParameterList}.
	 *
	 * @param parameters
	 * @return
	 * @throws PerceiveException if parameters is not the right format.
	 */
	private Parameter[] extractMultipleParameters(Parameter[] parameters) throws PerceiveException {
		if (parameters.length == 1 && parameters[0] instanceof ParameterList) {
			// special case where the top set is the set of arguments for function
			final ParameterList params = (ParameterList) parameters[0];
			parameters = new Parameter[params.size()];
			for (int i = 0; i < params.size(); i++) {
				parameters[i] = params.get(i);
			}
		} else {
			throw new PerceiveException(
					"multipleArguments parameter is set and therefore expecting a set but got " + parameters);
		}
		return parameters;
	}

	/**
	 * Depending on {@link AsPercept#multiplePercepts()} a perceptObject is either a
	 * collection that contains multiple percept objects or a single percept. This
	 * method unpacks either case into a list of percept objects. The
	 * {@link AsPercept#multipleArguments()} aspect is not handled here.
	 *
	 * @param method        that generated the percept object
	 * @param perceptObject the perceptObject generated by the method.
	 * @return a unpacked version of the percept object. A null perceptObject is
	 *         translated into an empty list.
	 * @throws PerceiveException if an attempt to perform an action or to retrieve
	 *                           percepts has failed
	 */
	@SuppressWarnings("unchecked")
	protected final List<Object> unpackPerceptObject(final Method method, final Object perceptObject)
			throws PerceiveException {
		final AsPercept annotation = method.getAnnotation(AsPercept.class);
		final String perceptName = annotation.name();

		if (!annotation.multiplePercepts()) {
			// This is percept does not provide multiples.
			final List<Object> unpacked = new ArrayList<>(1);
			if (perceptObject != null) {
				unpacked.add(perceptObject);
			}

			return unpacked;
		}

		// The method claims to provide multiple but doesn't provide a
		// collection.
		if (!(perceptObject instanceof Collection<?>)) {
			throw new PerceiveException("Unable to perceive " + perceptName
					+ " because a collection was expected but a " + perceptObject.getClass() + " was returned instead");
		}

		// The multiple percepts are a collection, put them in a list.
		final Collection<?> javaCollection = (Collection<?>) perceptObject;
		if (javaCollection instanceof List<?>) {
			return (List<Object>) javaCollection;
		} else {
			final List<Object> unpacked = new ArrayList<>(javaCollection);
			return unpacked;
		}
	}

	@Override
	public void reset() {
		this.previousPercepts.clear();
	}
}