package se.culvertsoft.mgen.api.model;

import java.util.List;
import java.util.Map;

/**
 * ParsedSources a set of parsed source files, in some idl language.
 */
public interface ParsedSources {

	/**
	 * Retrieves the settings specified. The settings are created from: <command
	 * line args> + <project file settings>.
	 * 
	 * @return The settings for this project
	 */
	Map<String, String> settings();

	/**
	 * Returns the modules parsed.
	 * 
	 * @return The modules parsed.
	 */
	List<Module> modules();

	/**
	 * Returns the project dependencies.
	 * 
	 * @return The project dependencies.
	 */
	List<Project> dependencies();

	/**
	 * Creates a list of all references modules, recursively including all
	 * dependencies and dependencies of dependencies
	 */
	List<Module> allModulesRecursively();
	
}
