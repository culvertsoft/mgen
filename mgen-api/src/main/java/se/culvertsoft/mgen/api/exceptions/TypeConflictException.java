package se.culvertsoft.mgen.api.exceptions;

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
