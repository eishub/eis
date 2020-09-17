import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.junit.Test;

import eis.iilang.IILElement;
import eis.iilang.PrologVisitor;
import eis.iilang.XMLVisitor;

public class IILVisitorTest {
	/**
	 * Test XML- Visitor.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testXML() {
		final Vector<IILElement> elements = IILExamples.correctExamples();

		// test to XML
		for (final IILElement e : elements) {
			String str1, str2;
			str1 = e.toXML();
			str2 = XMLVisitor.staticVisit(e);
			final String str1Clean = str1.replace(" ", "").replace("\n", "").replace("\t", "");
			final String str2Clean = str2.replace(" ", "").replace("\n", "").replace("\t", "");
			assertEquals(str1Clean, str2Clean);
		}
	}

	/**
	 * Test Prolog-Visitor.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testProlog() {
		final Vector<IILElement> elements = IILExamples.correctExamples();

		// test to Prolog
		for (final IILElement e : elements) {
			String str1, str2;
			str1 = e.toProlog();
			str2 = PrologVisitor.staticVisit(e);
			final String str1Clean = str1.replace(" ", "").replace("\n", "").replace("\t", "");
			final String str2Clean = str2.replace(" ", "").replace("\n", "").replace("\t", "");
			assertEquals(str1Clean, str2Clean);
		}
	}
}
