package se.culvertsoft.mgen.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.impl.ModuleImpl;

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
	 * Returns the module path of this module, i.e. the java package or c++
	 * namespace of this module.
	 * 
	 * @return The module path of this module.
	 */
	public String path();

	/**
	 * Returns the types defined within this module. The types are returned in a
	 * map where the key is the full type name (<module_path>.<typeName>) and
	 * the value is the type.
	 * 
	 * @return The types defined within this module.
	 */
	public Map<String, CustomType> types();

	/**
	 * Utility method for getting all known types (known from this module) with
	 * the provided unqualified (type name without module path). This includes
	 * both types that may be defined within this module but also in its
	 * dependencies.
	 * 
	 * @param typeName
	 *            The unqualified (excl. module path) type name of the type to
	 *            look for
	 * @return All known types with unqualified type name=typeName
	 */
	public List<Type> getAllKnownTypesWithName(final String typeName);

	/**
	 * Helper singleton for some analysis steps and generators. Usually not
	 * needed for generation. You'll know if you need it :).
	 */
	public static Module INSTANCE = new ModuleImpl(
			"",
			new HashMap<String, String>());

}
