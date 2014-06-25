package se.culvertsoft.mgen.javapack.classes;

import java.util.Collection;
import java.util.HashMap;

public class ClassRegistry {

	private final HashMap<Long, ClassRegistryEntry> m_typeId2Entry;
	private final HashMap<String, ClassRegistryEntry> m_name2Entry;

	public ClassRegistry() {
		m_typeId2Entry = new HashMap<Long, ClassRegistryEntry>();
		m_name2Entry = new HashMap<String, ClassRegistryEntry>();
	}

	public void add(final ClassRegistryEntry entry) {
		m_typeId2Entry.put(entry.typeId(), entry);
		m_name2Entry.put(entry.clsName(), entry);
	}

	public ClassRegistryEntry getByTypeIds16Bit(final short[] ids) {
		return null;
	}

	public ClassRegistryEntry getByTypeIds16BitBase64(final String[] ids) {
		return null;
	}

	public ClassRegistryEntry getByName(final String fullClassName) {
		return m_name2Entry.get(fullClassName);
	}

	public ClassRegistryEntry getById(final long typeId) {
		return m_typeId2Entry.get(typeId);
	}

	public Collection<ClassRegistryEntry> entries() {
		return m_name2Entry.values();
	}

}
