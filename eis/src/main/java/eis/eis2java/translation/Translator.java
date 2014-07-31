package eis.eis2java.translation;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Iterator;

import eis.eis2java.exception.NoTranslatorException;
import eis.eis2java.exception.TranslationException;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import eis.iilang.TruthValue;

/**
 * Singleton class that supports translation from {@link Object} to
 * {@link Parameter} and vice versa.
 * 
 * @author Lennard de Rijk
 */
public class Translator {

	/** Singleton instance of the translator */
	private static Translator singleton;

	/**
	 * @return Creates or gets the singleton instance of the {@link Translator}.
	 */
	public static Translator getInstance() {
		if (singleton == null) {
			singleton = new Translator();
		}
		return singleton;
	}

	/**
	 * All translators for {@link Object} to {@link Parameter} are located here.
	 */
	private final HashMap<Class<?>, Java2Parameter<?>> java2ParameterTranslators;
	/**
	 * All translators for {@link Parameter} to {@link Object} are located here.
	 */
	private final HashMap<Class<?>, Parameter2Java<?>> parameter2JavaTranslators;

	private Translator() {
		// Non-instantiable outside of class.
		java2ParameterTranslators = new HashMap<Class<?>, Java2Parameter<?>>();
		parameter2JavaTranslators = new HashMap<Class<?>, Parameter2Java<?>>();

		NumberTranslator numberTranslator = new NumberTranslator();
		registerJava2ParameterTranslator(numberTranslator);
		registerParameter2JavaTranslator(numberTranslator);

		BooleanTranslator booleanTranslator = new BooleanTranslator();
		registerJava2ParameterTranslator(booleanTranslator);
		registerParameter2JavaTranslator(booleanTranslator);

		CharTranslator charTranslator = new CharTranslator();
		registerJava2ParameterTranslator(charTranslator);
		registerParameter2JavaTranslator(charTranslator);

		StringTranslator stringTranslator = new StringTranslator();
		registerJava2ParameterTranslator(stringTranslator);
		registerParameter2JavaTranslator(stringTranslator);

		@SuppressWarnings("rawtypes")
		CollectionTranslator<?> collectionTranslator = new CollectionTranslator();
		registerJava2ParameterTranslator(collectionTranslator);

		registerParameter2JavaTranslator(new IntegerTranslator());
		registerParameter2JavaTranslator(new LongTranslator());
		registerParameter2JavaTranslator(new ShortTranslator());
		registerParameter2JavaTranslator(new DoubleTranslator());
		registerParameter2JavaTranslator(new FloatTranslator());
	}

	/**
	 * Registers a translator for Java to {@link Parameter}.
	 * 
	 * @param translator
	 *            the translator to register.
	 */
	public void registerJava2ParameterTranslator(Java2Parameter<?> translator) {
		java2ParameterTranslators.put(translator.translatesFrom(), translator);
	}

	/**
	 * Registers a translator for {@link Parameter} to Java.
	 * 
	 * @param translator
	 *            the translator to register.
	 */
	public void registerParameter2JavaTranslator(Parameter2Java<?> translator) {
		parameter2JavaTranslators.put(translator.translatesTo(), translator);
	}

	/**
	 * Translates the given object into a {@link Parameter}. The Translator must
	 * contain a translator for the type T or of a superclass of T. Otherwise no
	 * translation can be made.
	 * 
	 * @param <T>
	 *            The type of the object to translate.
	 * 
	 * @param o
	 *            the object to translate.
	 * @return The object translated into the parameter.
	 * @throws TranslationException
	 *             If the translation could not be made.
	 */
	@SuppressWarnings("unchecked")
	public <T> Parameter[] translate2Parameter(T o) throws TranslationException {
		Java2Parameter<?> rawTranslator = null;

		Class<?> clazz = o.getClass();

		if (clazz.isPrimitive()) {
			clazz = getWrapper(clazz);
		}

		// Go up the super class tree until we find a class we can translate
		while (clazz != null && rawTranslator == null) {
			rawTranslator = java2ParameterTranslators.get(clazz);
			clazz = clazz.getSuperclass();
		}

		if (rawTranslator == null) {
			throw new NoTranslatorException(o.getClass());
		}

		Java2Parameter<T> translator = (Java2Parameter<T>) rawTranslator;
		return translator.translate(o);
	}

