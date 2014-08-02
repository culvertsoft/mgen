package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.EnumDefaultValue;
import se.culvertsoft.mgen.api.model.EnumEntry;
import se.culvertsoft.mgen.api.model.EnumType;
import se.culvertsoft.mgen.api.model.Module;

/**
 * Represents a default value for an enum field/type.
 */
public class EnumDefaultValueImpl extends DefaultValueImpl implements EnumDefaultValue {

	/**
	 * Returns the enum value/entry represented by this default value object
	 */
	@Override
	public EnumEntry value() {
		return m_value;
	}

	/**
	 * The type of this enum default value
	 */
	@Override
	public EnumType expectedType() {
		return (EnumType) super.expectedType();
	}

	/**
	 * Returns if the type of this default value is defined in the same module
	 * as where it's now referenced.
	 */
	@Override
	public boolean isCurrentModule() {
		return m_isCurrentModule;
	}

	public EnumDefaultValueImpl(
			final EnumType typ,
			final EnumEntry entry,
			final Module currentModule) {
		super(typ);
		m_isCurrentModule = typ.module() == currentModule;
		m_value = entry;
	}

	private final EnumEntry m_value;
	private final boolean m_isCurrentModule;

}
