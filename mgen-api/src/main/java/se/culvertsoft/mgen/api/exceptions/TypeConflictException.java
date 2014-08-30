package se.culvertsoft.mgen.api.exceptions;

/**
 * Exception representing an error that occurred because two or more classes or
 * fields have conflicting IDs and/or names.
 */
public class TypeConflictException extends MGenException {

	private static final long serialVersionUID = 1L;

	public TypeConflictException(final String reason) {
		super(reason);
	}

	public TypeConflictException(final Throwable cause) {
		super(cause);
	}

	public TypeConflictException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
