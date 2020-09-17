package eis.eis2java.exception;

import eis.eis2java.translation.Translator;

/**
 * Exception raised when the {@link Translator} is unable to complete the
 * translation.
 */
public class TranslationException extends Exception {
	private static final long serialVersionUID = 1L;

	public TranslationException() {
	}

	public TranslationException(final String message) {
		super(message);
	}

	public TranslationException(final Throwable cause) {
		super(cause);
	}

	public TranslationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
