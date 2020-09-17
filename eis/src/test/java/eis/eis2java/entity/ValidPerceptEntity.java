package eis.eis2java.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eis.eis2java.annotation.AsPercept;
import eis.eis2java.translation.Filter.Type;
import eis.eis2java.util.AllPerceptsModule;
import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;

/**
 * Entity for testing. It has the normal @AsPercept annotated functions, but
 * also for each @AsPercept(X) an associated getX() function that returns what
 * the percept SHOULD return after calling.
 */
public class ValidPerceptEntity implements AllPerceptsProvider {
	private final AllPerceptsModule percepts;

	public ValidPerceptEntity() throws EntityException {
		this.percepts = new AllPerceptsModule(this);
	}

	@AsPercept(filter = Type.ALWAYS, multiplePercepts = false, name = "always")
	public String always() {
		return "A";
	}

	public Percept getAlways() {
		return new Percept("always", new Identifier("A"));
	}

	@AsPercept(filter = Type.ONCE, multiplePercepts = false, name = "once")
	public String once() {
		return "A";
	}

	public Percept getOnce() {
		return new Percept("once", new Identifier("A"));
	}

	boolean onChange = false;

	@AsPercept(filter = Type.ON_CHANGE, multiplePercepts = false, name = "onChange")
	public String onChange() {
		this.onChange = !this.onChange;
		return this.onChange ? "A" : "B";
	}

	public Percept getOnChange() {
		return new Percept("onChange", new Identifier("A"));
	}

	public Percept getOnChanged() {
		return new Percept("onChange", new Identifier("B"));
	}

	boolean onChangeNegation = false;

	@AsPercept(filter = Type.ALWAYS, multiplePercepts = true, name = "multipleAlways")
	public List<String> multipleAlways() {
		final List<String> list = new ArrayList<>(2);
		list.add("One");
		list.add("Two");
		return list;
	}

	public List<Percept> getMultipleAlways() {
		return Arrays.asList(new Percept("multipleAlways", new Identifier("One")),
				new Percept("multipleAlways", new Identifier("Two")));
	}

	int count = 0;

	@AsPercept(filter = Type.ON_CHANGE, multiplePercepts = true, name = "multipleOnChange")
	public List<String> multipleOnChange() {
		this.count++;
		final List<String> list = new ArrayList<>(this.count);

		if (this.count >= 1) {
			list.add("One");
		}
		if (this.count >= 2) {
			list.add("Two");
		}
		if (this.count >= 3) {
			list.add("Three");
		}

		return list;
	}

	public List<Percept> getMultipleOnChange() {
		final List<Percept> list = new ArrayList<>();

		if (this.count >= 1) {
			list.add(new Percept("multipleOnChange", new Identifier("One")));
		}
		if (this.count >= 2) {
			list.add(new Percept("multipleOnChange", new Identifier("Two")));
		}
		if (this.count >= 3) {
			list.add(new Percept("multipleOnChange", new Identifier("Three")));
		}

		return list;
	}

	/**
	 * @return a single multi-argumented percept
	 */
	@AsPercept(name = "multiArgs", multipleArguments = true, filter = Type.ALWAYS)
	public List<Integer> multiArgs() {
		final List<Integer> list = new ArrayList<>(2);
		list.add(1);
		list.add(2);
		return list;
	}

	/**
	 * @return a simple multi-arg percept
	 */
	public Percept getMultiArgs() {
		return new Percept("multiArgs", new Numeral(1), new Numeral(2));
	}

	/**
	 * @return combined multiple Arguments and MultiplePercepts
	 */
	@AsPercept(name = "multipleMultiArgs", multipleArguments = true, multiplePercepts = true, filter = Type.ALWAYS)
	public List<List<Integer>> multipleMultiArgs() {
		final List<List<Integer>> list = new ArrayList<>();
		final List<Integer> list1 = new ArrayList<>();
		list1.add(1);
		list1.add(2);
		final List<Integer> list2 = new ArrayList<>();
		list2.add(3);
		list2.add(4);
		list.add(list1);
		list.add(list2);
		return list;
	}

	public List<Percept> getMultipleMultiArgs() {
		return Arrays.asList(new Percept("multipleMultiArgs", new Numeral(1), new Numeral(2)),
				new Percept("multipleMultiArgs", new Numeral(3), new Numeral(4)));

	}

	public List<Percept> getMultipleOnChanged() {
		return Collections.emptyList();
	}

	@Override
	public Map<Method, Object> getPercepts() throws PerceiveException {
		this.percepts.updatePercepts();
		return this.percepts.getPercepts();
	}
}
