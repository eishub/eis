package eis.eis2java.translation;

import eis.eis2java.environment.AbstractEnvironment;

/**
 * The data filtering type determines how percepts are passed through from
 * environment.
 */
public class Filter {
	/**
	 * The filter type determines which percepts are passed through each cycle
	 */
	public enum Type {
		/**
		 * Return all percepts always
		 */
		ALWAYS,
		/**
		 * Return the percepts only the first time
		 * {@link AbstractEnvironment#getPercepts(String, String...)} is called.
		 */
		ONCE,
		/**
		 * Return only the percepts if something in the percepts changed. Return empty
		 * list if nothing changed.
		 */
		ON_CHANGE
	}
}
