package se.culvertsoft.mgen.javapack.serialization;

/**
 * See DefaultMGenReaderErrorHandlerSettings for default values.
 */
public class ReaderSettings extends SerializationSettings {

	public static final ReaderSettings DEFAULT = new ReaderSettings(
			SerializationSettings.DEFAULT,
			DerivedTypeAvailableResponse.IGNORE,
			TooManyFieldsResponse.SKIP,
			UnknownTypeResponse.SKIP_AND_WARN,
			UnknownFieldResponse.SKIP,
			UnexpectedTypeResponse.THROW);

	public static enum DerivedTypeAvailableResponse {
		IGNORE, // DEFAULT: Backw and Forw compatibility
		WARN,
		CAPTURE,
		THROW,
	}

	public static enum TooManyFieldsResponse {
		SKIP, // DEFAULT: Backw and Forw compatibility
		SKIP_AND_WARN,
		SKIP_AND_CAPTURE,
		THROW,
	}

	public static enum UnknownTypeResponse {
		SKIP, SKIP_AND_WARN, // DEFAULT: Backw/Forw comp, but new type available
		SKIP_AND_CAPTURE,
		THROW,
	}

	public static enum UnknownFieldResponse {
		SKIP, // DEFAULT: Backw and Forw compatibility
		SKIP_AND_WARN,
		SKIP_AND_CAPTURE,
		THROW,
	}

	public static enum UnexpectedTypeResponse {
		NULL_AND_IGNORE, // NOT RECOMMENDED: Incompatible definition
		NULL_AND_WARN,
		NULL_AND_CAPTURE,
		THROW, // DEFAULT: New message type is
				// incompatible
	}

	public ReaderSettings(
			final TypeIdType typeIdType,
			final FieldIdType fieldIdType,
			final DerivedTypeAvailableResponse moreDerivedTypeAvailableResponse,
			final TooManyFieldsResponse tooManyFieldsResponse,
			final UnknownTypeResponse unknownTypeResponse,
			final UnknownFieldResponse unknownFieldResponse,
			final UnexpectedTypeResponse unexpectedTypeResponse) {
		super(typeIdType, fieldIdType);
		m_moreDerivedTypeAvailableResponse = moreDerivedTypeAvailableResponse;
		m_tooManyFieldsResponse = tooManyFieldsResponse;
		m_unknownTypeResponse = unknownTypeResponse;
		m_unknownFieldResponse = unknownFieldResponse;
		m_unexpectedTypeResponse = unexpectedTypeResponse;
	}

	public ReaderSettings(
			final SerializationSettings serializationSettings,
			final DerivedTypeAvailableResponse moreDerivedTypeAvailableResponse,
			final TooManyFieldsResponse tooManyFieldsResponse,
			final UnknownTypeResponse unknownTypeResponse,
			final UnknownFieldResponse unknownFieldResponse,
			final UnexpectedTypeResponse unexpectedTypeResponse) {
		super(serializationSettings.typeIdType(), serializationSettings
				.fieldIdType());
		m_moreDerivedTypeAvailableResponse = moreDerivedTypeAvailableResponse;
		m_tooManyFieldsResponse = tooManyFieldsResponse;
		m_unknownTypeResponse = unknownTypeResponse;
		m_unknownFieldResponse = unknownFieldResponse;
		m_unexpectedTypeResponse = unexpectedTypeResponse;
	}

	public DerivedTypeAvailableResponse moreDerivedTypeAvailableResponse() {
		return m_moreDerivedTypeAvailableResponse;
	}

	public TooManyFieldsResponse tooManyFieldsResponse() {
		return m_tooManyFieldsResponse;
	}

	public UnknownTypeResponse unknownTypeResponse() {
		return m_unknownTypeResponse;
	}

	public UnknownFieldResponse unknownFieldResponse() {
		return m_unknownFieldResponse;
	}

	public UnexpectedTypeResponse unexpectedTypeResponse() {
		return m_unexpectedTypeResponse;
	}

	@Override
	public String toString() {
		return "MGenReaderSettings [moreDerivedTypeAvailableResponse()="
				+ moreDerivedTypeAvailableResponse()
				+ ", tooManyFieldsResponse()=" + tooManyFieldsResponse()
				+ ", unknownTypeResponse()=" + unknownTypeResponse()
				+ ", unknownFieldResponse()=" + unknownFieldResponse()
				+ ", unexpectedTypeResponse()=" + unexpectedTypeResponse()
				+ ", typeIdType()=" + typeIdType() + ", fieldIdType()="
				+ fieldIdType() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((m_moreDerivedTypeAvailableResponse == null) ? 0
						: m_moreDerivedTypeAvailableResponse.hashCode());
		result = prime
				* result
				+ ((m_tooManyFieldsResponse == null) ? 0
						: m_tooManyFieldsResponse.hashCode());
		result = prime
				* result
				+ ((m_unexpectedTypeResponse == null) ? 0
						: m_unexpectedTypeResponse.hashCode());
		result = prime
				* result
				+ ((m_unknownFieldResponse == null) ? 0
						: m_unknownFieldResponse.hashCode());
		result = prime
				* result
				+ ((m_unknownTypeResponse == null) ? 0 : m_unknownTypeResponse
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReaderSettings other = (ReaderSettings) obj;
		if (m_moreDerivedTypeAvailableResponse != other.m_moreDerivedTypeAvailableResponse)
			return false;
		if (m_tooManyFieldsResponse != other.m_tooManyFieldsResponse)
			return false;
		if (m_unexpectedTypeResponse != other.m_unexpectedTypeResponse)
			return false;
		if (m_unknownFieldResponse != other.m_unknownFieldResponse)
			return false;
		if (m_unknownTypeResponse != other.m_unknownTypeResponse)
			return false;
		return true;
	}

	private final DerivedTypeAvailableResponse m_moreDerivedTypeAvailableResponse;
	private final TooManyFieldsResponse m_tooManyFieldsResponse;
	private final UnknownTypeResponse m_unknownTypeResponse;
	private final UnknownFieldResponse m_unknownFieldResponse;
	private final UnexpectedTypeResponse m_unexpectedTypeResponse;

}
