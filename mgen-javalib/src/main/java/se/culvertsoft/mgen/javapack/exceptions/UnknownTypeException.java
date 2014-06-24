package se.culvertsoft.mgen.javapack.exceptions;

public class UnknownTypeException extends SerializationException {

	private static final long serialVersionUID = 1L;

	public UnknownTypeException(final String reason) {
		super(reason);
	}

	public UnknownTypeException(final Throwable cause) {
		super(cause);
	}

	public UnknownTypeException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
