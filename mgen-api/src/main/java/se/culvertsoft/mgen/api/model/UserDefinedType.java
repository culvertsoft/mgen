package se.culvertsoft.mgen.api.model;

public interface UserDefinedType extends Type {

	/**
	 * The that this type is defined within
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public Module module();

}
