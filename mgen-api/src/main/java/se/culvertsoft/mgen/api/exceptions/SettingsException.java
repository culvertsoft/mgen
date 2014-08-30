package se.culvertsoft.mgen.api.exceptions;

/**
 * Exception representing an error that occurred during settings analysis in the
 * MGen compiler.
 */
public class SettingsException extends MGenException {

	private static final long serialVersionUID = 1L;

	public SettingsException(final String reason) {
		super(reason);
	}

	public SettingsException(final Throwable cause) {
		super(cause);
	}

	public SettingsException(final String reason, final Throwable cause) {
		super(reason, cause);
	}

}