	/**
	 * Translates the given parameter into an object of type T. The Translator
	 * must contain a translator to type T otherwise no translation can be made.
	 * 
	 * @param <T>
	 *            The type to translate to.
	 * @param parameter
	 *            The parameter to translate.
	 * @param parameterClass
	 *            The class to which the parameter should be translated.
	 * @return The parameter translated into the object of class T.
	 * @throws TranslationException
	 *             if the translation could not be made.
	 * @throws NoTranslatorException
	 */
	@SuppressWarnings("unchecked")
	public <T> T translate2Java(Parameter parameter, Class<T> parameterClass)
			throws TranslationException, NoTranslatorException {
		Class<?> clazz = parameterClass;
		if (clazz.isPrimitive()) {
			clazz = getWrapper(clazz);
		}

		Parameter2Java<?> rawTranslator = parameter2JavaTranslators.get(clazz);

		if (rawTranslator == null) {
			throw new NoTranslatorException(clazz);
		}

		Parameter2Java<T> translator = (Parameter2Java<T>) rawTranslator;
		return translator.translate(parameter);
	}

	/**
	 * Returns the class that wraps the given primitive class.
	 * 
	 * @param clazz
	 *            primitive class to get the wrapper class for.
	 */
	private Class<?> getWrapper(Class<?> clazz) {
		if (Integer.TYPE.equals(clazz)) {
			return Integer.class;
		} else if (Boolean.TYPE.equals(clazz)) {
			return Boolean.class;
		} else if (Long.TYPE.equals(clazz)) {
			return Long.class;
		} else if (Float.TYPE.equals(clazz)) {
			return Float.class;
		} else if (Character.TYPE.equals(clazz)) {
			return Character.class;
		} else if (Double.TYPE.equals(clazz)) {
			return Double.class;
		} else if (Byte.TYPE.equals(clazz)) {
			return Byte.class;
		} else if (Short.TYPE.equals(clazz)) {
			return Short.class;
		} else if (Void.TYPE.equals(clazz)) {
			return Void.class;
		}
		return null;
	}

	/**
	 * Default translator for {@link Number} to {@link Numeral} and vice versa.
	 */
	private static class NumberTranslator implements Java2Parameter<Number>,
			Parameter2Java<Number> {
		@Override
		public Parameter[] translate(Number n) throws TranslationException {
			return new Parameter[] { new Numeral(n) };
		}

		@Override
		public Class<Number> translatesFrom() {
			return Number.class;
		}

		@Override
		public Number translate(Parameter parameter)
				throws TranslationException {
			if (!(parameter instanceof Numeral)) {
				throw new TranslationException(
						"Expected a numeral parameter but got " + parameter);
			}
			return ((Numeral) parameter).getValue();
		}

		@Override
		public Class<Number> translatesTo() {
			return Number.class;
		}
	}

	/**
	 * Default translator for {@link Boolean} to {@link TruthValue} and vice
	 * versa.
	 */
	private static class BooleanTranslator implements Java2Parameter<Boolean>,
			Parameter2Java<Boolean> {
		@Override
		public Parameter[] translate(Boolean b) throws TranslationException {
			return new Parameter[] { new TruthValue(b) };
		}

		@Override
		public Class<Boolean> translatesFrom() {
			return Boolean.class;
		}

		@Override
		public Boolean translate(Parameter parameter)
				throws TranslationException {
			if (!(parameter instanceof TruthValue)) {
				throw new TranslationException(
						"Expected a Truthvalue parameter but got " + parameter);
			}
			return ((TruthValue) parameter).getBooleanValue();
		}

		@Override
		public Class<Boolean> translatesTo() {
			return Boolean.class;
		}
	}

	/**
	 * Translates a {@link Char} into an {@link Identifier} and vice versa.
	 */
	private static class CharTranslator implements Java2Parameter<Character>,
			Parameter2Java<Character> {
		@Override
		public Character translate(Parameter parameter)
				throws TranslationException {
			if (!(parameter instanceof Identifier)) {
				throw new TranslationException(
						"Expected an Identifier parameter but got " + parameter);
			}

			Identifier id = (Identifier) parameter;
			String value = id.getValue();

			if (value.length() > 1) {
				throw new TranslationException(
						"A single character was expected instead a string of length "
								+ value.length() + " was given. Contents: "
								+ value);
			}

			return value.charAt(0);
		}

		@Override
		public Class<Character> translatesTo() {
			return Character.class;
		}

		@Override
		public Parameter[] translate(Character value)
				throws TranslationException {
			return new Parameter[] { new Identifier(String.valueOf(value)) };
		}

		@Override
		public Class<Character> translatesFrom() {
			return Character.class;
		}
	}

