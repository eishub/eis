package eis.eis2javahelloworld;

import java.util.Map;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.Action;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;

/**
 * Simple Hello World environment which launches a single entity upon
 * initialization.
 * 
 * @author Lennard de Rijk
 */
public class HelloWorldEnvironment extends AbstractEnvironment {

  private static final long serialVersionUID = 1L;

  @Override
  public void init(Map<String, Parameter> parameters) throws ManagementException {
    super.init(parameters);

    try {
      registerEntity("entity1", new Entity());
    } catch (EntityException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean isStateTransitionValid(EnvironmentState oldState, EnvironmentState newState) {
    return true;
  }

  @Override
  protected boolean isSupportedByEnvironment(Action action) {
    return true;
  }

  @Override
  protected boolean isSupportedByType(Action action, String type) {
    return true;
  }
}
