package eis.eis2java.exception;

import eis.eis2java.translation.Translator;

/**
 * Exception raised when the {@link Translator} is unable to complete the
 * translation.
 * 
 * @author Lennard de Rijk
 */
public class TranslationException extends Exception {

	private static final long serialVersionUID = 1L;

	public TranslationException() {
	}

	public TranslationException(String message) {
		super(message);
	}

	public TranslationException(Throwable cause) {
		super(cause);
	}

	public TranslationException(String message, Throwable cause) {
		super(message, cause);
	}

}
