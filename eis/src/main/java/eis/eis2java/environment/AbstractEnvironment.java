package eis.eis2java.environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eis.EIDefaultImpl;
import eis.eis2java.handlers.ActionHandler;
import eis.eis2java.handlers.DefaultActionHandler;
import eis.eis2java.handlers.DefaultPerceptHandler;
import eis.eis2java.handlers.PerceptHandler;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.Action;
import eis.iilang.Parameter;
import eis.iilang.Percept;

/**
 * Base implementation for environments that want to work with automated percept
 * and action discovery in EIS2Java.
 */
public abstract class AbstractEnvironment extends EIDefaultImpl {
	private static final long serialVersionUID = 1L;
	/** Map of entity names to objects representing those entities */
	private final Map<String, Object> entities = new HashMap<>();
	/** Maps an entity to an action handler */
	private final Map<String, PerceptHandler> perceptHandlers = new HashMap<>();
	/** Maps a Class to a map of action names and methods */
	private final Map<String, ActionHandler> actionHandlers = new HashMap<>();

	/**
	 * Couples a name to an entity and parses it's annotations for percepts and
	 * actions.
	 *
	 * @param name   The name of the entity.
	 * @param entity The entity itself.
	 * @param <T>    the type of the entity
	 * @throws EntityException if the entity could not be added.
	 */
	public final <T> void registerEntity(final String name, final T entity) throws EntityException {
		registerEntity(name, entity, new DefaultActionHandler(entity), new DefaultPerceptHandler(entity));
	}

	/**
	 * Couples a name to an entity and parses it's annotations for percepts and
	 * actions using the specified handlers.
	 *
	 * @param name           The name of the entity.
	 * @param entity         The entity itself.
	 * @param actionHandler  the associated action handler.
	 * @param <T>            the type of the entity
	 *
	 * @param perceptHandler the associated percept handler.
	 * @throws EntityException if the entity could not be added.
	 */
	public final <T> void registerEntity(final String name, final T entity, final ActionHandler actionHandler,
			final PerceptHandler perceptHandler) throws EntityException {
		this.actionHandlers.put(name, actionHandler);
		this.perceptHandlers.put(name, perceptHandler);

		this.entities.put(name, entity);
		addEntity(name);
	}

	/**
	 * Couples a name to an entity and parses it's annotations for percepts and
	 * actions.
	 *
	 * @param <T>    the type of the entity
	 *
	 * @param name   The name of the entity.
	 * @param type   The type of entity
	 * @param entity The entity itself.
	 * @throws EntityException if the entity could not be added.
	 */
	public final <T> void registerEntity(final String name, final String type, final T entity) throws EntityException {
		registerEntity(name, type, entity, new DefaultActionHandler(entity), new DefaultPerceptHandler(entity));
	}

	/**
	 * Couples a name to an entity and parses it's annotations for percepts and
	 * actions using the specified handlers.
	 * <p>
	 * Your environment must be able to handle
	 * {@link #getAllPercepts(String, String...)} and
	 * {@link #getAllPerceptsFromEntity(String)} when this is called.
	 *
	 *
	 * @param name           The name of the entity.
	 * @param type           The type of entity
	 * @param entity         The entity itself.
	 * @param actionHandler  the associated action handler.
	 * @param <T>            the type of the entity
	 *
	 * @param perceptHandler the associated percept handler.
	 * @throws EntityException if the entity could not be added.
	 */

	public final <T> void registerEntity(final String name, final String type, final T entity,
			final ActionHandler actionHandler, final PerceptHandler perceptHandler) throws EntityException {
		this.actionHandlers.put(name, actionHandler);
		this.perceptHandlers.put(name, perceptHandler);

		this.entities.put(name, entity);
		addEntity(name, type);
	}

	@Override
	public final void deleteEntity(final String name) throws EntityException, RelationException {
		super.deleteEntity(name);
		this.entities.remove(name);
		this.actionHandlers.remove(name);
		this.perceptHandlers.remove(name);
	}

	/**
	 * Retrieve an entity for a given name.
	 *
	 * @param <T>  The class of entity to return.
	 * @param name The name of the entity.
	 * @return the entity behind this name
	 */
	@SuppressWarnings("unchecked")
	public final <T> T getEntity(final String name) {
		return (T) this.entities.get(name);
	}

	@Override
	protected final List<Percept> getAllPerceptsFromEntity(final String name)
			throws PerceiveException, NoEnvironmentException {
		final PerceptHandler handler = this.perceptHandlers.get(name);
		if (handler == null) {
			throw new PerceiveException("Entity with name " + name + " has no handler");
		}

		return handler.getAllPercepts();
	}

	@Override
	protected final boolean isSupportedByEntity(final Action action, final String name) {
		final ActionHandler handler = this.actionHandlers.get(name);
		return handler.isSupportedByEntity(action);
	}

	@Override
	protected final Percept performEntityAction(final String name, final Action action) throws ActException {
		final ActionHandler handler = this.actionHandlers.get(name);
		if (handler == null) {
			throw new ActException(ActException.FAILURE, "Entity with name " + name + " has no handler");
		}

		return handler.performAction(action);
	}

	@Override
	public void reset(final Map<String, Parameter> parameters) throws ManagementException {
		super.reset(parameters);
		for (final PerceptHandler handler : this.perceptHandlers.values()) {
			handler.reset();
		}
		for (final ActionHandler handler : this.actionHandlers.values()) {
			handler.reset();
		}
	}
}
