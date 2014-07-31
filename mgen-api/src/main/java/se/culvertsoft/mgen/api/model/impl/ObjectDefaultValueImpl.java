package se.culvertsoft.mgen.api.model.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.ObjectDefaultValue;

/**
 * Represents a default value for an object/CustomType field/type.
 */
public class ObjectDefaultValueImpl extends DefaultValueImpl implements ObjectDefaultValue {

	/**
	 * The expected type of this default value
	 */
	@Override
	public CustomType expectedType() {
		return (CustomType) super.expectedType();
	}

	/**
	 * The actual type of this default value, which is either the same as the
	 * expected type or a subtype of it
	 */
	@Override
	public CustomType actualType() {
		return m_actualType;
	}

	/**
	 * Returns if the type of this default value is defined in the same module
	 * as where it's now referenced.
	 */
	@Override
	public boolean isCurrentModule() {
		return m_isCurrentModule;
	}

	/**
	 * Returns if the default value is of a subtype
	 */
	@Override
	public boolean isDefaultTypeOverriden() {
		return expectedType() != actualType();
	}

	/**
	 * The overridden field default values of this default value
	 */
	@Override
	public Map<Field, DefaultValue> overriddenDefaultValues() {
		return m_overriddenDefaultValues;
	}

	@SuppressWarnings("unchecked")
	public ObjectDefaultValueImpl(
			final CustomType expectedType,
			final String writtenString,
			final Module currentModule) {
		super(expectedType, writtenString);
		m_overriddenDefaultValues = new LinkedHashMap<Field, DefaultValue>();
		try {

			final Object src = JSONValue.parseWithException(writtenString);
			if (src instanceof JSONObject) {

				final JSONObject jsonObject = (JSONObject) src;

				final String optActualTypeName = (String) jsonObject.get("__TYPE");

				if (optActualTypeName != null) {

					m_actualType = findType(optActualTypeName, currentModule);

					if (m_actualType == null) {
						throw new AnalysisException("Could not find specified default value type "
								+ optActualTypeName + " for expected type " + expectedType);
					}

					if (expectedType != m_actualType
							&& !m_actualType.superTypeHierarchy().contains(expectedType)) {
						throw new AnalysisException(
								"Specified default value type "
										+ optActualTypeName
										+ " does not qualify for defaultvalue. It is not the same type or a subtype of "
										+ expectedType);
					}
					jsonObject.remove("__TYPE");
				} else {
					m_actualType = expectedType;
				}

				m_isCurrentModule = m_actualType.module() == currentModule;

				for (final Map.Entry<String, Object> e : ((Map<String, Object>) src).entrySet()) {
					final String fieldName = e.getKey();

					final Field f = m_actualType.findField(fieldName);
					if (f == null) {
						throw new AnalysisException("Failed to set default value. No field named "
								+ fieldName + " was found on type " + m_actualType);
					}

					final DefaultValue value = DefaultValueImpl.parse(f.typ(), e
							.getValue()
							.toString(), currentModule);
					m_overriddenDefaultValues.put(f, value);
				}

			} else {
				throw new AnalysisException("Failed to parse default value '" + writtenString
						+ "' as a JSON object.");
			}
		} catch (final Exception e) {
			throw new AnalysisException(e);
		}
	}

	private CustomType findType(final String name, final Module currentModule) {
		final CustomType t = (CustomType) currentModule.findType(name);
		return t != null ? t : (CustomType) currentModule.parent().findType(name);
	}

	private final CustomType m_actualType;
	private final boolean m_isCurrentModule;
	private final Map<Field, DefaultValue> m_overriddenDefaultValues;

}
