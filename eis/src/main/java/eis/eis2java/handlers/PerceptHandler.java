package eis.eis2java.handlers;

import eis.PerceptUpdate;
import eis.eis2java.annotation.AsPercept;
import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.PerceiveException;

/**
 * The {@link AbstractEnvironment} delegates the actual collection of percepts
 * from an agent to the PerceptHandler.
 */
public interface PerceptHandler {
	/**
	 * Collects all percepts provided by the registered entity through
	 * {@link AsPercept} annotations.
	 *
	 * @return a list of the collected percepts
	 * @throws PerceiveException if percepts can not be fetched
	 */
	PerceptUpdate getPercepts() throws PerceiveException;

	/**
	 * Called when the entity is reset.
	 */
	void reset();
}