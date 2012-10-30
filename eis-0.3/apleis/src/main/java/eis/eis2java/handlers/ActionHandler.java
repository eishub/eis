package eis.eis2java.handlers;

import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Percept;

public abstract class ActionHandler {

	public abstract boolean isSupportedByEntity(Action action);

	public abstract Percept performAction(Action action) throws ActException;

}