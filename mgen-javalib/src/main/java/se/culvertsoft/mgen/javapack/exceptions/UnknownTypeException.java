package se.culvertsoft.mgen.javapack.exceptions;

/**
 * Signals that an unknown type was read when reading a field of an object. This
 * means that the field type of a class has changed, and therefore the data
 * model is now incompatible. It is recommended not to change the field types of
 * classes. It's better to instead park the old field and create a new field
 * with the new type.
 */
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
