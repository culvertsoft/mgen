package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;

/**
 * A project represents a set of modules (see Module) parsed by the MGen
 * Compiler through a Parser.
 */
public class Project extends ParsedSources {

	/**
	 * The given name of this project. This name is the same as the project.xml
	 * file excluding ".xml".
	 * 
	 * @return The name of this project.
	 */
	public String name() {
		return m_name;
	}

	/**
	 * The file search path of this project's definition file, as written as
	 * command line argument for the compiler, or in a depender's project
	 * definition file.
	 */
	public String filePath() {
		return m_filePath;
	}

	/**
	 * The file absolute search path of this project's definition file.
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
	 * @return If this project is the root project (= not a dependency).
	 */
	public boolean isRoot() {
		return m_parent == null;
	}

	/**
	 * Gets the parent project of this project (if it's a dependency), otherwise
	 * it returns null.
	 */
	public Project parent() {
		return m_parent;
	}

	/**
	 * Sets the name of this project
	 */
	public void setName(String name) {
		m_name = name;
	}

	/**
	 * Sets the written (relative or absolute) file path of this project
	 */
	public void setFilePath(String filePath) {
		m_filePath = filePath;
	}

	/**
	 * Sets the absolute file path of this project
	 */
	public void setAbsoluteFilePath(String absolutefilePath) {
		m_absoluteFilePath = absolutefilePath;
	}

	/**
	 * Replaces all generators
	 */
	public void setGenerators(List<GeneratorDescriptor> generators) {
		m_generators = generators;
	}

	/**
	 * Convenience method for parsers.
	 * 
	 * Returns an existing module with the given module path or creates a new
	 * one and adds it to this Project.
	 * 
	 * Both file paths must be supplied (cannot be null).
	 * 
	 * @Throws AnalysisException if a module with the given module path already
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
	 * Searches the modules of this project and its dependencies for a module
	 * with a specific fully qualified package name.
	 */
	private Module findModule(final String name) {
		if (isRoot()) {
			return findModule(name, new HashSet<ParsedSources>());
		} else {
			return m_parent.findModule(name);
		}
	}

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
