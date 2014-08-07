package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for an enum field/type.
 */
public class EnumDefaultValue extends DefaultValue {

	/**
	 * Returns the enum value/entry represented by this default value object
	 */
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
	public boolean isCurrentModule() {
		return m_isCurrentModule;
	}

	@Override
	public String toString() {
		return m_value.toString();
	}

	public EnumDefaultValue(final EnumType typ, final EnumEntry entry, final Module currentModule) {
		super(typ);
		m_isCurrentModule = typ.module() == currentModule;
		m_value = entry;
	}

	private final EnumEntry m_value;
	private final boolean m_isCurrentModule;

}
