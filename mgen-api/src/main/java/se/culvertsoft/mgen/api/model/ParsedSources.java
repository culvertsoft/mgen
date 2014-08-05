package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * ParsedSources a set of parsed source files, in some idl language.
 */
public class ParsedSources {

	/**
	 * Retrieves the settings specified. The settings are created from: <command
	 * line args> + <project file settings>.
	 * 
	 * @return The settings for this project
	 */
	public Map<String, String> settings() {
		return m_settings;
	}

	/**
	 * Returns the modules parsed.
	 * 
	 * @return The modules parsed.
	 */
	public List<Module> modules() {
		return m_modules;
	}

	/**
	 * Returns the project dependencies.
	 * 
	 * @return The project dependencies.
	 */
	public List<Project> dependencies() {
		return m_dependencies;
	}

	/**
	 * Replaces all settings
	 */
	public void setSettings(Map<String, String> settings) {
		m_settings = settings;
	}

	/**
	 * Replaces all modules
	 */
	public void setModules(List<Module> modules) {
		m_modules = modules;
	}

	/**
	 * Replaces all dependencies
	 */
	public void setDependencies(List<Project> dependencies) {
		m_dependencies = dependencies;
	}

	/**
	 * Creates a list of all references modules, recursively including all
	 * dependencies and dependencies of dependencies
	 */
	public List<Module> allModulesRecursively() {
		final HashSet<Module> modules = new LinkedHashSet<Module>();
		final HashSet<ParsedSources> projects = new LinkedHashSet<ParsedSources>();
		allModulesRecursively(modules, projects);
		return new ArrayList<Module>(modules);
	}

	private Map<String, String> m_settings;
	private List<Module> m_modules;
	private List<Project> m_dependencies;

	public ParsedSources() {
		m_settings = new LinkedHashMap<String, String>();
		m_modules = new ArrayList<Module>();
		m_dependencies = new ArrayList<Project>();
	}

	/**
	 * Internal backing method of allModulesRecursively()
	 */
	protected void allModulesRecursively(
			final HashSet<Module> out,
			final HashSet<ParsedSources> scannedSources) {

		if (!scannedSources.contains(this)) {
			scannedSources.add(this);

			for (final Project d : m_dependencies) {
				d.allModulesRecursively(out, scannedSources);
			}

			for (final Module m : m_modules) {
				out.add(m);
			}

		}
	}

	/**
	 * Convenience methods for finding a UserDefinedType
	 */
	protected UserDefinedType findType(
			final String name,
			final HashSet<ParsedSources> alreadySearched) {

		if (alreadySearched.contains(this))
			return null;

		alreadySearched.add(this);

		for (final Module m : modules()) {
			final UserDefinedType foundType = m.findType(name);
			if (foundType != null)
				return foundType;
		}

		for (final Project d : m_dependencies) {
			final UserDefinedType foundType = d.findType(name, alreadySearched);
			if (foundType != null)
				return foundType;
		}
		return null;
	}

}
