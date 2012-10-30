package eis.eis2java.environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eis.EIDefaultImpl;
import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Filter;
import eis.eis2java.translation.Translator;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Function;
import eis.iilang.Parameter;
import eis.iilang.Percept;

/**
 * Base implementation for environments that want to work with automated percept
 * and action discovery in EIS2Java.
 * 
 * @author Lennard de Rijk
 */
public abstract class AbstractEnvironment extends EIDefaultImpl {
	private static final long serialVersionUID = 1L;
	/** Map of entity names to objects representing those entities */
	private final Map<String, Object> entities;
	/** Set of classes for which the percepts and actions have been processed */
	private final Set<Class<?>> processedClasses;
	/** Maps a Class to a map of percept names and methods */
	private final Map<Class<?>, Map<String, Method>> allPercepts;
	/** Maps a Class to a map of action names and methods */
	private final Map<Class<?>, Map<String, Method>> allActions;

	/**
	 * map of previous percepts that we got for each method. FIXME the first
	 * "Object" is the entity. The type checking is really lousy here...
	 */
	private Map<Object, Map<Method, List<Object>>> previousPercepts = new HashMap<Object, Map<Method, List<Object>>>();

	public AbstractEnvironment() {
		super();

		processedClasses = new HashSet<Class<?>>();
		entities = new HashMap<String, Object>();
		allPercepts = new HashMap<Class<?>, Map<String, Method>>();
		allActions = new HashMap<Class<?>, Map<String, Method>>();
	}

	/**
	 * Couples a name to an entity and parses it's annotations for percepts and
	 * actions.
	 * 
	 * @param name
	 *            The name of the entity.
	 * @param entity
	 *            The entity itself.
	 * @throws EntityException
	 *             if the entity could not be added.
	 */
	public final <T> void registerEntity(String name, T entity)
			throws EntityException {
		processAnnotations(entity.getClass());
		entities.put(name, entity);
		addEntity(name);
	}

	/**
	 * Couples a name to an entity and parses it's annotations for percepts and
	 * actions.
	 * 
	 * @param name
	 *            The name of the entity.
	 * @param type
	 *            The type of entity
	 * @param entity
	 *            The entity itself.
	 * @throws EntityException
	 *             if the entity could not be added.
	 */
	public final <T> void registerEntity(String name, String type, T entity)
			throws EntityException {
		processAnnotations(entity.getClass());
		entities.put(name, entity);
		addEntity(name, type);
	}

	@Override
	public final void deleteEntity(String name) throws EntityException,
			RelationException {
		super.deleteEntity(name);
		entities.remove(name);
	}

	/**
	 * Retrieve an entity for a given name.
	 * 
	 * @param <T>
	 *            The class of entity to return.
	 * @param name
	 *            The name of the entity.
	 */
	@SuppressWarnings("unchecked")
	public final <T> T getEntity(String name) {
		return (T) entities.get(name);
	}

	/**
	 * Processes and caches all annotations for the given class. If the class
	 * has already been processed this method will do nothing.
	 * 
	 * @param clazz
	 *            The class to process
	 * @throws EntityException
	 *             Thrown when the annotations are not used properly.
	 */
	private void processAnnotations(Class<?> clazz) throws EntityException {
		if (processedClasses.contains(clazz)) {
			// Already processed.
			return;
		}

		Map<String, Method> percepts = new HashMap<String, Method>();
		Map<String, Method> actions = new HashMap<String, Method>();
		for (Method method : clazz.getMethods()) {
			AsPercept asPercept = method.getAnnotation(AsPercept.class);
			if (asPercept != null) {
				String name = asPercept.name();
				if (percepts.containsKey(name)) {
					throw new EntityException(
							"Found two percept definitions with the same name: "
									+ name);
				}
				percepts.put(name, method);
			}

			AsAction asAction = method.getAnnotation(AsAction.class);
			if (asAction != null) {
				String name = asAction.name() + "/"
						+ method.getParameterTypes().length;
				if (actions.containsKey(name)) {
					throw new EntityException(
							"Found two action definitions with the same name: "
									+ name);
				}
				actions.put(name, method);
			}
		}
		allPercepts.put(clazz, percepts);
		allActions.put(clazz, actions);
		processedClasses.add(clazz);
	}

