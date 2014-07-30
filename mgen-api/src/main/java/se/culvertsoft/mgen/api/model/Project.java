package se.culvertsoft.mgen.api.model;

import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.impl.GeneratorDescriptorImpl;

/**
 * A project represents a set of modules (see Module) parsed by the MGen
 * Compiler through a Parser.
 * 
 * @author GiGurra
 */
public interface Project {

	/**
	 * The given name of this project. This name is the same as the project.xml
	 * file excluding ".xml".
	 * 
	 * @return The name of this project.
	 */
	String name();

	/**
	 * Retrieves the settings map for this project. The settings are created
	 * from: <command line args> + <project file settings>.
	 * 
	 * @return The settings for this project
	 */
	Map<String, String> settings();

	/**
	 * The file search path of this project's definition file, as written as
	 * command line argument for the compiler, or in a depender's project
	 * definition file.
	 */
	String filePath();

	/**
	 * The file absolute search path of this project's definition file.
	 */
	String absoluteFilePath();

	/**
	 * Returns the modules contained within this project.
	 * 
	 * @return The modules contained within this project.
	 */
	List<Module> modules();

	/**
	 * Returns the projects that this project depends on.
	 * 
	 * @return The projects that this project depends on.
	 */
	List<Project> dependencies();

	/**
	 * Returns the generators that were specified for this project.
	 * 
	 * @return The generators for this project.
	 */
	List<GeneratorDescriptorImpl> generators();

	/**
	 * @return If this project is the root project (= not a dependency).
	 */
	boolean isRoot();

	/**
	 * Searches the types of this project and its dependencies for a type with a
	 * given name
	 * 
	 * @param name
	 *            The short or full class name of the type to find
	 * @return The found type, or null if none found
	 */
	Type findType(final String name);
	
	/**
	 * Gets the parent project of this project (if it's a dependency), otherwise it returns null.
	 */
	Project parent();

}
