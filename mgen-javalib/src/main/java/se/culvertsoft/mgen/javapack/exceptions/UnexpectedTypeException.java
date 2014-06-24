package se.culvertsoft.mgen.javapack.exceptions;

public class UnexpectedTypeException extends SerializationException {

	private static final long serialVersionUID = 1L;

	public UnexpectedTypeException(final String reason) {
		super(reason);
	}

	public UnexpectedTypeException(final Throwable cause) {
		super(cause);
	}

	public UnexpectedTypeException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
