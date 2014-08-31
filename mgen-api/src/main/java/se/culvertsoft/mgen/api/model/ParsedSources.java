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
	 * Retrieves the settings specified. The settings are created from: [command
	 * line args] + [project file settings].
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
	 * Replaces all settings for this ParsedSources
	 * 
	 * @param settings
	 *            The new settings for this ParsedSources
	 */
	public void setSettings(Map<String, String> settings) {
		m_settings = settings;
	}

	/**
	 * Replaces all modules of this ParsedSources
	 * 
	 * @param modules
	 *            The new modules for this ParsedSources
	 */
	public void setModules(List<Module> modules) {
		m_modules = modules;
	}

	/**
	 * Adds a new module to this ParsedSources
	 * 
	 * @param module
	 *            The module to add
	 */
	public void addModule(Module module) {
		m_modules.add(module);
	}

	/**
	 * Replaces all dependencies for this ParsedSources
	 * 
	 * @param dependencies
	 *            The new dependencies of this parsedSources
	 */
	public void setDependencies(List<Project> dependencies) {
		m_dependencies = dependencies;
	}

	/**
	 * Adds a new Project dependency to this ParsedSources
	 * 
	 * @param dependency
	 *            The Project dependency to add
	 */
	public void addDependency(Project dependency) {
		m_dependencies.add(dependency);
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
	 * @param out
	 * 		The HashSet this function will add modules to
	 * @param scannedSources
	 * 		The already scanned sources (Will not be recursed)
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
	 * Internal Convenience methods for finding a Module
	 * @param name
	 * 		The name of the module
	 * @param alreadySearched
	 *		Soruces that are already scanned
	 * @return The found module or null if not found
	 * 
	 */
	protected Module findModule(
			final String name,
			final HashSet<ParsedSources> alreadySearched) {

		if (alreadySearched.contains(this))
			return null;

		alreadySearched.add(this);

		for (final Module m : modules()) {
			if (m.path().equals(name))
				return m;
		}

		for (final Project d : m_dependencies) {
			final Module m = d.findModule(name, alreadySearched);
			if (m != null)
				return m;
		}
		return null;
	}
}
