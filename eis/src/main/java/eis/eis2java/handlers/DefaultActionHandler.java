package eis.eis2java.handlers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Map;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.eis2java.util.EIS2JavaUtil;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Percept;

/**
 * Default implementation of EIS2Java action handling. When an action is called,
 * the handler will call the associated action method on the agent.
 * 
 * @author mpkorstanje
 * 
 */

public class DefaultActionHandler extends ActionHandler {

	protected final Map<String, Method> actionMethods;
	protected final Object entity;

	public DefaultActionHandler(Object entity) throws EntityException {
		this.entity = entity;
		this.actionMethods = EIS2JavaUtil.processActionAnnotations(entity
				.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.tudelft.goal.EIS2Java.environment.ActionHandler#isSupportedByClass
	 * (eis.iilang.Action, java.lang.Class)
	 */
	@Override
	public final boolean isSupportedByEntity(Action action) {
		String actionName = EIS2JavaUtil.getNameOfAction(action);
		return actionMethods.get(actionName) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.tudelft.goal.EIS2Java.environment.ActionHandler#performAction(java
	 * .lang.Object, eis.iilang.Action)
	 */
	@Override
	public final Percept performAction(Action action) throws ActException {
		String actionName = EIS2JavaUtil.getNameOfAction(action);
		Method actionMethod = actionMethods.get(actionName);
		if (actionMethod == null) {
			throw new ActException(ActException.FAILURE,
					"Entity does not support action: " + action);
		}

		// workaround/fix for an EIS issue. #1986.
		try {
			return performAction(entity, actionMethod, action);
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
	 * @param action
	 *            The action that is being performed.
	 * @return The percept generated by the action.
	 * @throws ActException
	 *             if the action can not be performed for any reason.
	 */
	private Percept performAction(Object entity, Method method, Action action)
			throws ActException {
		Translator translator = Translator.getInstance();

		Class<?>[] parameterTypes = method.getParameterTypes();

		LinkedList<Parameter> parameters = action.getParameters();
		Object[] arguments = new Object[parameters.size()];

		// Doing it the hard way because LinkedList.get(index) sucks!
		int i = 0;
		for (Parameter parameter : parameters) {
			try {
				arguments[i] = translator.translate2Java(parameter,
						parameterTypes[i]);
			} catch (TranslationException e) {
				throw new ActException(ActException.FAILURE, "Action "
						+ action.getName() + " with parameters " + parameters
						+ " failed to be translated", e);
			}
			i++;
		}

		Object returnValue;
		try {
			returnValue = method.invoke(entity, arguments);
		} catch (IllegalArgumentException e) {
			throw new ActException(ActException.FAILURE, "Action "
					+ action.getName() + " with parameters " + parameters
					+ " failed to execute", e);
		} catch (IllegalAccessException e) {
			throw new ActException(ActException.FAILURE, "Action "
					+ action.getName() + " with parameters " + parameters
					+ " failed to execute", e);
		} catch (InvocationTargetException e) {
			throw new ActException(ActException.FAILURE, "Action "
					+ action.getName() + " with parameters " + parameters
					+ " failed to execute", e.getTargetException());
		}

		if (returnValue == null) {
			return null;
		} else {
			try {
				return new Percept(action.getName(),
						translator.translate2Parameter(returnValue));
			} catch (TranslationException e) {
				throw new ActException(ActException.FAILURE, "Action "
						+ action.getName() + " with parameters " + parameters
						+ " failed to return a proper failue", e);
			}
		}
	}

	@Override
	public void reset() {
		// nothing to reset here
	}

}
