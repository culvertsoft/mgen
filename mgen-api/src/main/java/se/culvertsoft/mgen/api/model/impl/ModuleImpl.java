package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Module;

public class ModuleImpl implements Module {

	private final String m_path;
	private final String m_filePath;
	private final String m_absoluteFilePath;
	private final Map<String, String> m_settings;
	private final ArrayList<LinkedCustomType> m_types;

	public ModuleImpl(
			final String path,
			final String filePath,
			final String absoluteFilePath,
			final Map<String, String> settings) {
		m_path = path;
		m_filePath = filePath;
		m_absoluteFilePath = absoluteFilePath;
		m_settings = settings;
		m_types = new ArrayList<LinkedCustomType>();
	}

	public Map<String, String> settings() {
		return m_settings;
	}

	public String path() {
		return m_path;
	}

	public String filePath() {
		return m_filePath;
	}

	public String absoluteFilePath() {
		return m_absoluteFilePath;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CustomType> types() {
		return (List) m_types;
	}

	public List<LinkedCustomType> typesMutable() {
		return m_types;
	}

	public void setTypes(final List<LinkedCustomType> types) {
		m_types.clear();
		for (final LinkedCustomType type : types) {
			addType(type);
		}
	}

	public void addType(final LinkedCustomType type) {
		m_types.add(type);
	}

}