	@Override
	protected final LinkedList<Percept> getAllPerceptsFromEntity(String name)
			throws PerceiveException, NoEnvironmentException {
		Object entity = getEntity(name);

		if (entity == null) {
			throw new PerceiveException("Entity with name " + name
					+ " does not exist");
		}

		Map<String, Method> allPerceptMethods = allPercepts.get(entity
				.getClass());

		LinkedList<Percept> percepts = new LinkedList<Percept>();
		for (Entry<String, Method> entry : allPerceptMethods.entrySet()) {
			String perceptName = entry.getKey();
			Method method = entry.getValue();
			List<Percept> percept = getPercepts(entity, perceptName, method);
			percepts.addAll(percept);
		}
		return percepts;
	}

	/**
	 * Creates new percepts by calling the given method on the entity.
	 * 
	 * @param entity
	 *            the entity to get the percept from.
	 * @param perceptName
	 *            the name of the percept.
	 * @param method
	 *            the method to invoke on the entity which must be annotated
	 *            with {@link AsPercept}.
	 * @return The percepts that were generated by invoking the method on the
	 *         entity.
	 * @throws PerceiveException
	 *             If the percepts couldn't be retrieved.
	 */
	private List<Percept> getPercepts(Object entity, String perceptName,
			Method method) throws PerceiveException {

		// list of new objects for the percepts
		List<Object> perceptObjects = new ArrayList<Object>();

		// the add and delete list based on the perceived objects
		// list of object that we had last round but not at this moment.
		List<Object> addList = new ArrayList<Object>();
		List<Object> delList = new ArrayList<Object>();

		AsPercept annotation = method.getAnnotation(AsPercept.class);

		Map<Method, List<Object>> previousPerceptsForEntity = previousPercepts
				.get(entity);
		if (previousPerceptsForEntity == null) {
			previousPerceptsForEntity = new HashMap<Method, List<Object>>();
			previousPercepts.put(entity, previousPerceptsForEntity);
		}
		List<Object> previous = previousPerceptsForEntity.get(method);
		Filter.Type filter = annotation.filter();

		if (filter != Filter.Type.ONCE || previous == null) {
			perceptObjects = getPerceptObjects(method, entity, perceptName);
		}

		// do the proper filtering.
		switch (filter) {
		case ALWAYS:
		case ONCE:
			addList = perceptObjects;
			break;
		case ON_CHANGE:
			if (!perceptObjects.equals(previous)) {
				addList = perceptObjects;
			}
			break;
		case ON_CHANGE_NEG:
			addList.addAll(perceptObjects);
			if (previous != null) {
				addList.removeAll(previous);
				delList.addAll(previous);
				delList.removeAll(perceptObjects);
			}
			break;
		}

		// Translate addList.
		List<Percept> percepts = new ArrayList<Percept>();
		for (Object javaObject : addList) {
			Parameter[] parameters;
			try {
				parameters = Translator.getInstance().translate2Parameter(
						javaObject);
			} catch (TranslationException e) {
				throw new PerceiveException("Unable to translate percept "
						+ perceptName, e);
			}
			percepts.add(new Percept(perceptName, parameters));
		}

		// Translate delList.
		for (Object javaObject : delList) {
			Parameter[] parameters;
			try {
				parameters = Translator.getInstance().translate2Parameter(
						javaObject);
			} catch (TranslationException e) {
				throw new PerceiveException("Unable to translate percept "
						+ perceptName, e);
			}
			percepts.add(new Percept("not", new Function(perceptName,
					parameters)));
		}

		previousPerceptsForEntity.put(method, perceptObjects);
		return percepts;
	}

	/**
	 * Get the percept objects for given percept name, using method. CHECK why
	 * use all these params, we can get name from the method, right?
	 * 
	 * @param method
	 * @param entity
	 * @param perceptName
	 * @return
	 * @throws PerceiveException
	 */
	private List<Object> getPerceptObjects(Method method, Object entity,
			String perceptName) throws PerceiveException {
		// Retrieve the object that is generated by the method.
		List<Object> generatedJavaObjects = new ArrayList<Object>();
		Object returnValue;
		try {
			returnValue = method.invoke(entity);
		} catch (IllegalArgumentException e) {
			throw new PerceiveException("Unable to perceive " + perceptName, e);
		} catch (IllegalAccessException e) {
			throw new PerceiveException("Unable to perceive " + perceptName, e);
		} catch (InvocationTargetException e) {
			throw new PerceiveException("Unable to perceive " + perceptName, e);
		}

		AsPercept annotation = method.getAnnotation(AsPercept.class);
		if (!annotation.multiplePercepts()) {
			generatedJavaObjects = new ArrayList<Object>(1);
			if (returnValue != null) {
				generatedJavaObjects.add(returnValue);
			}
		} else {
			if (!(returnValue instanceof Collection<?>)) {
				throw new PerceiveException("Unable to perceive " + perceptName
						+ " because a collection was expected but a "
						+ returnValue.getClass() + " was returned instead");
			}

			Collection<?> javaCollection = (Collection<?>) returnValue;
			generatedJavaObjects = new ArrayList<Object>(javaCollection.size());
			for (Object o : javaCollection) {
				generatedJavaObjects.add(o);
			}
		}
		return generatedJavaObjects;
	}

