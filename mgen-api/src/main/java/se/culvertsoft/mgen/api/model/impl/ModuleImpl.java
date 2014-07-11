package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.EnumType;
import se.culvertsoft.mgen.api.model.Module;

public class ModuleImpl implements Module {

	private final String m_path;
	private final String m_filePath;
	private final String m_absoluteFilePath;
	private final Map<String, String> m_settings;
	private final ArrayList<LinkedCustomType> m_types;
	private final ArrayList<EnumTypeImpl> m_enums;

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
		m_enums = new ArrayList<EnumTypeImpl>();
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EnumType> enums() {
		return (List) m_enums;
	}

	public List<LinkedCustomType> typesMutable() {
		return m_types;
	}

	public List<EnumTypeImpl> enumsMutable() {
		return m_enums;
	}

	public void setTypes(final List<LinkedCustomType> types) {
		m_types.clear();
		m_types.addAll(types);
	}

	public void setEnums(final List<EnumTypeImpl> enums) {
		m_enums.clear();
		m_enums.addAll(enums);
	}

	public void addType(final LinkedCustomType type) {
		m_types.add(type);
	}

}
