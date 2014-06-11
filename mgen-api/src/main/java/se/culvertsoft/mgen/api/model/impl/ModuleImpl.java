package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Type;

public class ModuleImpl implements Module {

	private final String m_path;
	private final String m_filePath;
	private final String m_absoluteFilePath;
	private final Map<String, String> m_settings;
	private LinkedHashMap<String, CustomTypeImpl> m_types;

	public ModuleImpl(
			final String path, 
			final String filePath,
			final String absoluteFilePath, 
			final Map<String, String> settings) {
		m_path = path;
		m_filePath = filePath;
		m_absoluteFilePath = absoluteFilePath;
		m_settings = settings;
		m_types = new LinkedHashMap<String, CustomTypeImpl>();
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
	public Map<String, CustomType> types() {
		return (Map<String, CustomType>) ((Map) m_types);
	}

	public HashMap<String, CustomTypeImpl> typesMutable() {
		return m_types;
	}

	public void setTypes(final List<CustomTypeImpl> types) {
		m_types.clear();
		for (final CustomTypeImpl type : types) {
			addType(type);
		}
	}

	public void addType(final CustomTypeImpl type) {
		m_types.put(type.name(), type);
	}

	@Override
	public List<Type> getAllKnownTypesWithName(String typeName) {
		final ArrayList<Type> out = new ArrayList<Type>();
		final Type thisModuleType = m_types.get(typeName);
		if (thisModuleType != null)
			out.add(thisModuleType);
		return out;
	}

}
