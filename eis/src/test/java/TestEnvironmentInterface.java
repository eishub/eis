import eis.EIDefaultImpl;
import eis.PerceptUpdate;
import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;

/**
 * A test environment implementing EIDefaultImpl
 */
@SuppressWarnings("serial")
public class TestEnvironmentInterface extends EIDefaultImpl {
	public TestEnvironmentInterface() throws ManagementException {
		setState(EnvironmentState.PAUSED);
	}

	@Override
	protected PerceptUpdate getPerceptsForEntity(final String entity) throws PerceiveException, NoEnvironmentException {
		return null;
	}

	@Override
	protected boolean isSupportedByEntity(final Action action, final String entity) {
		return false;
	}

	@Override
	protected boolean isSupportedByEnvironment(final Action action) {
		return false;
	}

	@Override
	protected boolean isSupportedByType(final Action action, final String type) {
		return false;
	}

	@Override
	protected void performEntityAction(final Action action, final String entity) throws ActException {
	}

	@Override
	public String requiredVersion() {
		return null;
	}

	public void doAddEntity() throws EntityException {
		addEntity("entityname", "entity");
	}
}
