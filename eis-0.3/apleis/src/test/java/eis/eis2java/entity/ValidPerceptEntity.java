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
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Percept;

public class ValidPerceptEntity implements AllPerceptsProvider {

	private AllPerceptsModule percepts;

	public ValidPerceptEntity() throws EntityException {
		percepts = new AllPerceptsModule(this);
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
		onChange = !onChange;
		return onChange ? "A" : "B";
	}

	public Percept getOnChange() {
		return new Percept("onChange", new Identifier("A"));
	}

	public Percept getOnChanged() {
		return new Percept("onChange", new Identifier("B"));
	}

	boolean onChangeNegation = false;

	@AsPercept(filter = Type.ON_CHANGE_NEG, multiplePercepts = false, name = "onChangeNegation")
	public String onChangeNegation() {
		onChangeNegation = !onChangeNegation;
		return onChangeNegation ? "A" : "B";
	}

	public List<Percept> getOnChangeNegation() {
		return Arrays.asList(new Percept[] { new Percept("onChangeNegation",
				new Identifier("A")) });
	}

	public List<Percept> getOnChangedNegation() {
		return Arrays.asList(new Percept[] {
				new Percept("not", new Function("onChangeNegation",
						new Identifier("A"))),
				new Percept("onChangeNegation", new Identifier("B")) });

	}

	@AsPercept(filter = Type.ALWAYS, multiplePercepts = true, name = "multipleAlways")
	public List<String> multipleAlways() {
		ArrayList<String> list = new ArrayList<String>(2);

		list.add("One");
		list.add("Two");
		return list;
	}

	public List<Percept> getMultipleAlways() {
		return Arrays.asList(new Percept[] {
				new Percept("multipleAlways", new Identifier("One")),
				new Percept("multipleAlways", new Identifier("Two")) });
	}

	int count = 0;

	@AsPercept(filter = Type.ON_CHANGE, multiplePercepts = true, name = "multipleOnChange")
	public List<String> multipleOnChange() {

		count++;
		ArrayList<String> list = new ArrayList<String>(count);

		if (count >= 1)
			list.add("One");
		if (count >= 2)
			list.add("Two");
		if (count >= 3)
			list.add("Three");

		return list;
	}

	public List<Percept> getMultipleOnChange() {

		ArrayList<Percept> list = new ArrayList<Percept>();

		if (count >= 1)
			list.add(new Percept("multipleOnChange", new Identifier("One")));
		if (count >= 2)
			list.add(new Percept("multipleOnChange", new Identifier("Two")));
		if (count >= 3)
			list.add(new Percept("multipleOnChange", new Identifier("Three")));

		return list;
	}

	public List<Percept> getMultipleOnChanged() {
		return Collections.emptyList();
	}

	public Map<Method, Object> getAllPercepts() throws PerceiveException {
		percepts.updatePercepts();
		return percepts.getAllPercepts();

	}

}
