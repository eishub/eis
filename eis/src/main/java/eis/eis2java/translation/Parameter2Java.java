package eis.eis2java.translation;

import eis.eis2java.exception.TranslationException;
import eis.iilang.Parameter;

/**
 * Interface for translating a {@link Parameter} into a Java object of Type T.
 * 
 * @param <T>
 *            The type of the Java object to translate.
 * @author Lennard de Rijk
 */
public interface Parameter2Java<T> {

	/**
	 * Translates the {@link Parameter} into an object of type T.
	 * 
	 * @param <T>
	 *            The type of object to translate the parameter to.
	 * @param parameter
	 *            The parameter to translate.
	 * @return The {@link Parameter} that represents a translated object.
	 * @throws TranslationException
	 *             if the translation can not be made.
	 */
	T translate(Parameter parameter) throws TranslationException;

	/**
	 * @return The class that is translated to.
	 */
	Class<T> translatesTo();
}
