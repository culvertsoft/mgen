package se.culvertsoft.mgen.api.model;

/**
 * Base type for all compiler data types.
 * 
 * @author GiGurra
 */
public abstract class Type {

	/**
	 * The short (unqualified) name of this type. If used outside the compiler,
	 * it will not return the short name but instead return the same as
	 * fullName().
	 */
	public abstract String shortName();

	/**
	 * The full (qualified) name of this type.
	 */
	public abstract String fullName();

	/**
	 * Returns a utility enum to easier switch on field and data types.
	 */
	public TypeEnum typeEnum() {
		return m_enum;
	}

	/**
	 * The 1 byte type tag of this type, specifically used by MGen binary
	 * streams. Can be used both within the compiler and during runtime.
	 */
	public byte typeTag() {
		return typeEnum().binaryMetadatTag();
	}

	/**
	 * Checks if this type has been fully parsed and linked by the compiler.
	 * Always returns false outside the compiler.
	 */
	public abstract boolean isLinked();

	/**
	 * Returns if this type contains any custom defined type. Specifically
	 * useful for generic types and containers, where it's not always obvious.
	 */
	public abstract boolean containsUserDefinedType();

	/**
	 * Returns the class represented by this type object. May only be called on
	 * generated code and not while inside the compiler. Calling from inside the
	 * compiler on arrays, classes or enums returns null.
	 */
	public abstract Class<?> classOf();

	@Override
	public int hashCode() {
		return fullName().hashCode();
	}

	@Override
	public String toString() {
		return fullName();
	}

	protected Type(final TypeEnum enm) {
		m_enum = enm;
	}

	private final TypeEnum m_enum;

}
