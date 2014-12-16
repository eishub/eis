package eis.eis2java.translation;

import eis.eis2java.environment.AbstractEnvironment;

public class Filter {
	/**
	 * The filter type determines which percepts are passed through each cycle
	 * 
	 * @author W.Pasman 27sep2011
	 * 
	 */
	public enum Type {
		/**
		 * Return all percepts always
		 */
		ALWAYS,
		/**
		 * Return the percepts only the first time
		 * {@link AbstractEnvironment#getAllPercepts(String, String...)} is
		 * called.
		 */
		ONCE,
		/**
		 * Return only the percepts if something in the percepts changed. Return
		 * empty list if nothing changed.
		 */
		ON_CHANGE,
		/**
		 * Return only the newly appeared percepts, plus not(p) for all percepts
		 * that disappeared.
		 */
		ON_CHANGE_NEG
	};
}
