package eis.eis2java.exception;

import eis.eis2java.translation.Translator;

/**
 * Exception raised when the {@link Translator} is unable to complete the
 * translation due to a missing translator.
 */
public class NoTranslatorException extends TranslationException {
	private static final long serialVersionUID = 1L;

	public NoTranslatorException() {
	}

	public NoTranslatorException(final String message) {
		super(message);
	}

	public NoTranslatorException(final Throwable cause) {
		super(cause);
	}

	public NoTranslatorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NoTranslatorException(final Class<? extends Object> parameterClass) {
		this("No translator found for class " + parameterClass.getName());
	}

	public NoTranslatorException(final Class<? extends Object> parameterClass, final Throwable cause) {
		this("No translator found for class " + parameterClass.getName(), cause);
	}
}
