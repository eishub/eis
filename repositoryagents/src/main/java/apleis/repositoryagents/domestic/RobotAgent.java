package apleis.repositoryagents.domestic;

import java.util.Collection;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import apleis.repositoryagents.Agent;
import apltk.interpreter.data.Belief;
import apltk.interpreter.data.LogicBelief;

public class RobotAgent extends Agent {

	private String state = "none";
	
	public RobotAgent(String name, String team) {
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
		
		this.println("I see: " + percepts);

		boolean ownerHasBeer = false;
		boolean atFridge = false;
		boolean atOwner = false;
		int inStock = -1;
		for ( Percept p : percepts ) {
			if ( p.toProlog().equals("has(owner,beer)") ) {
				ownerHasBeer = true;
			}
			else if ( p.toProlog().equals("at(robot,fridge)") ) {
				atFridge = true;
			}
			else if ( p.toProlog().equals("at(robot,owner)") ) {
				atOwner = true;
			}
			else if ( p.getName().equals("stock") ) {
				inStock = ((Numeral)p.getParameters().get(1)).getValue().intValue();
			}
			else {
				println("unhandled percept " + p.toProlog() );			
				assert false;
			}
		}

		if ( state.equals("none") ) {
			if ( ownerHasBeer == false )
				state = "getBeer";
		}
		else if ( state.equals("getBeer") ) {
			if ( atFridge == false ) {
				println("my owner has no beer. going to the fridge");
				return new Action("move_towards",new Identifier("fridge"));
			}
			if ( atFridge == true && inStock == -1 ) {
				println("fridge is closed. I open it.");
				return new Action("open",new Identifier("fridge"));
			}
			if ( atFridge == true && inStock == 0 ) {
				this.sendMessage(new LogicBelief("fridgeEmpty"),"supermarket");
				state = "waitForBeer";
				return null;
			}
			if ( atFridge == true && inStock > 0 ) {
				println("fridge is open. getting a beer.");
				state = "giveBeer";
				return new Action("get",new Identifier("beer"));
			}
		}
		else if ( state.equals("giveBeer") ) {
			if ( atOwner == false ) {
				println("going to the fridge owner");
				return new Action("move_towards",new Identifier("owner"));
			}
			if ( atOwner == true ) {
				println("giving beer");
				state = "none";
				return new Action("hand_in",new Identifier("beer"));
			}
			
		}
		else if ( state.equals("waitForBeer") ) {
			
			if ( inStock > 0 ) {
				state = "getBeer";
			}
			
			return null;
			
		}
		


		return null;
		
	}

}
