package se.culvertsoft.mgen.javapack.exceptions;

/**
 * Signals that unexpected data was read when reading data streams,
 * for example an array length was expected but a negative number was read.
 */
public class StreamCorruptedException extends SerializationException {

	private static final long serialVersionUID = 1L;

	public StreamCorruptedException() {
		super();
	}
	
	public StreamCorruptedException(final String reason) {
		super(reason);
	}

	public StreamCorruptedException(final Throwable cause) {
		super(cause);
	}

	public StreamCorruptedException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
