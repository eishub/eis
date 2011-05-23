package apleis.repositoryagents.ita;

import java.util.Collection;
import java.util.LinkedList;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Percept;
import apleis.repositoryagents.Agent;

public class CleanerAgent extends Agent {

	LinkedList<Action> plan = new LinkedList<Action>();
	
	public CleanerAgent(String name, String team) {
		super(name, team);
	}

	@Override
	public void handlePercept(Percept p) {

	}

	@Override
	public Action step() {
		
		// perform first action of plan
		if ( plan.isEmpty() == false ) {
			return plan.removeFirst();
		}
		
		Collection<Percept> percepts = getAllPercepts();
		println("I see: " + percepts);
		
		// if there is dust perform clean plan
		if ( percepts.contains( new Percept("square",new Identifier("here"),new Identifier("dust")) ) ) {
			plan.add( new Action("light",new Identifier("on")) );
			plan.add( new Action("clean") );
			plan.add( new Action("light",new Identifier("off")) );
			return null;
		}
		
		int rule = (int) (Math.random() * 100000 % 3);
		if ( rule == 0 && !percepts.contains( new Percept("square",new Identifier("forward"),new Identifier("obstacle")) ) ) {
			return new Action("move",new Identifier("forward"));
		}
			
		if ( rule == 1 && !percepts.contains( new Percept("square",new Identifier("left"),new Identifier("obstacle")) ) ) {
			return new Action("move",new Identifier("left"));
		}

		if ( rule == 2 && !percepts.contains( new Percept("square",new Identifier("right"),new Identifier("obstacle")) ) ) {
			return new Action("move",new Identifier("right"));
		}

		if ( percepts.contains( new Percept("square",new Identifier("right"),new Identifier("obstacle")) ) ) {
			if ( percepts.contains( new Percept("square",new Identifier("left"),new Identifier("obstacle")) ) ) {
				if ( percepts.contains( new Percept("square",new Identifier("forward"),new Identifier("obstacle")) ) ) {
					return new Action("move",new Identifier("back"));
				}
			}
		}
		
		return null;

	}

}
