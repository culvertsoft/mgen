package se.culvertsoft.mgen.api.model;

import java.util.List;
import java.util.Map;

/**
 * A module represents a set of types, similar to a java package or c++
 * namespace.
 * 
 * @author GiGurra
 * 
 */
public interface Module {

	/**
	 * Retrieves the settings map for this module. The settings are created
	 * from: <command line args> + <project file settings> + <module file
	 * settings>
	 * 
	 * @return The settings for this module
	 */
	public Map<String, String> settings();

	/**
	 * Returns the path of this module, i.e. the java package or c++ namespace
	 * of this module.
	 * 
	 * @return The module path of this module.
	 */
	public String path();

	/**
	 * The written file path used to reference this module in its parent project
	 * file.
	 * 
	 * @return The written file path to this module.
	 */
	public String filePath();

	/**
	 * The absolute file path calculated from \filePath().
	 * 
	 * @return The absolute file path to this module
	 */
	public String absoluteFilePath();

	/**
	 * Returns the classes defined within this module.
	 * 
	 * @return The classes defined within this module.
	 */
	public List<CustomType> types();

	/**
	 * Returns the enumerations defined within this module.
	 * 
	 * @return The enumerations defined within this module.
	 */
	public List<EnumType> enums();
	
}
