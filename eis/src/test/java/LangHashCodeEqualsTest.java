
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eis.iilang.Action;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;

public class LangHashCodeEqualsTest {
	private static final String ACTION = "action";
	private static final String PERCEPT = "percept";
	private static final String FUN = "fun";
	private static final int NUM = 42;
	private static final String LITT = "fourtyTwo";

	Percept perceptB;
	Action actionB;
	Function functionB;
	Numeral numeralB;
	Identifier identifierB;

	Percept perceptA;
	Action actionA;
	Function functionA;
	Numeral numeralA;
	Identifier identifierA;

	@Before
	public void setUp() throws Exception {
		this.numeralB = new Numeral(NUM);
		this.identifierB = new Identifier(LITT);
		this.functionB = new Function(FUN, this.identifierB, this.numeralB);
		this.perceptB = new Percept(PERCEPT, this.functionB, this.identifierB, this.numeralB);
		this.actionB = new Action(ACTION, this.functionB, this.identifierB, this.numeralB);

		this.numeralA = new Numeral(NUM);
		this.identifierA = new Identifier(LITT);
		this.functionA = new Function(FUN, this.identifierA, this.numeralA);
		this.perceptA = new Percept(PERCEPT, this.functionA, this.identifierA, this.numeralA);
		this.actionA = new Action(ACTION, this.functionA, this.identifierA, this.numeralA);
	}

	@After
	public void tearDown() throws Exception {
		this.perceptB = null;
		this.actionB = null;
		this.functionB = null;
		this.numeralB = null;
		this.identifierB = null;

		this.perceptA = null;
		this.actionA = null;
		this.functionA = null;
		this.numeralA = null;
		this.identifierA = null;
	}

	@Test
	public void testEquals() {
		assertEquals(this.identifierA, this.identifierB);
		assertEquals(this.numeralA, this.numeralB);
		assertEquals(this.functionA, this.functionB);
		assertEquals(this.actionA, this.actionB);
		assertEquals(this.perceptA, this.perceptB);
	}

	@Test
	public void testHashCode() {
		assertEquals(this.identifierA.hashCode(), this.identifierB.hashCode());
		assertEquals(this.numeralA.hashCode(), this.numeralB.hashCode());
		assertEquals(this.functionA.hashCode(), this.functionB.hashCode());
		assertEquals(this.actionA.hashCode(), this.actionB.hashCode());
		assertEquals(this.perceptA.hashCode(), this.perceptB.hashCode());
	}

	@Test
	public void testSet() {
		final Set<Percept> perceptSet = new HashSet<>(2);
		// Add two percepts to set
		perceptSet.add(this.perceptA);
		perceptSet.add(this.perceptB);

		// Set should contain both percepts.
		assertTrue(perceptSet.contains(this.perceptA));
		assertTrue(perceptSet.contains(this.perceptB));

		// But because percepts are eqaul, size should be 1.
		assertEquals(1, perceptSet.size());
	}
}
