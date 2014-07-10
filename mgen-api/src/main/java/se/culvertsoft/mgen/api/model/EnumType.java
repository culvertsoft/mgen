package se.culvertsoft.mgen.api.model;

import java.util.List;

import se.culvertsoft.mgen.api.model.impl.EnumTypeImpl;

/**
 * Represents an enumeration
 * 
 * @author GiGurra
 * 
 */
public interface EnumType extends SimpleType, UserDefinedType {

	/**
	 * The entries of this enumeration.
	 * 
	 * @return The entries of this enumeration.
	 */
	public List<EnumEntry> entries();

	/**
	 * The module this enumeration is defined in
	 * 
	 * @return The module this enumeration is defined in
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public Module module();
	
	/**
	 * Intended for serializers. gets enum instances by name.
	 * 
	 * @throws RuntimeException
	 *             If called inside the compiler
	 */
	public Enum<?> get(final String entryStringName);

	/**
	 * Intended for serializers. gets enum instances by integer.
	 * 
	 * @throws RuntimeException
	 *             If called inside the compiler
	 */
	public Enum<?> get(final int entryIntvalue);

	public static final EnumType INSTANCE = new EnumTypeImpl("0xFFFF", "UNKNOWN", null);

}
