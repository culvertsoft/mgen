package se.culvertsoft.mgen.api.model;

/**
 * Base interface for all compiler data types.
 * 
 * @author GiGurra
 */
public interface Type {

	/**
	 * The class of this type, intended to be used for type introspection during
	 * runtime - not from within the compiler.
	 * 
	 * @throws RuntimeException
	 *             If called inside the compiler
	 */
	Class<?> classOf();

	/**
	 * Returns a utility enum to easier switch on field and data types
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	TypeEnum typeEnum();

	/**
	 * The 1 byte type tag of this type, specifically used by MGen binary
	 * streams. Can be used both within the compiler and during runtime.
	 */
	byte typeTag();

	/**
	 * The short (unqualified) name of this type. If used outside the compiler,
	 * it will not return the short name but instead return the same as
	 * fullName().
	 */
	String shortName();

	/**
	 * The full (qualified) name of this type.
	 */
	String fullName();

	/**
	 * Checks if this type has been fully parsed and linked by the compiler.
	 * Always returns false outside the compiler.
	 */
	boolean isLinked();

	/**
	 * Returns if this type contains any custom defined type. Specifically
	 * useful for generic types and containers, where it's not always obvious.
	 */
	boolean containsCustomType();

}
