package eis.eis2java.handlers;

import java.util.LinkedList;

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
	 */
	public abstract LinkedList<Percept> getAllPercepts()
			throws PerceiveException;

}