package apleis.repositoryagents.domestic;

import java.util.Collection;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Percept;
import apleis.repositoryagents.Agent;

public class OwnerAgent extends Agent {

	public OwnerAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Action step() {

		Collection<Percept> percepts = getAllPercepts();
		
		for ( Percept p : percepts ) {
			if ( p.toProlog().equals("has(owner,beer)") ) {
				println("I have a beer and drink it");
				return new Action("sip",new Identifier("beer"));
			}
		}
		
		return null;

	}

}
