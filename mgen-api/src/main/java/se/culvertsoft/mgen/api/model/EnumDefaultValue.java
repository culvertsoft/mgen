package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for an enum field/type.
 */
public class EnumDefaultValue extends DefaultValue {

	/**
	 * Returns the enum value/entry represented by this default value object
	 * 
	 * @return The enum value/entry represented by this default value object
	 */
	public EnumEntry value() {
		return m_value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnumType expectedType() {
		return (EnumType) super.expectedType();
	}

	/**
	 * Returns if the type of this default value is defined in the same scope it
	 * is referenced from.
	 * 
	 * @return If the type of this default value is defined in the same scope it
	 *         is referenced from.
	 */
	public boolean isLocalDefinition() {
		return m_isLocalDefinition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return m_value.toString();
	}

	/**
	 * Creates a new enum default value
	 * 
	 * @param typ
	 *            The enum type of this default value
	 * 
	 * @param entry
	 *            The enum value/entry of this default value
	 * 
	 * @param referencedFrom
	 *            The class from which this default value is referenced
	 */
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
