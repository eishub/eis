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
import eis.iilang.TruthValue;
import eis.iilang.XMLVisitor;

public class IILVisitorTest {

	/**
	 * Tests both the XML- and Prolog-Visitor.
	 */
	@Test
	public void testXML() {
		
		DataContainer cont = null;
		Vector<DataContainer> dcs = new Vector<DataContainer>();
		
		// testing values
		cont = new Percept(
						"values", 
						new Identifier("id"),
						new Numeral(1), 
						new TruthValue(true)
						);
		dcs.add(cont);

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

		// test to XML
		for ( DataContainer dc : dcs ) {
			String str1,str2;
			str1 = dc.toXML();
			str2 = XMLVisitor.staticVisit(dc);
			String str1Clean = str1.replace(" ", "").replace("\n","").replace("\t","");
			String str2Clean = str2.replace(" ", "").replace("\n","").replace("\t","");
			boolean eq = str1Clean.equals(str2Clean);
			if ( !eq ) {
				System.out.println("toXML:\n" + str1); 
				System.out.println("XMLVisitor:\n" + str2); 
				fail("NOT EQUAL");
			}
			assertTrue(eq);
		}
		
		// test to XML
		for ( DataContainer dc : dcs ) {
			String str1,str2;
			str1 = dc.toProlog();
			str2 = PrologVisitor.staticVisit(dc);
			String str1Clean = str1.replace(" ", "").replace("\n","").replace("\t","");
			String str2Clean = str2.replace(" ", "").replace("\n","").replace("\t","");
			boolean eq = str1Clean.equals(str2Clean);
			if ( !eq ) {
				System.out.println("toProlog:\n" + str1); 
				System.out.println("PrologVisitor:\n" + str2); 
				fail("NOT EQUAL");
			}
			assertTrue(eq);
		}
		
	}

}
