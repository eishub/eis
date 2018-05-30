package eis.eis2java.handlers;

import java.util.List;

import eis.eis2java.annotation.AsPercept;
import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

/**
 * The {@link AbstractEnvironment} delegates the actual collection of percepts
 * from an agent to the PerceptHandler.
 * 
 * @author mpkorstanje
 * 
 */
public abstract class PerceptHandler {

	/**
	 * Collects all percepts provided by the registered entity through
	 * {@link AsPercept} annotations.
	 * 
	 * @return a list of the collected percepts
	 * @throws PerceiveException
	 *             if percepts can not be fetched
	 */
	public abstract List<Percept> getAllPercepts() throws PerceiveException;

	/**
	 * Called when the entity is reset.
	 */
	public abstract void reset();

}