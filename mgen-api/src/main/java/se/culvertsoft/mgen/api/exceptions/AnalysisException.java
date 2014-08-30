package se.culvertsoft.mgen.api.exceptions;

/**
 * Exception representing an error that occurred during IDL analysis in the MGen
 * compiler.
 */
public class AnalysisException extends MGenException {

	private static final long serialVersionUID = 1L;

	public AnalysisException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

	public AnalysisException(final String reason) {
		super(reason);
	}

	public AnalysisException(final Throwable cause) {
		super(cause);
	}

	public AnalysisException() {
		super();
	}

}
