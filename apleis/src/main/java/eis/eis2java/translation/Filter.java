package eis.eis2java.translation;

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
		 * Return all percepts only the first time.
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
