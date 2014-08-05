package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A project represents a set of modules (see Module) parsed by the MGen
 * Compiler through a Parser.
 * 
 * @author GiGurra
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
	 * Searches the types of this project and its dependencies for a type with a
	 * given name
	 * 
	 * @param name
	 *            The short or full class name of the type to find
	 * @return The found type, or null if none found
	 */
	public UserDefinedType findType(final String name) {
		if (isRoot()) {
			return findType(name, new HashSet<ParsedSources>());
		} else {
			return m_parent.findType(name);
		}
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