	/**
	 * Translates a {@link String} into an {@link Identifier} and vice versa.
	 */
	private static class StringTranslator implements Java2Parameter<String>,
			Parameter2Java<String> {
		@Override
		public String translate(Parameter parameter)
				throws TranslationException {
			if (!(parameter instanceof Identifier)) {
				throw new TranslationException(
						"Expected an Identifier parameter but got " + parameter);
			}

			Identifier id = (Identifier) parameter;
			return id.getValue();
		}

		@Override
		public Class<String> translatesTo() {
			return String.class;
		}

		@Override
		public Parameter[] translate(String value) throws TranslationException {
			return new Parameter[] { new Identifier(value) };
		}

		@Override
		public Class<String> translatesFrom() {
			return String.class;
		}
	}

	/**
	 * Translates a {@link AbstractCollection} into an {@link ParameterList}.
	 */
	private static class CollectionTranslator<T> implements
			Java2Parameter<AbstractCollection<T>> {
		@Override
		public Parameter[] translate(AbstractCollection<T> value)
				throws TranslationException {
			Parameter[] parameters = new Parameter[value.size()];

			int i = 0;
			Iterator<T> it = value.iterator();
			Translator translator = Translator.getInstance();

			while (it.hasNext()) {
				Parameter[] translation = translator.translate2Parameter(it
						.next());
				if (translation.length == 1) {
					// Translations into single parameters are unwrapped.
					parameters[i] = translation[0];
				} else {
					parameters[i] = new ParameterList(translation);
				}
				i++;
			}
			return new Parameter[] { new ParameterList(parameters) };
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<? extends AbstractCollection<T>> translatesFrom() {
			// mpkorstanje 2012-05-24: When using OpenJDK we can't cast directly
			// from
			// Class<AbstractCollection> to Class<? extends
			// AbstractCollection<T>>. This is a work around.
			@SuppressWarnings("rawtypes")
			Class cls = AbstractCollection.class;
			return (Class<? extends AbstractCollection<T>>) cls;
		}
	}

	/**
	 * Translates {@link Numeral} into an {@link Integer}.
	 */
	private static class IntegerTranslator implements Parameter2Java<Integer> {
		@Override
		public Integer translate(Parameter parameter)
				throws TranslationException {
			if (!(parameter instanceof Numeral)) {
				throw new TranslationException(
						"Expected a numeral parameter but got " + parameter);
			}
			return ((Numeral) parameter).getValue().intValue();
		}

		@Override
		public Class<Integer> translatesTo() {
			return Integer.class;
		}
	}

	/**
	 * Translates {@link Numeral} into a {@link Long}.
	 */
	private static class LongTranslator implements Parameter2Java<Long> {
		@Override
		public Long translate(Parameter parameter) throws TranslationException {
			if (!(parameter instanceof Numeral)) {
				throw new TranslationException(
						"Expected a numeral parameter but got " + parameter);
			}
			return ((Numeral) parameter).getValue().longValue();
		}

		@Override
		public Class<Long> translatesTo() {
			return Long.class;
		}
	}

	/**
	 * Translates {@link Numeral} into a {@link Short}.
	 */
	private static class ShortTranslator implements Parameter2Java<Short> {
		@Override
		public Short translate(Parameter parameter) throws TranslationException {
			if (!(parameter instanceof Numeral)) {
				throw new TranslationException(
						"Expected a numeral parameter but got " + parameter);
			}
			return ((Numeral) parameter).getValue().shortValue();
		}

		@Override
		public Class<Short> translatesTo() {
			return Short.class;
		}
	}

	/**
	 * Translates {@link Numeral} into a {@link Double}.
	 */
	private static class DoubleTranslator implements Parameter2Java<Double> {
		@Override
		public Double translate(Parameter parameter)
				throws TranslationException {
			if (!(parameter instanceof Numeral)) {
				throw new TranslationException(
						"Expected a numeral parameter but got " + parameter);
			}
			return ((Numeral) parameter).getValue().doubleValue();
		}

		@Override
		public Class<Double> translatesTo() {
			return Double.class;
		}
	}

	/**
	 * Translates {@link Numeral} into a {@link Float}.
	 */
	private static class FloatTranslator implements Parameter2Java<Float> {
		@Override
		public Float translate(Parameter parameter) throws TranslationException {
			if (!(parameter instanceof Numeral)) {
				throw new TranslationException(
						"Expected a numeral parameter but got " + parameter);
			}
			return ((Numeral) parameter).getValue().floatValue();
		}

		@Override
		public Class<Float> translatesTo() {
			return Float.class;
		}
	}
}
