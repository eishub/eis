import java.util.Vector;

import eis.iilang.*;

public class IILExamples {

	public static Vector<IILElement> correctExamples() {
	
		Vector<IILElement> ret = new Vector<IILElement>();
		IILElement element = null;
		
		// testing values
		element = new Percept(
						"values", 
						new Identifier("id"),
						new Numeral(1), 
						new TruthValue(true)
						);
		ret.add(element);

		// moving to (2,3)
		element = new Action(
						"moveTo", 
						new Numeral(2), 
						new Numeral(3)
						);
		ret.add(element);

		// following a path at a given speed
		element = new Action(
						"followPath", 
						new ParameterList( 
								new Function("pos", new Numeral(1), new Numeral(1)), 
								new Function("pos", new Numeral(2), new Numeral(1)), 
								new Function("pos", new Numeral(2), new Numeral(2)), 
								new Function("pos", new Numeral(3), new Numeral(2)), 
								new Function("pos", new Numeral(4), new Numeral(2)), 
								new Function("pos", new Numeral(4), new Numeral(3)) 
						), 
						new Function("speed", new Numeral(10.0))
				);
		ret.add(element);
		
		// perceiving a red rubber-ball
		element = new Percept(
						"sensors", 
						new ParameterList(
								new Function("red", new Identifier("ball")),
								new Function("rubber", new Identifier("ball"))
						)
					);
		ret.add(element);
		
		// see some entities
		element = new Percept(
				"entities", 
				new ParameterList(
						new Identifier("entity1"),
						new Identifier("entity2"),
						new Identifier("entity3")
				)
		);
		ret.add(element);
		
		// see an empty list
		element = new Percept(
				"entities", 
				new ParameterList(
				)
		);
		ret.add(element);

		return ret;

	}

	public static Vector<IILElement> wrongExamples() {
		
		Vector<IILElement> ret = new Vector<IILElement>();
		IILElement element = null;
		
		// testing values
		element = new Percept(
						"Values", 
						new Identifier("Id"),
						new Numeral(1), 
						new TruthValue(true)
						);
		ret.add(element);

		return ret;
		
	}

}
