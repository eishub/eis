package apleis.repositoryagents.agents2011;

import java.util.Collection;
import java.util.LinkedList;

import apltk.interpreter.data.Belief;
import apltk.interpreter.data.LogicBelief;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.Percept;

/**
 * This is a utility-class for the Mars-scenario. It us useful for creating
 * actions and dealing with percepts.
 * @author tristanbehrens
 *
 */
public class Mars2011Util {

	private Mars2011Util() {
		throw new AssertionError("do not instantiate!");
	}
	
	/**
	 * Yields a valid goto-action.
	 * @param nodeName
	 * @return
	 */
	static Action gotoAction(String nodeName) {
		return new Action("goto",new Identifier(nodeName));
	}

	/**
	 * Yields a valid skip-action.
	 * @return
	 */
	static Action skipAction() {
		return new Action("skip");
	}

	/**
	 * Yields a valid probe-action.
	 * @return
	 */
	static Action probeAction() {
		return new Action("probe");
	}

	/**
	 * Yields a valid survey-action.
	 * @return
	 */
	static Action surveyAction() {
		return new Action("survey");
	}

	/**
	 * Yields a valid inspect-action.
	 * @return
	 */
	static Action inspectAction() {
		return new Action("inspect");
	}

	/**
	 * Yields a valid parry-action.
	 * @return
	 */
	static Action parryAction() {
		return new Action("parry");
	}
	
	/**
	 * Yields a valid attack action.
	 * @param entityName
	 * @return
	 */
	static Action attackAction(String entityName) {
		return new Action("attack",new Identifier(entityName));
	}

	/**
	 * Yields a valid buy action.
	 * @param item 
	 * @return
	 */
	static Action buyAction(String item) {
		return new Action("buy",new Identifier(item));
	}

	/**
	 * Yields a valid repair action.
	 * @param entity
	 * @return
	 */
	static Action repairAction(String entity) {
		return new Action("repair",new Identifier(entity));
	}
	
	/**
	 * Yields a valid recharge action.
	 * @param entity
	 * @return
	 */
	static Action rechargeAction() {
		return new Action("recharge");
	}
	
	/**
	 * Filters all given percepts with respect to a given filter.
	 * That is, a list of percepts is returned that have a given name.
	 * @param percepts
	 * @param filter
	 * @return
	 */
	static LinkedList<Percept> filterPercepts(Collection<Percept> percepts, String filter) {
		
		LinkedList<Percept> ret = new LinkedList<Percept>();
		
		for ( Percept p : percepts ) {
			if ( p.getName().equals(filter) )
				ret.add(p);
		}
		
		return ret;
		
	}
	
	/**
	 * Maps a percept to a belief.
	 * @param percept
	 * @return
	 */
	static LogicBelief perceptToBelief(Percept percept) {
		
		String predicate = percept.getName();
		
		LinkedList<String> parameters = new LinkedList<String>();
		for ( Parameter param : percept.getParameters() ) {
			parameters.add(param.toProlog());
		}
		
		return new LogicBelief(predicate,parameters);
		
	}
	
}
