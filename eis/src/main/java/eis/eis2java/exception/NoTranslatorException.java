package eis.eis2java.exception;

import eis.eis2java.translation.Translator;

public class NoTranslatorException extends TranslationException {
	/**
	 * Exception raised when the {@link Translator} is unable to complete the
	 * translation due to a missing translator.
	 * 
	 * @author Lennard de Rijk
	 */
	private static final long serialVersionUID = 1L;

	public NoTranslatorException() {
	}

	public NoTranslatorException(String message) {
		super(message);
	}

	public NoTranslatorException(Throwable cause) {
		super(cause);
	}

	public NoTranslatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoTranslatorException(Class<? extends Object> parameterClass) {
		this("No translator found for class " + parameterClass.getName());
	}

	public NoTranslatorException(Class<? extends Object> parameterClass,
			Throwable cause) {
		this("No translator found for class " + parameterClass.getName(), cause);
	}

}
