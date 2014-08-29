package se.culvertsoft.mgen.javapack.exceptions;

/**
 * Signals that an unexpected type was read when reading data streams,
 * for example an integer field was to be read, but in fact string data read.
 */
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
