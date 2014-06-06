package se.culvertsoft.mgen.api.exceptions;

public class SerializationException extends MGenException {

	private static final long serialVersionUID = 1L;

	public SerializationException(final String reason) {
		super(reason);
	}

	public SerializationException(final Throwable cause) {
		super(cause);
	}

	public SerializationException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
