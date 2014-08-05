package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a bool field/type.
 */
public class BoolDefaultValue extends DefaultValue {

	/**
	 * Returns the boolean value represented by this default value
	 */
	public boolean value() {
		return m_value;
	}

	/**
	 * The type of this bool default value
	 */
	@Override
	public BoolType expectedType() {
		return (BoolType) super.expectedType();
	}
	
	public BoolDefaultValue(final boolean value) {
		super(BoolType.INSTANCE);
		m_value = value;
	}

	private final boolean m_value;

}
