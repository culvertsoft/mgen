package se.culvertsoft.mgen.javapack.classes;

/**
 * Interface that all MGen generated enums implement. Used primarily by readers
 * and writers for serialization.
 */
public interface MGenEnum {

	/**
	 * Gets the name of this enum value.
	 * 
	 * @return The name of this enum value.
	 */
	public String getStringValue();

	/**
	 * Gets the integer value of this enum value.
	 * 
	 * @return The integer value of this enum value.
	 */
	public int getIntValue();

}