package eis.eis2java.handlers;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eis.eis2java.util.AllPerceptsProvider;
import eis.exceptions.EntityException;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

public final class AllPerceptPerceptHandler extends AbstractPerceptHandler {

	/** Maps a Class to a map of percept method */
	private final AllPerceptsProvider allPercepProvider;

	public AllPerceptPerceptHandler(AllPerceptsProvider entity)
			throws EntityException {
		super(entity);
		this.allPercepProvider = entity;
	}

	@Override
	public final LinkedList<Percept> getAllPercepts() throws PerceiveException {

		LinkedList<Percept> percepts = new LinkedList<Percept>();
		Map<Method, Object> batchPerceptObjects;

		batchPerceptObjects = allPercepProvider.getAllPercepts();

		for (Entry<Method, Object> entry : batchPerceptObjects.entrySet()) {

			Method method = (Method) entry.getKey();
			Object perceptObject = entry.getValue();

			List<Object> perceptObjects = unpackPerceptObject(method,
					perceptObject);
			List<Percept> translatedPercepts = translatePercepts(method,
					perceptObjects);
			percepts.addAll(translatedPercepts);
		}

		return percepts;
	}

}
