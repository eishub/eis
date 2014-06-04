
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
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
		numeralB = new Numeral(NUM);
		identifierB = new Identifier(LITT);
		functionB = new Function(FUN, identifierB, numeralB);
		perceptB = new Percept(PERCEPT, functionB, identifierB, numeralB);
		actionB = new Action(ACTION, functionB, identifierB, numeralB);

		numeralA = new Numeral(NUM);
		identifierA = new Identifier(LITT);
		functionA = new Function(FUN, identifierA, numeralA);
		perceptA = new Percept(PERCEPT, functionA, identifierA, numeralA);
		actionA = new Action(ACTION, functionA, identifierA, numeralA);
	}

	@After
	public void tearDown() throws Exception {
		perceptB = null;
		actionB = null;
		functionB = null;
		numeralB = null;
		identifierB = null;

		perceptA = null;
		actionA = null;
		functionA = null;
		numeralA = null;
		identifierA = null;

	}

	@Test
	public void testEquals() {
		
		// If equals is correctly implemented the following should pass.
		assertEquals(identifierA, identifierB);
		assertEquals(numeralA, numeralB);
		assertEquals(functionA, functionB);
		assertEquals(actionA, actionB);
		assertEquals(perceptA, perceptB);

	}
	
	@Test
	public void testHashCode() {
		//If hash code is correctly implemented the following should pass
		assertEquals(identifierA.hashCode(),identifierB.hashCode());
		assertEquals(numeralA.hashCode(), numeralB.hashCode());
		assertEquals(functionA.hashCode(),functionB.hashCode());
		assertEquals(actionA.hashCode(),actionB.hashCode());
		assertEquals(perceptA.hashCode(),perceptB.hashCode());
	}
		
	
	@Test
	public void testSet(){
		
		Set<Percept> perceptSet = new HashSet<Percept>();
		//Add two percepts to set
		perceptSet.add(perceptA);
		perceptSet.add(perceptB);
		
		//Set should contain both percepts.
		Assert.assertTrue(perceptSet.contains(perceptA));
		Assert.assertTrue(perceptSet.contains(perceptB));
		
		//But because percepts are eqaul, size should be 1.
		Assert.assertTrue(perceptSet.size() == 1);
		
	}

}
