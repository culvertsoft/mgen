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
	 * The overridden field default values of this default value
	 */
	public Map<Field, DefaultValue> overriddenDefaultValues() {
		return m_overriddenDefaultValues;
	}

	public ObjectDefaultValue(
			final ClassType expectedType,
			final ClassType actualType,
			final Module currentModule,
			final Map<Field, DefaultValue> overriddenDefaultValues) {
		super(expectedType);
		m_actualType = actualType;
		m_isCurrentModule = m_actualType.module() == currentModule;
		m_overriddenDefaultValues = overriddenDefaultValues;
	}

	private final ClassType m_actualType;
	private final boolean m_isCurrentModule;
	private final Map<Field, DefaultValue> m_overriddenDefaultValues;
}
