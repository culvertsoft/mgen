package se.culvertsoft.mgen.api.model;

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
	 * Returns the types defined within this module. The types are returned in a
	 * map where the key is the full type name (<module_path>.<typeName>) and
	 * the value is the type.
	 * 
	 * @return The types defined within this module.
	 */
	public Map<String, CustomType> types();

}
