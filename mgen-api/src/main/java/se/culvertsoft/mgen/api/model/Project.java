package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;

/**
 * A project represents a set of modules (see Module) parsed by the MGen
 * Compiler through a Parser. When the MGen compiler is executed, the result of
 * the parsing phase is always a root project, which may contain Modules defined
 * within. It may also reference further projects as dependencies.
 * 
 * IDL --&gt; [Compiler: parsers] to Project { modules, dependencies } --&gt;
 * [Compiler: generators] --&gt; Generated source code
 */
public class Project extends ParsedSources {

	/**
	 * Gets the given name of this project. This name is the same as the
	 * project.xml file excluding ".xml".
	 * 
	 * @return The name of this project.
	 */
	public String name() {
		return m_name;
	}

	/**
	 * Gets the file search path of this project's definition file, as written
	 * as command line argument for the compiler, or in a depender's project
	 * definition file.
	 * 
	 * @return The file search path of this project's definition file
	 */
	public String filePath() {
		return m_filePath;
	}

	/**
	 * Gets the file absolute search path of this project's definition file.
	 * 
	 * @return The file absolute search path of this project's definition file
	 */
	public String absoluteFilePath() {
		return m_absoluteFilePath;
	}

	/**
	 * Returns the generators that were specified for this project.
	 * 
	 * @return The generators for this project.
	 */
	public List<GeneratorDescriptor> generators() {
		return m_generators;
	}

	/**
	 * Checks if this project is the root project (= not a dependency)
	 * 
	 * @return If this project is the root project (= not a dependency).
	 */
	public boolean isRoot() {
		return m_parent == null;
	}

	/**
	 * Gets the parent project of this project (if it's a dependency), otherwise
	 * it returns null.
	 * 
	 * @return The parent project of this project (if it's a dependency),
	 *         otherwise it returns null
	 */
	public Project parent() {
		return m_parent;
	}

	/**
	 * Sets the name of this Project
	 * 
	 * @param name
	 *            The new name of this Project
	 */
	public void setName(String name) {
		m_name = name;
	}

	/**
	 * Sets the written (relative or absolute) file path of this project
	 * 
	 * @param filePath
	 *            The new written (relative or absolute) file path of this
	 *            project
	 */
	public void setFilePath(String filePath) {
		m_filePath = filePath;
	}

	/**
	 * Sets the absolute file path of this project
	 * 
	 * @param absolutefilePath
	 *            The new absolute file path of this project
	 */
	public void setAbsoluteFilePath(String absolutefilePath) {
		m_absoluteFilePath = absolutefilePath;
	}

	/**
	 * Replaces all generators of this project
	 * 
	 * @param generators
	 *            The new generators of this project
	 */
	public void setGenerators(List<GeneratorDescriptor> generators) {
		m_generators = generators;
	}

	/**
	 * Convenience method for parsers. Gets an existing module with a given
	 * module path or creates a new one and adds it to this Project.
	 * 
	 * Both relative and absolute file path arguments of this function must be
	 * supplied (cannot be null). However the file paths arguments are not
	 * required to point to actual files. These arguments are necessary when the
	 * compiler is set to output IDL code instead of normal source code/classes.
	 * This is used primarily when consolidating multiple IDL languages and
	 * sources into a single IDL definition. These arguments will then be used
	 * to determine where to save the new definitions.
	 * 
	 * @param modulePath
	 *            The path of this module. The path of a module is its full java
	 *            package, e.g. com.mycompany.myproduct.
	 * 
	 * @param filePath
	 *            The written (relative or absolute) file path to the file in
	 *            which the Module is defined
	 * 
	 * @param absoluteFilePath
	 *            The absolute file path to the file in which the Module is
	 *            defined
	 * 
	 * @param settings
	 *            The settings for a new module. This parameter is ignored if
	 *            the module already exists within the project.
	 * 
	 * @return The new or existing Module (already added to this Project, by
	 *         this function call)
	 * 
	 * @throws AnalysisException if a module with the given module path already
	 *         exists but is in a different project that this.
	 */
	public Module getOrCreateModule(
			final String modulePath,
			final String filePath,
			final String absoluteFilePath,
			final java.util.Map<String, String> settings) {

		final Module existingModule = findModule(modulePath);
		if (existingModule != null) {
			if (existingModule.parent() != this)
				throw new AnalysisException("Types for module " + modulePath
						+ " are defined in multiple projects. This is not (yet) allowed");
			return existingModule;
		} else {
			final Module newModule = new Module(
					modulePath,
					filePath,
					absoluteFilePath,
					settings,
					this);
			addModule(newModule);
			return newModule;
		}
	}

	/**
	 * Searches the modules of this entire project and its dependencies for a
	 * module with a specific fully qualified package name. Not just the modules
	 * defined within this Project.
	 * 
	 * @param name
	 *            The name of the module to find
	 * 
	 * @return The found module, or null if none was found
	 */
	private Module findModule(final String name) {
		if (isRoot()) {
			return findModule(name, new HashSet<ParsedSources>());
		} else {
			return m_parent.findModule(name);
		}
	}

	/**
	 * Creates a new Project
	 * 
	 * @param name
	 *            The name of the Project
	 * 
	 * @param filePath
	 *            The written file path of the Project file
	 * 
	 * @param absoluteFilePath
	 *            The absolute file path of the Project file
	 * 
	 * @param parent
	 *            It's parent project, or null if it is the root project
	 */
	public Project(
			final String name,
			final String filePath,
			final String absoluteFilePath,
			final Project parent) {
		m_name = name;
		m_filePath = filePath;
		m_absoluteFilePath = absoluteFilePath;
		m_generators = new ArrayList<GeneratorDescriptor>();
		m_parent = parent;
	}

	private String m_name;
	private String m_filePath;
	private String m_absoluteFilePath;
	private List<GeneratorDescriptor> m_generators;
	private final Project m_parent;

}
