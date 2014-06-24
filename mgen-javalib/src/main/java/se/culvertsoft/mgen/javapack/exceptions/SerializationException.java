package se.culvertsoft.mgen.javapack.exceptions;

import se.culvertsoft.mgen.api.exceptions.MGenException;

public class SerializationException extends MGenException {

	private static final long serialVersionUID = 1L;

	public SerializationException() {
		super();
	}

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
