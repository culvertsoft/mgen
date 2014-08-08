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
	 * Returns if the type of this default value is defined in the same scope it
	 * is referenced from.
	 */
	public boolean isLocalDefinition() {
		return m_isLocalDefinition;
	}

	@Override
	public String toString() {
		return m_value.toString();
	}

	public EnumDefaultValue(
			final EnumType typ,
			final EnumEntry entry,
			final ClassType referencedFrom) {
		super(typ, referencedFrom);
		m_isLocalDefinition = typ.module() == referencedFrom.module();
		m_value = entry;
	}

	private final EnumEntry m_value;
	private final boolean m_isLocalDefinition;

}
