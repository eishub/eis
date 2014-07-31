package eis.eis2java.entity;

import eis.eis2java.annotation.AsAction;

public class ValidActionEntity {

	int x = 0;

	@AsAction(name = "setX")
	public void setX(int x) {
		this.x = x;
	}

	@AsAction(name = "getX")
	public int getX() {
		return x;
	}

}
