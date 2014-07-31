package eis.eis2java.util;

import java.lang.reflect.Method;
import java.util.Map;

import eis.eis2java.annotation.AsPercept;
import eis.eis2java.handlers.AllPerceptPerceptHandler;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;

/**
 * Interface for agents that use the {@link AllPerceptPerceptHandler}.
 * 
 * This implementation can be greatly simplified by using the
 * {@link AllPerceptsModule} in the agent. This module will use the
 * {@link AsPercept} annotations to gather percepts.
 * 
 * @author mpkorstanje
 * 
 * 
 */
public interface AllPerceptsProvider {

	/**
	 * A batch of percepts should be returned as a map of the methods annotated
	 * with {@link AsPercept} and the results of their invocation.
	 * 
	 * Note that when {@link AsPercept.event()} is true for a percept, the
	 * implementation must store all results from that percept until this method
	 * is called.
	 * 
	 * @return a previously prepared batch of percepts.
	 * @throws EntityException
	 */
	public Map<Method, Object> getAllPercepts() throws PerceiveException;

}
