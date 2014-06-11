package se.culvertsoft.mgen.api.model;

import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.impl.ProjectImpl;
import se.culvertsoft.mgen.api.plugins.GeneratorDescriptor;

/**
 * A project represents an MGen Project. That is: a set of modules (see Module),
 * each containing a set of custom defined types (see CustomType).
 * 
 * A project is defined in a single project file, either xml or json, but may
 * reference any number of other project files.
 * 
 * @author GiGurra
 */
public interface Project {

	/**
	 * The given name of this project. The name must be unique as such that it
	 * does not also exist among its dependencies. The name is used by the
	 * default generators as the java class path for generated code.
	 * 
	 * @return The name of this project.
	 */
	public String name();

	/**
	 * Retrieves the settings map for this project. The settings are created
	 * from: <command line args> + <project file settings>.
	 * 
	 * @return The settings for this project
	 */
	public Map<String, String> settings();

	/**
	 * The file search path of this project's definition file, as written as 
	 * command line argument for the compiler, or in a depender's project 
	 * definition file.
	 */
	public String filePath();

	/**
	 * The file absolute search path of this project's definition file.
	 */
	public String absoluteFilePath();

	/**
	 * Returns the modules contained within this project.
	 * 
	 * @return The modules contained within this project.
	 */
	public List<Module> modules();

	/**
	 * Returns the projects that this project depends on.
	 * 
	 * @return The projects that this project depends on.
	 */
	public List<Project> dependencies();

	/**
	 * Returns the generators that were specified for this project. If this is a
	 * dependency, then the depending projects may add additional generators
	 * than those originally specified for this dependency/leaf.
	 * 
	 * @return The generators for this project.
	 */
	public List<GeneratorDescriptor> generators();

	/**
	 * Returns if this project is the root project.
	 * 
	 * @return If this project is the root project.
	 */
	public boolean isRoot();

	/**
	 * Helper singleton for some analysis steps and generators. Usually not
	 * needed for generation. You'll know if you need it :).
	 */
	public static Project INSTANCE = new ProjectImpl("", "", "", true);

}
