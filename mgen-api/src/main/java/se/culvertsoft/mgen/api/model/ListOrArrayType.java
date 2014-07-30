package se.culvertsoft.mgen.api.model;

/**
 * Convenience interface for array and list types
 */
public interface ListOrArrayType extends Type {

	/**
	 * The type of the elements in this array or list
	 * 
	 * @throws RuntimeException
	 *             If called inside the compiler
	 */
	Type elementType();

}
