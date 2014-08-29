package se.culvertsoft.mgen.javapack.classes;

/**
 * An interface that represents a way to default construct an instance of an
 * MGen object. Registered in class registries to facilitate runtime
 * instantiation of object of generated classes primarily during
 * deserialization.
 */
public interface Ctor {

	/**
	 * A method for default instantiating an object of a particular generated
	 * MGen class.
	 * 
	 * @return A new default constructed object of a particular generated MGen
	 *         class.
	 */
	public MGenBase create();

}
