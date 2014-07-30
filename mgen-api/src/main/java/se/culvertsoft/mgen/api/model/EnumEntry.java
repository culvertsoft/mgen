package se.culvertsoft.mgen.api.model;

/**
 * Represents an entry in an enumeration
 * 
 * @author GiGurra
 * 
 */
public interface EnumEntry {

	/**
	 * The written name of the entry
	 * 
	 * @return The written name of the entry
	 */
	String name();

	/**
	 * The value specified for the enum, or null otherwise
	 * 
	 * @return The value specified for the enum, or null otherwise
	 */
	String constant();

}
