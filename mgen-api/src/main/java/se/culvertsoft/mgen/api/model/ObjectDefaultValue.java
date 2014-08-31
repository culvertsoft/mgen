package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a default value for an object/CustomType field/type.
 */
public class ObjectDefaultValue extends DefaultValue {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassType expectedType() {
		return (ClassType) super.expectedType();
	}

	/**
	 * Gets the actual type of this default value, which is either the same as
	 * the expected type or a subtype of it
	 * 
	 * @return The actual type of this default value
	 */
	public ClassType actualType() {
		return m_actualType;
	}

	/**
	 * Checks if the type of this default value is defined in the same scope as
	 * where it's now referenced.
	 * 
	 * @return If the type of this default value is defined in the same scope as
	 *         where it's now referenced
	 */
	public boolean isLocalDefinition() {
		return m_isLocalDefinition;
	}

	/**
	 * Checks if expectedType() != actualType() (implies that actualType() is a
	 * sub type)
	 * 
	 * @return If expectedType() != actualType()
	 */
	public boolean isDefaultTypeOverriden() {
		return expectedType() != actualType();
	}

	/**
	 * Gets the overridden field default values of this default value
	 * 
	 * @return The overridden field default values of this default value
	 */
	public Map<Field, DefaultValue> overriddenDefaultValues() {
		return m_overriddenDefaultValues;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return m_actualType.fullName() + ": {" + m_overriddenDefaultValues
				+ "}";
	}

	/**
	 * Creates a new ObjectDefaultValue
	 * 
	 * @param expectedType
	 *            The expected type of the default value (the type of the field
	 *            that has this default value)
	 * 
	 * @param actualType
	 *            The actual type of the default value (either the same as
	 *            expected type or a sub type of it)
	 * 
	 * @param overriddenDefaultValues
	 *            The field default values of this ObjectDefaultValue. These
	 *            work by overriding the actualType's normal default values.
	 * 
	 * @param referencedFrom
	 *            The class wherein this default value is defined
	 */
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
