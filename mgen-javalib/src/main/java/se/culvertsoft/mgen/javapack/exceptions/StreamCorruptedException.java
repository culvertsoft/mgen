package se.culvertsoft.mgen.javapack.exceptions;

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
