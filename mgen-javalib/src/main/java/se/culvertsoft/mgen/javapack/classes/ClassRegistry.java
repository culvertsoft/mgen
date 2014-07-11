package se.culvertsoft.mgen.javapack.classes;

import java.util.Collection;
import java.util.HashMap;

public abstract class ClassRegistry {

	private final HashMap<Long, ClassRegistryEntry> m_typeId2Entry;
	private final HashMap<String, ClassRegistryEntry> m_name2Entry;
	private final HashMap<Class<?>, ClassRegistryEntry> m_cls2Entry;

	public ClassRegistry() {
		m_typeId2Entry = new HashMap<Long, ClassRegistryEntry>();
		m_name2Entry = new HashMap<String, ClassRegistryEntry>();
		m_cls2Entry = new HashMap<Class<?>, ClassRegistryEntry>();
	}

	public void add(final ClassRegistryEntry entry) {
		m_typeId2Entry.put(entry.typeId(), entry);
		m_name2Entry.put(entry.clsName(), entry);
		m_cls2Entry.put(entry.cls(), entry);
	}

	public ClassRegistryEntry getByName(final String fullClassName) {
		return m_name2Entry.get(fullClassName);
	}

	public ClassRegistryEntry getById(final long typeId) {
		return m_typeId2Entry.get(typeId);
	}

	public ClassRegistryEntry getByClass(final Class<?> cls) {
		return m_cls2Entry.get(cls);
	}

	public Collection<ClassRegistryEntry> entries() {
		return m_name2Entry.values();
	}

	public abstract ClassRegistryEntry getByTypeIds16Bit(final short[] ids);

	public abstract ClassRegistryEntry getByTypeIds16BitBase64(final String[] ids);

}
