package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Parameter;

/**
 * Interface for translating Java objects of type T to a {@link Parameter}.
 * 
 * @param <T>
 *            The type of the Java object to translate.
 * @author Lennard de Rijk
 */
public interface Java2Parameter<T> {

	/**
	 * Translates the object into an array of {@link Parameter}.
	 * 
	 * @param o
	 *            The object to translate.
	 * @return The array of {@link Parameter} that represents a translated
	 *         object.
	 * @throws TranslationException
	 *             if the translation can not be made.
	 */
	Parameter[] translate(T o) throws TranslationException;

	/**
	 * @return The class that is being translated.
	 */
	Class<? extends T> translatesFrom();
}
