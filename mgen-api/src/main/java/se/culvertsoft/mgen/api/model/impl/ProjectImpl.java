package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Project;
import se.culvertsoft.mgen.api.model.Type;

public class ProjectImpl implements Project {

	private String m_name;
	private String m_filePath;
	private String m_absoluteFilePath;
	private Map<String, String> m_settings;
	private List<ModuleImpl> m_modules;
	private List<ProjectImpl> m_dependencies;
	private List<GeneratorDescriptorImpl> m_generators;
	private final ProjectImpl m_parent;

	public ProjectImpl(
			final String name,
			final String filePath,
			final String absoluteFilePath,
			final ProjectImpl parent) {
		m_name = name;
		m_filePath = filePath;
		m_absoluteFilePath = absoluteFilePath;
		m_settings = new HashMap<String, String>();
		m_modules = new ArrayList<ModuleImpl>();
		m_dependencies = new ArrayList<ProjectImpl>();
		m_generators = new ArrayList<GeneratorDescriptorImpl>();
		m_parent = parent;
	}

	@Override
	public String name() {
		return m_name;
	}

	@Override
	public String filePath() {
		return m_filePath;
	}

	@Override
	public String absoluteFilePath() {
		return m_absoluteFilePath;
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

	@Override
	public boolean isRoot() {
		return m_parent == null;
	}

	public void setName(String name) {
		m_name = name;
	}

	public void setFilePath(String filePath) {
		m_filePath = filePath;
	}

	public void setAbsoluteFilePath(String absolutefilePath) {
		m_absoluteFilePath = absolutefilePath;
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
	public Project parent() {
		return m_parent;
	}

	@Override
	public List<GeneratorDescriptorImpl> generators() {
		return m_generators;
	}

	public void setGenerators(List<GeneratorDescriptorImpl> generators) {
		m_generators = generators;
	}

	private Type findType(final String name, final HashSet<Project> alreadySearchedProjects) {

		for (final Module m : modules()) {
			final Type foundType = m.findType(name);
			if (foundType != null)
				return foundType;
		}
		alreadySearchedProjects.add(this);

		for (final ProjectImpl d : m_dependencies) {
			final Type foundType = d.findType(name, alreadySearchedProjects);
			if (foundType != null) {
				return foundType;
			}
		}
		return null;
	}

	@Override
	public Type findType(final String name) {
		if (isRoot()) {
			final HashSet<Project> alreadySearchedProjects = new HashSet<Project>();
			return findType(name, alreadySearchedProjects);
		} else {
			return m_parent.findType(name);
		}
	}

}
