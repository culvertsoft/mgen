package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import se.culvertsoft.mgen.api.model.ParsedSources;
import se.culvertsoft.mgen.api.model.Project;
import se.culvertsoft.mgen.api.model.Type;

public class ProjectImpl extends ParsedSourcesImpl implements Project {

	private String m_name;
	private String m_filePath;
	private String m_absoluteFilePath;
	private List<GeneratorDescriptorImpl> m_generators;
	private final Project m_parent;

	public ProjectImpl(
			final String name,
			final String filePath,
			final String absoluteFilePath,
			final Project parent) {
		m_name = name;
		m_filePath = filePath;
		m_absoluteFilePath = absoluteFilePath;
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

	@Override
	public Type findType(final String name) {
		if (isRoot()) {
			return findType(name, new HashSet<ParsedSources>());
		} else {
			return m_parent.findType(name);
		}
	}

}
