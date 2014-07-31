package eis.eis2java.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eis.eis2java.annotation.AsAction;
import eis.eis2java.annotation.AsPercept;
import eis.exceptions.EntityException;
import eis.iilang.Action;

public class EIS2JavaUtil {

	/**
	 * Processes all {@link AsPercept} annotations for the given class.
	 * 
	 * @return processed annotations
	 * 
	 * @throws EntityException
	 *             Thrown when the annotations are not used properly.
	 */
	public static Set<Method> processPerceptAnnotations(Class<?> clazz)
			throws EntityException {

		Set<Method> percepts = new HashSet<Method>();

		for (Method method : clazz.getMethods()) {
			AsPercept asPercept = method.getAnnotation(AsPercept.class);
			if (asPercept != null) {

				if (method.getParameterTypes().length != 0) {
					throw new EntityException(
							"Percepts may not have any arguments");
				}

				percepts.add(method);
			}

		}

		return percepts;
	}

	/**
	 * Processes all annotations for the given class. The annotations are
	 * provided as map action names and methods.
	 * 
	 * @return processed annotations
	 * 
	 * @throws EntityException
	 *             Thrown when the annotations are not used properly.
	 */
	public static Map<String, Method> processActionAnnotations(Class<?> clazz)
			throws EntityException {

		Map<String, Method> actions = new HashMap<String, Method>();

		for (Method method : clazz.getMethods()) {
			AsAction asAction = method.getAnnotation(AsAction.class);
			if (asAction != null) {
				String name = getNameOfAction(method);
				if (actions.containsKey(name)) {
					throw new EntityException(
							"Found two action definitions with the same name: "
									+ name);
				}
				actions.put(name, method);
			}

		}
		return actions;

	}

	/**
	 * Returns the name of the action used for storage. The name of the given
	 * action and the number of parameters are used as an identifier.
	 * 
	 * @param action
	 *            The action to get the name of.
	 */
	public static String getNameOfAction(Action action) {
		return action.getName() + "/" + action.getParameters().size();
	}

	/**
	 * Returns the name of the action method as name/number. The name given in
	 * the {@link AsAction#name()}. The number is the number of parameters.
	 * 
	 * Action names can be used to uniquely identify the action.
	 * 
	 * @param action
	 *            The method to get the name of.
	 * @return the name/number of the action or null when the method does not
	 *         have the AsAction annotation.
	 */
	public static String getNameOfAction(Method method) {
		AsAction annotation = method.getAnnotation(AsAction.class);

		if (annotation == null) {
			return null;
		}

		return annotation.name() + "/" + method.getParameterTypes().length;
	}

	/**
	 * Returns the name of the percept method. The name is given in the
	 * {@link AsPercept#name()}.
	 * 
	 * Percept names can not be used to uniquely identify percepts.
	 * 
	 * @param method
	 * @return the name of the percept or null when the method does not have the
	 *         AsPercept annotation.
	 */
	public static String getNameOfPercept(Method method) {
		AsPercept annotation = method.getAnnotation(AsPercept.class);
		if (annotation == null) {
			return null;
		}

		return annotation.name();
	}
}
