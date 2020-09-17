import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
		final Percept p1 = new Percept("blueBall");
		final Percept p2 = new Percept("blueBall");
		final Percept p3 = new Percept("redBall");

		assertEquals(p1, p2);
		assertFalse(p2.equals(p3));
	}

	@Test
	public void test2() {
		final Percept p1 = new Percept("ball", new Identifier("blue"));
		final Percept p2 = new Percept("ball", new Identifier("blue"));
		final Percept p3 = new Percept("ball", new Identifier("red"));

		assertEquals(p1, p2);
		assertFalse(p2.equals(p3));
	}

	@Test
	public void test3() {
		final Percept p1 = new Percept("ball", new Identifier("blue"), new Identifier("rubber"));
		final Percept p2 = new Percept("ball", new Identifier("blue"), new Identifier("rubber"));
		final Percept p3 = new Percept("ball", new Identifier("red"));

		assertEquals(p1, p2);
		assertFalse(p2.equals(p3));
	}

	@Test
	public void test4() {
		final Action p1 = new Action("kickBall");
		final Action p2 = new Action("kickBall");
		final Action p3 = new Action("throBall");

		assertEquals(p1, p2);
		assertFalse(p2.equals(p3));
	}

	@Test
	public void test5() {
		final Action p1 = new Action("kick", new Identifier("ball"));
		final Action p2 = new Action("kick", new Identifier("ball"));
		final Action p3 = new Action("throw", new Identifier("ball"));

		assertEquals(p1, p2);
		assertFalse(p2.equals(p3));
	}

	@Test
	public void test6() {
		final Function f1 = new Function("plus", new Function("minus", new Numeral(1), new Numeral(2)), new Numeral(3));
		final Function f2 = new Function("plus", new Function("minus", new Numeral(1), new Numeral(2)), new Numeral(3));
		final Function f3 = new Function("minus", new Function("plus", new Numeral(1), new Numeral(2)), new Numeral(3));

		assertEquals(f1, f2);
		assertFalse(f2.equals(f3));
	}

	@Test
	public void test7() {
		final Identifier id1 = new Identifier("blue");
		final Identifier id2 = new Identifier("blue");
		final Identifier id3 = new Identifier("red");

		assertEquals(id1, id2);
		assertFalse(id2.equals(id3));
	}

	@Test
	public void test8() {
		final ParameterList pl1 = new ParameterList(new Numeral(1), new Numeral(2), new Numeral(3));
		final ParameterList pl2 = new ParameterList(new Numeral(1), new Numeral(2), new Numeral(3));
		final ParameterList pl3 = new ParameterList(new Numeral(1), new Numeral(3));

		assertEquals(pl1, pl2);
		assertFalse(pl2.equals(pl3));
	}
}