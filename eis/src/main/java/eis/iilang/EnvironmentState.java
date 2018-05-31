package eis.iilang;

import eis.EnvironmentInterfaceStandard;

public enum EnvironmentState {
	/**
	 * The environment is not ready for use.
	 */
	INITIALIZING,
	/**
	 * The environment is fully operational and running
	 */
	RUNNING,
	/**
	 * The environment is operational but currently not running.
	 * {@link EnvironmentInterfaceStandard#getPercepts(String, String...)}
	 * should be working but
	 * {@link EnvironmentInterfaceStandard#performAction(String, Action, String...)}
	 * may fail.
	 */
	PAUSED,
	/**
	 * The environment was taken down after running.
	 */
	KILLED;
}
