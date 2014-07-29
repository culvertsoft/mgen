package se.culvertsoft.mgen.api.model;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;

/**
 * Represents a default value for an object/CustomType field/type.
 */
public class ObjectDefaultValue extends DefaultValue {

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
	public CustomType actualType() {
		return m_actualType;
	}

	/**
	 * Returns if the type of this default value is defined in the same module
	 * as where it's now referenced.
	 */
	public boolean isCurrentModule() {
		return m_isCurrentModule;
	}

	/**
	 * Returns if the default value is of a subtype
	 */
	public boolean isDefaultTypeOverriden() {
		return expectedType() != actualType();
	}

	/**
	 * The field default values of this default value
	 */
	public Map<Field, DefaultValue> values() {
		return m_values;
	}

	@SuppressWarnings("unchecked")
	public ObjectDefaultValue(
			final CustomType expectedType,
			final String writtenString,
			final Module currentModule) {
		super(expectedType, writtenString);
		m_values = new HashMap<Field, DefaultValue>();
		try {

			final Object src = JSONValue.parseWithException(writtenString);
			if (src instanceof JSONObject) {

				final JSONObject jsonObject = (JSONObject) src;

				final String optActualTypeName = jsonObject.get("__TYPE").toString();

				if (optActualTypeName != null) {
					m_actualType = findType(optActualTypeName, currentModule);

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

				for (final Field f : m_actualType.fieldsInclSuper()) {
					if (f.hasDefaultValue()) {
						m_values.put(f, f.defaultValue());
					}
				}

				for (final Map.Entry<String, Object> e : ((Map<String, Object>) src).entrySet()) {
					final String fieldName = e.getKey();

					final Field f = m_actualType.findField(fieldName);
					if (f == null) {
						throw new AnalysisException("Failed to set default value. No field named "
								+ fieldName + " was found on type " + m_actualType);
					}

					final DefaultValue value = DefaultValue.parse(
							f.typ(),
							e.getValue().toString(),
							currentModule);
					m_values.put(f, value);
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
	private final Map<Field, DefaultValue> m_values;

}
