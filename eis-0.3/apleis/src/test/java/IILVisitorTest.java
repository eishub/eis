import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

import eis.iilang.Action;
import eis.iilang.DataContainer;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.ParameterList;
import eis.iilang.Percept;
import eis.iilang.PrologVisitor;
import eis.iilang.XMLVisitor;

public class IILVisitorTest {

	@Test
	public void testXML() {
		
		DataContainer cont = null;
		Vector<DataContainer> dcs = new Vector<DataContainer>();
		
		// moving to (2,3)
		cont = new Action(
						"moveTo", 
						new Numeral(2), 
						new Numeral(3)
						);
		dcs.add(cont);

		// following a path at a given speed
		cont = new Action(
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
		dcs.add(cont);
		
		// perceiving a red rubber-ball
		cont = new Percept(
						"sensors", 
						new ParameterList(
								new Function("red", new Identifier("ball")),
								new Function("rubber", new Identifier("ball"))
						)
					);
		dcs.add(cont);
		
		// see some entities
		cont = new Percept(
				"entities", 
				new ParameterList(
						new Identifier("entity1"),
						new Identifier("entity2"),
						new Identifier("entity3")
				)
		);
		dcs.add(cont);
		
		// see an empty list
		cont = new Percept(
				"entities", 
				new ParameterList(
				)
		);
		dcs.add(cont);

		// test
		for ( DataContainer dc : dcs ) {
			System.out.println("TEST");
			String str1,str2;
			str1 = dc.toXML();
			str2 = XMLVisitor.staticVisit(dc);
			System.out.println("toXML:\n" + str1); 
			System.out.println("visit:\n" + str2); 
			str1 = str1.replace(" ", "").replace("\n","").replace("\t","");
			str2 = str2.replace(" ", "").replace("\n","").replace("\t","");
			boolean eq = str1.equals(str2);
			if ( !eq ) System.out.println("NOT EQUAL");
			else System.out.println("EQUAL");
			assertTrue(eq);
			System.out.println("\n\n");
		}
		
	}

}
