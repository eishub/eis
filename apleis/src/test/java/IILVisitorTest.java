import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Vector;

import eis.iilang.IILElement;
import eis.iilang.PrologVisitor;
import eis.iilang.XMLVisitor;

public class IILVisitorTest {

	/**
	 * Tests both the XML- and Prolog-Visitor.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testXML() {
		
		Vector<IILElement> elements = IILExamples.correctExamples();
		
		// test to XML
		for ( IILElement e : elements ) {
			String str1,str2;
			str1 = e.toXML();
			str2 = XMLVisitor.staticVisit(e);
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
		
		// test to Prolog
		for ( IILElement e : elements ) {
			String str1,str2;
			str1 = e.toProlog();
			str2 = PrologVisitor.staticVisit(e);
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
