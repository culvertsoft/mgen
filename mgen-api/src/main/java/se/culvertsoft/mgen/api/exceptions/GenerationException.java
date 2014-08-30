package se.culvertsoft.mgen.api.exceptions;

/**
 * Exception representing an error that occurred during code generation in the
 * MGen compiler.
 */
public class GenerationException extends MGenException {

	private static final long serialVersionUID = 1L;

	public GenerationException(final String reason) {
		super(reason);
	}

	public GenerationException(final Throwable cause) {
		super(cause);
	}

	public GenerationException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
