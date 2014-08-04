package se.culvertsoft.mgen.api.model.impl;

import java.util.Map;

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

	public ObjectDefaultValueImpl(
			final CustomType expectedType,
			final CustomType actualType,
			final Module currentModule,
			final Map<Field, DefaultValue> overriddenDefaultValues) {
		super(expectedType);
		m_actualType = actualType;
		m_isCurrentModule = m_actualType.module() == currentModule;
		m_overriddenDefaultValues = overriddenDefaultValues;
	}

	private final CustomType m_actualType;
	private final boolean m_isCurrentModule;
	private final Map<Field, DefaultValue> m_overriddenDefaultValues;

}
