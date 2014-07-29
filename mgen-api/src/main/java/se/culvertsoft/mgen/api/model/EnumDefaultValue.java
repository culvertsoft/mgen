package se.culvertsoft.mgen.api.model;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;

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
	public EnumType typ() {
		return (EnumType) super.typ();
	}

	public EnumDefaultValue(final EnumType typ, final String writtenString) {
		super(typ, writtenString);
		final String[] parts = writtenString.split("\\.");
		final String entryName = parts[parts.length - 1];
		for (final EnumEntry e : typ.entries()) {
			if (e.name().equals(entryName)) {
				m_value = e;
				return;
			}
		}
		throw new AnalysisException("Don't know any enum value named " + writtenString
				+ " for enum type " + typ);
	}

	private final EnumEntry m_value;

}
