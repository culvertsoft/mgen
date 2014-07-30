package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;
import se.culvertsoft.mgen.api.model.BoolType;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.EnumType;
import se.culvertsoft.mgen.api.model.ListOrArrayType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.PrimitiveType;
import se.culvertsoft.mgen.api.model.StringType;
import se.culvertsoft.mgen.api.model.Type;

public class DefaultValueImpl implements DefaultValue {

	/**
	 * The expected type of this default value
	 */
	@Override
	public Type expectedType() {
		return m_expectedType;
	}

	/**
	 * The actual written default value string in the IDL
	 */
	@Override
	public String writtenString() {
		return m_writtenString;
	}

	/**
	 * The actual written default value string in the IDL
	 */
	@Override
	public boolean isLinked() {
		return m_expectedType != null;
	}

	protected DefaultValueImpl(final Type typ, final String writtenString) {
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

		if (writtenString == null || writtenString.trim().equals("null"))
			return null;

		switch (expectedType.typeEnum()) {
		case ENUM:
			return new EnumDefaultValueImpl((EnumType) expectedType, writtenString, currentModule);
		case BOOL:
			return new BoolDefaultValueImpl((BoolType) expectedType, writtenString);
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
			return new NumericDefaultValueImpl((PrimitiveType) expectedType, writtenString);
		case STRING:
			return new StringDefaultValueImpl((StringType) expectedType, writtenString);
		case LIST:
		case ARRAY:
			return new ListOrArrayDefaultValueImpl(
					(ListOrArrayType) expectedType,
					writtenString,
					currentModule);
		case MAP:
			return new MapDefaultValueImpl((MapType) expectedType, writtenString, currentModule);
		case UNKNOWN:
		case CUSTOM:
			return new ObjectDefaultValueImpl(
					(CustomType) expectedType,
					writtenString,
					currentModule);
		default:
			throw new AnalysisException("Unexpected type enum: " + expectedType.typeEnum());
		}
	}

}
