package eis.eis2java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import eis.iilang.Percept;

import eis.eis2java.translation.Java2Parameter;

/**
 * Annotation for methods that generate a percept. The method must not take any
 * parameters but is allowed to return any value for which a
 * {@link Java2Parameter} translator has been registered.
 * 
 * @author Lennard de Rijk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsPercept {
  /** The name of the {@link Percept} when generated */
  String name();

  /**
   * If defined as true the return value of the annotated function must be of
   * type {@link Collection}. The return value will be used to generate multiple
   * percepts with the same name.
   */
  boolean multiplePercepts() default false;
}
