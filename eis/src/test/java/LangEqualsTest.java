import static org.junit.Assert.*;

import org.junit.Test;

import eis.iilang.Action;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.ParameterList;
import eis.iilang.Percept;

public class LangEqualsTest {

	@Test
	public void test1() {

		Percept p1 = new Percept("blueBall");
		Percept p2 = new Percept("blueBall");
		Percept p3 = new Percept("redBall");
		
		assertTrue( p1.equals(p2));
		assertFalse( p2.equals(p3));
		assertFalse( p1.equals("blueBall"));
		
	}

	@Test
	public void test2() {

		Percept p1 = new Percept("ball", new Identifier("blue"));
		Percept p2 = new Percept("ball", new Identifier("blue"));
		Percept p3 = new Percept("ball", new Identifier("red"));
		
		assertTrue( p1.equals(p2));
		assertFalse( p2.equals(p3));
		assertFalse( p1.equals("blueBall"));
		
	}

	@Test
	public void test3() {

		Percept p1 = new Percept("ball", new Identifier("blue"), new Identifier("rubber"));
		Percept p2 = new Percept("ball", new Identifier("blue"), new Identifier("rubber"));
		Percept p3 = new Percept("ball", new Identifier("red"));
		
		assertTrue( p1.equals(p2));
		assertFalse( p2.equals(p3));
		assertFalse( p1.equals("blueBall"));
		
	}

	@Test
	public void test4() {

		Action p1 = new Action("kickBall");
		Action p2 = new Action("kickBall");
		Action p3 = new Action("throBall");
		
		assertTrue( p1.equals(p2));
		assertFalse( p2.equals(p3));
		assertFalse( p1.equals("throwBall"));
		
	}

	@Test
	public void test5() {

		Action p1 = new Action("kick", new Identifier("ball"));
		Action p2 = new Action("kick", new Identifier("ball"));
		Action p3 = new Action("throw", new Identifier("ball"));
		
		assertTrue( p1.equals(p2));
		assertFalse( p2.equals(p3));
		assertFalse( p1.equals("blueBall"));
		
	}

	@Test
	public void test6() {

		Function f1 = new Function("plus", new Function("minus", new Numeral(1), new Numeral(2)), new Numeral(3));
		Function f2 = new Function("plus", new Function("minus", new Numeral(1), new Numeral(2)), new Numeral(3));
		Function f3 = new Function("minus", new Function("plus", new Numeral(1), new Numeral(2)), new Numeral(3));
		
		assertTrue( f1.equals(f2));
		assertFalse( f2.equals(f3));
		assertFalse( f1.equals("blueBall"));

	}

	@Test
	public void test7() {

		Identifier id1 = new Identifier("blue");
		Identifier id2 = new Identifier("blue");
		Identifier id3 = new Identifier("red");
		
		assertTrue( id1.equals(id2));
		assertFalse( id2.equals(id3));
		assertFalse( id1.equals("blueBall"));

	}

	@Test
	public void test8() {
		
		ParameterList pl1 = new ParameterList(new Numeral(1) , new Numeral(2), new Numeral(3) );
		ParameterList pl2 = new ParameterList(new Numeral(1) , new Numeral(2), new Numeral(3) );
		ParameterList pl3 = new ParameterList(new Numeral(1) , new Numeral(3) );

		assertTrue( pl1.equals(pl2));
		assertFalse( pl2.equals(pl3));
		assertFalse( pl1.equals("blueBall"));

	}

}