package se.culvertsoft.mgen.javapack.exceptions;

/**
 * An exception thrown by MGen readers and writers if reading or writing objects
 * that have missing required fields.
 */
public class MissingRequiredFieldsException extends SerializationException {

	private static final long serialVersionUID = 1L;

	public MissingRequiredFieldsException(final String reason) {
		super(reason);
	}

	public MissingRequiredFieldsException(final Throwable cause) {
		super(cause);
	}

	public MissingRequiredFieldsException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