	@Override
	protected final boolean isSupportedByEntity(Action action, String name) {
		Object entity = getEntity(name);
		Map<String, Method> supportedActions = allActions
				.get(entity.getClass());
		return supportedActions != null
				&& supportedActions.containsKey(getNameOfAction(action));
	}

	/**
	 * Returns the name of the action used in the internal storage. The name of
	 * the given action and the number of parameters are used as an identifier.
	 * 
	 * @param action
	 *            The action to get the name of.
	 */
	private String getNameOfAction(Action action) {
		return action.getName() + "/" + action.getParameters().size();
	}

	@Override
	protected final Percept performEntityAction(String name, Action action)
			throws ActException {
		Object entity = getEntity(name);
		if (entity == null) {
			throw new ActException(ActException.FAILURE, "Entity with name "
					+ name + " does not exist");
		}

		Map<String, Method> supportedActions = allActions
				.get(entity.getClass());
		String actionName = getNameOfAction(action);
		Method actionMethod = supportedActions.get(actionName);
		if (actionMethod == null) {
			throw new ActException(ActException.FAILURE,
					"Entity does not support action: " + action);
		}

		// workaround/fix for an EIS issue. #1986.
		try {
			return performAction(entity, actionMethod, action.getName(),
					action.getParameters());
		} catch (Exception e) {
			if (e instanceof ActException) {
				throw (ActException) e;
			}
			throw new ActException(ActException.FAILURE, "execution of action "
					+ action + "failed", e);
		}
	}

	/**
	 * Performs the action method on the given method using the parameters. The
	 * returned {@link Percept} will have the same name as the action.
	 * 
	 * @param entity
	 *            The entity to perform the method on.
	 * @param method
	 *            The method to invoke on the entity.
	 * @param actionName
	 *            The name of the action that is being performed.
	 * @param parameters
	 *            The parameters to the method (before translation).
	 * @return The percept generated by the action.
	 * @throws ActException
	 *             if the action can not be performed for any reason.
	 */
	private Percept performAction(Object entity, Method method,
			String actionName, LinkedList<Parameter> parameters)
			throws ActException {
		Translator translator = Translator.getInstance();

		Class<?>[] parameterTypes = method.getParameterTypes();

		Object[] arguments = new Object[parameters.size()];
		// Doing it the hard way because LinkedList.get(index) sucks!
		int i = 0;
		for (Parameter parameter : parameters) {
			try {
				arguments[i] = translator.translate2Java(parameter,
						parameterTypes[i]);
			} catch (TranslationException e) {
				throw new ActException(ActException.FAILURE, "Action "
						+ actionName + " with parameters " + parameters
						+ " failed to be translated", e);
			}
			i++;
		}

		Object returnValue;
		try {
			returnValue = method.invoke(entity, arguments);
		} catch (IllegalArgumentException e) {
			throw new ActException(ActException.FAILURE, "Action " + actionName
					+ " with parameters " + parameters + " failed to execute",
					e);
		} catch (IllegalAccessException e) {
			throw new ActException(ActException.FAILURE, "Action " + actionName
					+ " with parameters " + parameters + " failed to execute",
					e);
		} catch (InvocationTargetException e) {
			throw new ActException(ActException.FAILURE, "Action " + actionName
					+ " with parameters " + parameters + " failed to execute",
					e);
		}

		if (returnValue == null) {
			return null;
		} else {
			try {
				return new Percept(actionName,
						translator.translate2Parameter(returnValue));
			} catch (TranslationException e) {
				throw new ActException(ActException.FAILURE, "Action "
						+ actionName + " with parameters " + parameters
						+ " failed to return a proper failue", e);
			}
		}
	}

	@Override
	public final String requiredVersion() {
		return "0.3";
	}
}
