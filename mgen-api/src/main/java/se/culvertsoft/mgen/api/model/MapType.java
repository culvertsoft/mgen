package se.culvertsoft.mgen.api.model;

/**
 * Interface for all map types (e.g. map[A,B])
 */
public interface MapType extends Type {

	/**
	 * The type of the keys of this map
	 */
	public Type keyType();

	/**
	 * The type of the keys of this map
	 */
	public Type valueType();

}
