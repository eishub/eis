package eis.eis2java.handlers;

import eis.eis2java.entity.ValidActionEntity;
import eis.exceptions.EntityException;

public class DefaultActionHandlerTest extends ActionHandlerTest {

	@Override
	public ActionHandler getHandler(Object entity) throws EntityException {
		return new DefaultActionHandler(entity);
	}

	@Override
	public ValidActionEntity getValdidEntity() {
		return new ValidActionEntity();
	}
}
