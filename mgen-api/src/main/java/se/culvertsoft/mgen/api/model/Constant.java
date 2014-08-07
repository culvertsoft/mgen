package se.culvertsoft.mgen.api.model;

/**
 * Represents a class static constant value or object.
 */
public class Constant {

	/**
	 * Returns the type of this constant.
	 * 
	 * @return the type of this constant
	 */
	public Type typ() {
		return m_type;
	}

	/**
	 * Sets the type of this constant.
	 * 
	 * @param m_type
	 *            the new type of this constant.
	 */
	public void setType(Type m_type) {
		this.m_type = m_type;
	}

	/**
	 * Sets the value of this constant
	 * 
	 * @param m_value
	 *            the value of this constant
	 */
	public void setValue(DefaultValue m_value) {
		this.m_value = m_value;
	}

	/**
	 * Gets the type of this constant.
	 * 
	 * @return the type of this constant.
	 */
	public Type ty() {
		return m_type;
	}

	/**
	 * Gets the value of this constant.
	 * 
	 * @return the value of this constant.
	 */
	public DefaultValue value() {
		return m_value;
	}

	/**
	 * Gets the short name of this constant (without package.class path
	 * prepended). For example a class se.culvertsoft.Foo with constant Bar
	 * would return Bar.
	 */
	public String shortName() {
		return m_shortName;
	}

	/**
	 * Gets the full name of this constant (with package.class path prepended).
	 * For example a class se.culvertsoft.Foo with constant Bar would return
	 * se.culvertsoft.Foo.Bar.
	 * 
	 * @return
	 */
	public String fullName() {
		return m_fullName;
	}

	/**
	 * Returns the parent class of this constant.
	 * 
	 * @return the parent class of this constant.
	 */
	public ClassType parent() {
		return m_parent;
	}

	/**
	 * Returns if this constant has been linked (both its type and value).
	 * 
	 * @return if this constant has been linked.
	 */
	public boolean isLinked() {
		return m_type != null && m_type.isLinked() && m_value != null && m_value.isLinked();
	}

	@Override
	public String toString() {
		return "Constant [m_fullName=" + m_fullName + ", m_type=" + m_type + ", m_value=" + m_value
				+ "]";
	}

	public Constant(
			final String name,
			final ClassType parent,
			final Type type,
			final DefaultValue value) {
		m_shortName = name;
		m_fullName = parent.fullName() + "." + name;
		m_parent = parent;
		m_type = type;
		m_value = value;
	}

	private final String m_shortName;
	private final String m_fullName;
	private final ClassType m_parent;
	private Type m_type;
	private DefaultValue m_value;

}
