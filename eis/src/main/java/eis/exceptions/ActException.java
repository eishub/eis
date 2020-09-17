package eis.exceptions;

/**
 * This action is thrown if an attempt to perform an action has failed. An
 * action could fail if the action is not supported by the environment, if it
 * has wrong parameters, or if it cannot be executed because of the state of the
 * environment.
 *
 * @author tristanbehrens
 *
 */
public class ActException extends EnvironmentInterfaceException {
	private static final long serialVersionUID = 9092788356656103070L;

	/** No type. */
	public static final int NOTSPECIFIC = 0;
	/** if the agent is not registered */
	public static final int NOTREGISTERED = 1;
	/** if the agent has no entities */
	public static final int NOENTITIES = 2;
	/** if there is a wrong entity */
	public static final int WRONGENTITY = 3;
	/** if the action is not supported by the type */
	public static final int NOTSUPPORTEDBYENVIRONMENT = 4;
	/** if the action is not supported by the type */
	public static final int NOTSUPPORTEDBYTYPE = 5;
	/** if the action is not supported by the type */
	public static final int NOTSUPPORTEDBYENTITY = 6;
	/** if the action cannot be executed */
	public static final int FAILURE = 7;

	/** the type */
	private int type = NOTSPECIFIC;

	/**
	 * @param string the error message
	 */
	public ActException(final String string) {
		super(string);
	}

	/**
	 * @param message the error message
	 * @param cause   the cause (possibly null)
	 */
	public ActException(final String message, final Throwable cause) {
		super(message);
		initCause(cause);
	}

	/**
	 * @param string the error message
	 * @param type   the type of error
	 */
	public ActException(final String string, final int type) {
		super(string);
		setType(type);
	}

	/**
	 * @param type the type of error
	 */
	public ActException(final int type) {
		super("");
		setType(type);
	}

	/**
	 * @param message the error message
	 * @param type    the type of error
	 */
	public ActException(final int type, final String message) {
		super(message);
		setType(type);
	}

	/**
	 *
	 * @param type    type of the error
	 * @param message the error message
	 * @param cause   the cause of the error
	 */
	public ActException(final int type, final String message, final Throwable cause) {
		super(message);
		setType(type);
		initCause(cause);
	}

	/**
	 * @return type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * @param type the new type of the error
	 */
	public void setType(int type) {
		if (type < 0 || type > 7) {
			assert false : "Type \"" + type + "\" not supported.";
			type = 0;
		}

		this.type = type;
	}

	@Override
	public String toString() {
		String ret = "";

		String strType = "";
		if (this.type == NOTSPECIFIC) {
			strType = "not specific";
		} else if (this.type == NOTREGISTERED) {
			strType = "not registered";
		} else if (this.type == NOENTITIES) {
			strType = "no entities";
		} else if (this.type == WRONGENTITY) {
			strType = "wrong entity";
		} else if (this.type == NOTSUPPORTEDBYENVIRONMENT) {
			strType = "not supported by environment";
		} else if (this.type == NOTSUPPORTEDBYTYPE) {
			strType = "not supported by type";
		} else if (this.type == NOTSUPPORTEDBYENTITY) {
			strType = "not supported by entity";
		} else if (this.type == FAILURE) {
			strType = "failure";
		}

		ret += "ActException type=\"" + strType + "\"";

		if (getMessage() != null) {
			ret += " message=\"" + getMessage() + "\"";
		}

		if (getCause() != null) {
			ret += " cause=\"" + getCause() + "\"";
		}

		return ret;
	}
}
