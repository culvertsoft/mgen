package se.culvertsoft.mgen.api.model;

/**
 * Base type for all compiler data types. Used to indicate types of class
 * fields, super types, sub types and default values.
 */
public abstract class Type {

	/**
	 * The short (unqualified) name of this type. If used outside the compiler,
	 * it will not return the short name but instead return the same as
	 * fullName().
	 * 
	 * @return The short name of this type
	 */
	public abstract String shortName();

	/**
	 * The full (qualified) name of this type.
	 * 
	 * @return The full name of this type.
	 */
	public abstract String fullName();

	/**
	 * Returns a utility enum to easier switch on field and data types.
	 * 
	 * @return A utility enum to easier switch on field and data types
	 */
	public TypeEnum typeEnum() {
		return m_enum;
	}

	/**
	 * The 1 byte type tag of this type, specifically used by MGen binary
	 * streams. Can be used both within the compiler and during runtime.
	 * 
	 * @return The 1 byte type tag of this type, specifically used by MGen
	 *         binary streams
	 */
	public byte typeTag() {
		return typeEnum().binaryMetadatTag();
	}

	/**
	 * Checks if this type has been fully parsed and linked by the compiler.
	 * Always returns false outside the compiler.
	 * 
	 * @return If this type is linked
	 */
	public abstract boolean isLinked();

	/**
	 * Returns if this type contains any custom defined type. Specifically
	 * useful for generic types and containers, where it's not always obvious.
	 * 
	 * @return If this type contains a user defined type
	 */
	public abstract boolean containsUserDefinedType();

	/**
	 * Returns the class represented by this type object. May only be called on
	 * generated code and not while inside the compiler. Calling from inside the
	 * compiler on arrays, classes or enums returns null.
	 * 
	 * @return The class that this Type represents, or null if this method is
	 *         called from within the compiler on MGen object types.
	 */
	public abstract Class<?> classOf();

	/**
	 * A hash code implemented as fullName().hashCode()
	 * 
	 * @return Hash code of this Type
	 */
	@Override
	public int hashCode() {
		return fullName().hashCode();
	}

	/**
	 * Returns the fully qualified name of this type (e.g. com.mypackage.Type)
	 * 
	 * @return The full (qualified) name of this type
	 */
	@Override
	public String toString() {
		return fullName();
	}

	/**
	 * Creates a new Type. Only to be used internally by the compiler
	 * 
	 * @param enm
	 *            The TypeEnum of this type
	 */
	protected Type(final TypeEnum enm) {
		m_enum = enm;
	}

	private final TypeEnum m_enum;

}
