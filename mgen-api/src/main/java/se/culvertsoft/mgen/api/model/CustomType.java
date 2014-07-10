package se.culvertsoft.mgen.api.model;

import java.util.List;
import java.util.Set;

/**
 * A class definition
 * 
 * @author GiGurra
 * 
 */
public interface CustomType extends Type {

	/**
	 * 64 bit id hashed from the qualified type name of this class
	 */
	public long typeId();

	/**
	 * 16 bit id hashed from the qualified type name of this class. Verified by
	 * the compiler to be unique among classes with the same super type.
	 */
	public short typeId16Bit();

	/**
	 * The base64 representation of typeId16Bit()
	 */
	public String typeId16BitBase64();

	/**
	 * The written name of this type
	 */
	public String name();

	/**
	 * The that this class is defined within
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public Module module();

	/**
	 * The super type of this class, or null if it has none
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public CustomType superType();

	/**
	 * True if this type has a super type
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public boolean hasSuperType();

	/**
	 * True if this type has sub types
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public boolean hasSubTypes();

	/**
	 * Returns the sub types of this class
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public List<CustomType> subTypes();

	/**
	 * Returns all (recursive) super types of this class
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public List<CustomType> superTypeHierarchy();

	/**
	 * Returns all fields of this class, excluding those of its super type(s)
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public List<Field> fields();

	/**
	 * Returns all fields of this class, including those of its super type(s)
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public List<Field> fieldsInclSuper();

	/**
	 * Returns the set of types referenced from this type.
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public Set<CustomType> referencedClasses();

	/**
	 * Returns the set of types referenced from this type.
	 */
	public Set<EnumType> referencedEnums();

}
