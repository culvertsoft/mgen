package se.culvertsoft.mgen.api.exceptions;

/**
 * Base class for all MGen exceptions, both exceptions in the MGen compiler, but
 * also exceptions from the MGen java runtime library.
 */
public class MGenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MGenException() {
		super();
	}

	public MGenException(final String reason) {
		super(reason);
	}

	public MGenException(final Throwable cause) {
		super(cause);
	}

	public MGenException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
