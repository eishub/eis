package eis.eis2java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import eis.eis2java.environment.AbstractEnvironment;
import eis.eis2java.handlers.DefaultPerceptHandler;
import eis.eis2java.translation.Filter;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.util.AllPerceptsModule;
import eis.iilang.Percept;

/**
 * Annotation for methods that generate a percept. The method must not take any
 * parameters but is allowed to return any value for which a
 * {@link Java2Parameter} translator has been registered.
 * <p>
 * {@link @AsPercept} tags have four parameters:
 * <ol>
 * <li>name: the name of the percept to be returned
 * <li>multiplePercepts: true when the percept function returns a List of
 * (translatable) Objects, and each of the elements in the list should give
 * another percept. E.g. "on" returning [1,2] gives the percepts {on(1), on(2)}.
 * <li>multipleArguments: true when the percept function returns a List of
 * (translatable) objects, and each of the elements of the list should be put as
 * another argument in the percept. E.g., "on" returning [1,2] gives the percept
 * on(1,2).
 * <li>filter. One of Filter.Type.ALWAYS, ONCE, ONCHANGE or ONCHANGENEG.
 * <li>event. True when the percepts are event based and the results from all
 * calls to this method should be passed to the agent.
 * </ol>
 * 
 * <p>
 * It is possible to combine multiplePercepts and multipleArguments, in which
 * case the function should return a list of lists. The outside list is used for
 * the multiplePercepts, the inner for the multipleArguments. E.g., "on"
 * returning [[1,2],[3,4,5]] then results in the percepts {on(1,2),on(3,4,5)}.
 * </p>
 * 
 * The filter type determines the way percepts are filtered before sending out
 * through EIS:
 * <p>
 * <TABLE border='1'>
 * <thead>
 * <tr>
 * <th>filter name</th>
 * <th>full name</th>
 * <th>behaviour</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>ALWAYS</td>
 * <td>send always</td>
 * <td>send percepts every round</td>
 * </tr>
 * <tr>
 * <td>ONCE</td>
 * <td>send one time</td>
 * *
 * <td>send percepts only the first round</td>
 * </tr>
 * <tr>
 * <td>ON_CHANGE</td>
 * <td>send when changed</td>
 * <td>send percept(s), but only if any of the percepts changes</td>
 * </tr>
 * <tr>
 * <td>ON_CHANGE_NEG</td>
 * <td>send on change using 'not'</td>
 * <td>if percept p is new then send p; if percept p was removed and
 * (multiplePercepts=true or (multiple=false and p became null)) then send
 * not(p)</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * <em>
 * Important: </em>the {@link AbstractEnvironment} compares old and new percepts
 * based on the {@link Object} that is returned, not based on the translator
 * result. This is to avoid extra overhead of translation. This means that you
 * <em>must return</em> a static (non-changing) object as percept, and that that
 * percept object must have a functioning equals.
 * </p>
 * 
 * @author Lennard de Rijk
 * @author W.Pasman 27sep2011 added filter annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsPercept {
	/** The name of the {@link Percept} when generated */
	String name();

	/**
	 * If defined as true the return value of the annotated function must be of
	 * type {@link Collection}. The return value will be used to generate
	 * multiple percepts with the same name.
	 */
	boolean multiplePercepts() default false;

	/**
	 * if defined as true the return value of the annotated function must be of
	 * type {@link Collection}. The return value will be used to generate
	 * multiple arguments for the percept. If {@link #multiplePercepts()} is
	 * also true, {@link #multiplePercepts()} takes the outer {@link Collection}
	 * , and each element inside that collection is a {@link Collection} for
	 * multipleActions.
	 * 
	 * @return
	 */
	boolean multipleArguments() default false;

	Filter.Type filter() default Filter.Type.ALWAYS;

	/**
	 * If defined as true the method returns a list of events that have occurred
	 * since its last invocation. This flag is used by {@link AllPerceptsModule}
	 * to ensure that all events on each update are stored passed on to the
	 * environment as a batch. This setting has no effect on the
	 * {@link DefaultPerceptHandler}.
	 * 
	 */
	boolean event() default false;

}
