package apleis.repositoryagents.elevator;

import java.util.Collection;
import java.util.LinkedList;

import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import apleis.repositoryagents.Agent;

public class ElevatorAgent extends Agent {

	int floors;
	
	public ElevatorAgent(String name, String team) {
		super(name, team);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handlePercept(Percept p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Action step() {

		Collection<Percept> percepts = (LinkedList<Percept>) getAllPercepts();
		
		int floor = 0;
		for ( Percept p : percepts ) {
			
			if ( p.getName().equals("atFloor") ) {
				println("I am at floor " + p.getParameters().get(0));
				floor = ((Numeral)p.getParameters().get(0)).getValue().intValue();
			}
			else if ( p.getName().equals("doorState") ) {
				println("the door is " + p.getParameters().get(0));
			}
			else if ( p.getName().equals("carPosition") ) {
				println("I am at position " + p.getParameters().get(0));
			}
			else if ( p.getName().equals("capacity") ) {
				println("my capacity is " + p.getParameters().get(0));
			}
			else if ( p.getName().equals("people") ) {
				println("I carry " + p.getParameters().get(0) + " people");
			}
			else if ( p.getName().equals("fButtonOn") ) {
				println("a button was pressed at " + p.getParameters().get(0) + ", direction is " + p.getParameters().get(1));
			}
			else if ( p.getName().equals("floorCount") ) {
				println("there are " + p.getParameters().get(0) + " floors");
				floors = ((Numeral)p.getParameters().get(0)).getValue().intValue();
			}
			else if ( p.getName().equals("eButtonOn") ) {
				println("button to floor " + p.getParameters().get(0) + " pressed");
				floors = ((Numeral)p.getParameters().get(0)).getValue().intValue();
			}
			else {
				assert false : p;
			}
		}
		
		if ( floors == 0 ) return null;
		
		int newFloor = floor + 1;
		if (newFloor > floors ) newFloor = 1;
		String direction;
		if ( newFloor > floor ) direction = "up";
		else direction = "down";
		
		println("I will to to floor " + newFloor + ", direction is " + direction);
		
		return new Action("goto",new Numeral(newFloor),new Identifier(direction));
		
	}

}
