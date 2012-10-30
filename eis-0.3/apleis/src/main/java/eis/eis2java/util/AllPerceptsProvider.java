package eis.eis2java.util;

import java.lang.reflect.Method;
import java.util.Map;

import eis.eis2java.annotation.AsPercept;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;

/**
 * Interface for agents that can not provide percepts one at a time. These
 * objects agents prepare a batch of percepts ahead of time by analyzing their
 * own annotations.
 * 
 * @author mpkorstanje
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
