package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a default value for an object/CustomType field/type.
 */
public class ObjectDefaultValue extends DefaultValue {

	/**
	 * The expected type of this default value
	 */
	@Override
	public ClassType expectedType() {
		return (ClassType) super.expectedType();
	}

	/**
	 * The actual type of this default value, which is either the same as the
	 * expected type or a subtype of it
	 */
	public ClassType actualType() {
		return m_actualType;
	}

	/**
	 * Returns if the type of this default value is defined in the same scope as
	 * where it's now referenced.
	 */
	public boolean isLocalDefinition() {
		return m_isLocalDefinition;
	}

	/**
	 * Returns if the default value is of a subtype
	 */
	public boolean isDefaultTypeOverriden() {
		return expectedType() != actualType();
	}

	/**
	 * The overridden field default values of this default value
	 */
	public Map<Field, DefaultValue> overriddenDefaultValues() {
		return m_overriddenDefaultValues;
	}

	@Override
	public String toString() {
		return m_actualType.fullName() + ": {" + m_overriddenDefaultValues + "}";
	}

	public ObjectDefaultValue(
			final ClassType expectedType,
			final ClassType actualType,
			final Map<Field, DefaultValue> overriddenDefaultValues,
			final ClassType referencedFrom) {
		super(expectedType, referencedFrom);
		m_actualType = actualType;
		m_isLocalDefinition = m_actualType.module() == referencedFrom.module();
		m_overriddenDefaultValues = overriddenDefaultValues;
	}

	private final ClassType m_actualType;
	private final boolean m_isLocalDefinition;
	private final Map<Field, DefaultValue> m_overriddenDefaultValues;
}
