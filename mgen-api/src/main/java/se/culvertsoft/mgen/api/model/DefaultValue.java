package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;

/**
 * Represents a default value specified in the IDL for a field.
 */
public abstract class DefaultValue {

	/**
	 * The expected type of this default value
	 */
	public Type expectedType() {
		return m_expectedType;
	}

	/**
	 * The actual written default value string in the IDL
	 */
	public String writtenString() {
		return m_writtenString;
	}

	/**
	 * The actual written default value string in the IDL
	 */
	public boolean isLinked() {
		return m_expectedType != null;
	}

	protected DefaultValue(final Type typ, final String writtenString) {
		m_expectedType = typ;
		m_writtenString = writtenString;
	}

	private Type m_expectedType;
	private String m_writtenString;

	/**
	 * Intended to be used by the compiler during the type linkage phase to
	 * parse default value strings.
	 */
	public static DefaultValue parse(
			final Type expectedType,
			final String writtenString,
			final Module currentModule) {
		switch (expectedType.typeEnum()) {
		case ENUM:
			return new EnumDefaultValue((EnumType) expectedType, writtenString, currentModule);
		case BOOL:
			return new BoolDefaultValue((BoolType) expectedType, writtenString);
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
			return new NumericDefaultValue((PrimitiveType) expectedType, writtenString);
		case STRING:
			return new StringDefaultValue((StringType) expectedType, writtenString);
		case LIST:
		case ARRAY:
			return new ListOrArrayDefaultValue(
					(ListOrArrayType) expectedType,
					writtenString,
					currentModule);
		case MAP:
			return new MapDefaultValue((MapType) expectedType, writtenString, currentModule);
		case UNKNOWN:
		case CUSTOM:
			return new ObjectDefaultValue((CustomType) expectedType, writtenString, currentModule);
		default:
			throw new AnalysisException("Unexpected type enum: " + expectedType.typeEnum());
		}
	}

}
