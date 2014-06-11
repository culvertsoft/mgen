package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Project;
import se.culvertsoft.mgen.api.plugins.GeneratorDescriptor;

public class ProjectImpl implements Project {

	private String m_name;
	private String m_filePath;
	private String m_absoluteFilePath;
	private Map<String, String> m_settings;
	private List<ModuleImpl> m_modules;
	private List<ProjectImpl> m_dependencies;
	private List<GeneratorDescriptor> m_generators;
	private boolean m_isRoot;

	public ProjectImpl(
			final String name,
			final String filePath,
			final String absoluteFilePath,
			final boolean isRoot) {
		m_name = name;
		m_filePath = filePath;
		m_absoluteFilePath = absoluteFilePath;
		m_settings = new HashMap<String, String>();
		m_modules = new ArrayList<ModuleImpl>();
		m_dependencies = new ArrayList<ProjectImpl>();
		m_generators = new ArrayList<GeneratorDescriptor>();
		m_isRoot = isRoot;
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
		return m_isRoot;
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

	public void setIsRoot(boolean state) {
		m_isRoot = state;
	}

	@Override
	public List<GeneratorDescriptor> generators() {
		return m_generators;
	}

	public void setGenerators(List<GeneratorDescriptor> generators) {
		m_generators = generators;
	}

}
