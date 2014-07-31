package eis.eis2java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.iilang.Action;

/**
 * Annotation for methods that perform an action. The method must be public and
 * is allowed to take any arguments for which a {@link Parameter2Java}
 * translator has been registered. The method may also return a value of any
 * type for which a {@link Java2Parameter} translator has been registered.
 * 
 * @author Lennard de Rijk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsAction {
	/** The name of the action when specified as an {@link Action}. */
	String name();
}
