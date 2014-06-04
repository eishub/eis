package apleis.repositoryagents.domestic;

import java.util.Collection;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import apleis.repositoryagents.Agent;
import apltk.interpreter.data.LogicBelief;
import apltk.interpreter.data.Message;

public class SupermarketAgent extends Agent {

	public SupermarketAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Action step() {

		Collection<Message> messages = getMessages();

		for ( Message msg : messages ) {
			if ( msg.value.value.startsWith("fridgeEmpty") )
				return new Action("deliver", new Identifier("beer"), new Numeral(2));	
		}
		
		return null;
	
	}

}
