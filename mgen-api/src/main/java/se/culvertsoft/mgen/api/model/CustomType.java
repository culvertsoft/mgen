package se.culvertsoft.mgen.api.model;

import java.util.List;
import java.util.Set;

/**
 * A class definition
 * 
 * @author GiGurra
 * 
 */
public interface CustomType extends UserDefinedType {

	/**
	 * 64 bit id hashed from the qualified type name of this class
	 */
	long typeId();

	/**
	 * 16 bit id hashed from the qualified type name of this class. Verified by
	 * the compiler to be unique among classes with the same super type.
	 */
	short typeId16Bit();

	/**
	 * The base64 representation of typeId16Bit()
	 */
	String typeId16BitBase64();

    /**
     * the base 16 bit type concatinated with all parents 16 bits hash.
     */
    String typeId16BitBase64Hierarchy();

    /**
	 * The written name of this type
	 */
	String name();

	/**
	 * The super type of this class, or null if it has none
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	CustomType superType();

	/**
	 * True if this type has a super type
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	boolean hasSuperType();

	/**
	 * True if this type has sub types
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	boolean hasSubTypes();

	/**
	 * Returns the sub types of this class
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	List<CustomType> subTypes();

	/**
	 * Returns all (recursive) super types of this class
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	List<CustomType> typeHierarchy();

	/**
	 * Returns all fields of this class, excluding those of its super type(s)
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	List<Field> fields();

	/**
	 * Returns all fields of this class, including those of its super type(s)
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	List<Field> fieldsInclSuper();

	/**
	 * Returns the set of types referenced from this type.
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	Set<CustomType> referencedClasses();

	/**
	 * Returns the set of types referenced from this type.
	 */
	Set<EnumType> referencedEnums();
	
	/**
	 * Finds/Gets a field by name. Returns null if no such field is found.
	 */
	Field findField(final String name);

}
