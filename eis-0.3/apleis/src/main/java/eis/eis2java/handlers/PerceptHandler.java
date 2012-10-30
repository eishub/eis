package eis.eis2java.handlers;

import java.util.LinkedList;

import eis.eis2java.annotation.AsPercept;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

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