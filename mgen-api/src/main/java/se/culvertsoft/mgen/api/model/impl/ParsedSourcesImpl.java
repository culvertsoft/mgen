package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.ParsedSources;
import se.culvertsoft.mgen.api.model.Project;
import se.culvertsoft.mgen.api.model.Type;

public class ParsedSourcesImpl implements ParsedSources {

	private Map<String, String> m_settings;
	private List<ModuleImpl> m_modules;
	private List<ProjectImpl> m_dependencies;

	public ParsedSourcesImpl() {
		m_settings = new LinkedHashMap<String, String>();
		m_modules = new ArrayList<ModuleImpl>();
		m_dependencies = new ArrayList<ProjectImpl>();
	}

	@Override
	public Map<String, String> settings() {
		return m_settings;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Module> modules() {
		return (List) m_modules;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Project> dependencies() {
		return (List) m_dependencies;
	}

	public void setSettings(Map<String, String> settings) {
		m_settings = settings;
	}

	public void setModules(List<ModuleImpl> modules) {
		m_modules = modules;
	}

	public void setDependencies(List<ProjectImpl> dependencies) {
		m_dependencies = dependencies;
	}

	@Override
	public List<Module> allModulesRecursively() {
		final HashSet<Module> modules = new LinkedHashSet<Module>();
		final HashSet<ParsedSourcesImpl> projects = new LinkedHashSet<ParsedSourcesImpl>();
		allModulesRecursively(modules, projects);
		return new ArrayList<Module>(modules);
	}

	protected void allModulesRecursively(
			final HashSet<Module> out,
			final HashSet<ParsedSourcesImpl> scannedSources) {

		if (!scannedSources.contains(this)) {
			scannedSources.add(this);

			for (final ProjectImpl d : m_dependencies) {
				d.allModulesRecursively(out, scannedSources);
			}

			for (final Module m : m_modules) {
				out.add(m);
			}

		}
	}

	protected Type findType(
			final String name,
			final HashSet<ParsedSources> alreadySearched) {

		if (alreadySearched.contains(this))
			return null;

		alreadySearched.add(this);

		for (final Module m : modules()) {
			final Type foundType = m.findType(name);
			if (foundType != null)
				return foundType;
		}

		for (final ProjectImpl d : m_dependencies) {
			final Type foundType = d.findType(name, alreadySearched);
			if (foundType != null)
				return foundType;
		}
		return null;
	}

}
